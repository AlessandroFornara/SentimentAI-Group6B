package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import polimi.aui.sentimentaigroup6b.entities.Audio;
import polimi.aui.sentimentaigroup6b.entities.BadgeType;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.models.*;
import polimi.aui.sentimentaigroup6b.models.emotionAI.Emotion;
import polimi.aui.sentimentaigroup6b.models.emotionAI.EmotionAIResponse;
import polimi.aui.sentimentaigroup6b.repositories.UserRepo;
import polimi.aui.sentimentaigroup6b.utils.gamification.PointsManager;
import polimi.aui.sentimentaigroup6b.models.llm.Message;
import polimi.aui.sentimentaigroup6b.repositories.AudioRepo;
import polimi.aui.sentimentaigroup6b.repositories.SessionRepo;
import polimi.aui.sentimentaigroup6b.utils.CachingComponent;
import polimi.aui.sentimentaigroup6b.utils.EmotionAIRequestGenerator;
import polimi.aui.sentimentaigroup6b.utils.OpenAIRequestGenerator;

import java.util.*;

@AllArgsConstructor
@Service
public class SessionService {

    private final AudioRepo audioRepo;
    private final BadgeService badgeService;
    private final SessionRepo sessionRepo;
    private final UserRepo userRepo;

    private final OpenAIRequestGenerator openAIRequestGenerator;
    private final EmotionAIRequestGenerator emotionAIRequestGenerator;

    private final CachingComponent cachingComponent;

    private final PointsManager pointsManager;

    //TODO: check user is not in a session already (?)
    public ServerResponse createSession(User worker) {

        return ServerResponse.SESSION_CREATED;
    }

    public ServerResponse startSession(User worker){
        // Create a new session
        Session session = new Session(worker, new Date());
        try {
            sessionRepo.save(session);
        } catch (Exception e) {
            System.err.println("Error saving session: " + e.getMessage());
            return ServerResponse.SESSION_CREATION_ERROR;
        }
        return ServerResponse.SESSION_STARTED;
    }

    public Message handleAudio(User worker, byte[] audio, String audioTranscript) {
        Session session = getUserActiveSession(worker);
        if(session == null) return null;

        Long sessionId = session.getId();

        Audio audioEntity = new Audio(session, audio);
        try {
            audioRepo.save(audioEntity);
        } catch (Exception e) {
            System.err.println("Error saving audio: " + e.getMessage());
            return null;
        }
        String fileURI, emotionValues;
        EmotionAIResponse response;
        try {
            //Upload audio and analyze emotions
            fileURI = emotionAIRequestGenerator.uploadAudioToAIServer(audio);
            response = emotionAIRequestGenerator.sendEmotionDetectionRequest(fileURI);
            emotionValues = response.getEmotions().toString();
            audioEntity.setDetectedEmotions(response.getEmotionsAsList());
            audioRepo.save(audioEntity);
        } catch (Exception e) {
            System.err.println("Error analyzing audio: " + e.getMessage());
            return null;
        }

        List<Message> chatMessages = null;
        try {
            //Add new audio to the chat as a message from the user
            chatMessages = cachingComponent.getChatMessages(sessionId);
            chatMessages.add(new Message("user", "Detected emotions: " + emotionValues
                    + "\nUser message: " + audioTranscript));
        } catch (Exception e) {
            System.err.println("Error saving audio to chat: " + e.getMessage());
        }

        Message answer;
        try {
            //Generate response using the llm and add it to the chat as a message
            answer = openAIRequestGenerator.sendRequestToAzureOpenAI(chatMessages);
            if (answer == null || answer.getContent() == null || answer.getContent().isEmpty())
                throw new Exception("response is null");
        } catch (Exception e) {
            System.err.println("Error generating response: " + e.getMessage());
            return null;
        }

        try {
            //Save the updated chat to the cache
            chatMessages.add(answer);
            cachingComponent.saveChat(sessionId, chatMessages);
        } catch (Exception e) {
            System.err.println("Error saving chat: " + e.getMessage());
            return null;
        }
        return answer;
    }

    @Transactional(rollbackFor = Exception.class)
    public FinalResponse endSession(User worker){
        Session session = getUserActiveSession(worker);
        if(session == null) return null;

        List<AudioEmotionsDTO> detectedEmotions = audioRepo.findEmotionsBySession(session);
        if (detectedEmotions == null || detectedEmotions.isEmpty()) {
            return null;
        }
        Emotion dominantEmotion = computeDominantEmotion(detectedEmotions);
        ActivityResponse activity = chooseActivity(dominantEmotion);

        int points = pointsManager.calculateXPForSession(activity, detectedEmotions.size());
        int totalXP = worker.getPoints() + points;
        int level = pointsManager.calculateUserLevel(totalXP);

        worker.setPoints(totalXP);
        worker.setLevel(level);

        Map<BadgeType, Integer> badges = badgeService.assignBadges(worker);
        System.out.println("Badges: " + badges);

        session.setDominantEmotion(dominantEmotion.getEmotion());
        session.setActivityCategory(activity.getActivityCategory());
        session.setActivityText(activity.getActivityText());
        sessionRepo.save(session);

        userRepo.save(worker);

        cachingComponent.deleteChat(session.getId());
        System.out.println("Chat deleted");
        return new FinalResponse(dominantEmotion.getEmotion(),
                activity,
                badges,
                points,
                level);
    }

    private Emotion computeDominantEmotion(List<AudioEmotionsDTO> detectedEmotions) {
        if (detectedEmotions == null || detectedEmotions.isEmpty()) {
            return null;
        }

        double totalAnger = 0, totalDisgust = 0, totalFear = 0, totalJoy = 0,
                totalSadness = 0, totalSurprise = 0, totalNeutrality = 0;
        int count = detectedEmotions.size();

        for (AudioEmotionsDTO dto : detectedEmotions) {
            totalAnger += dto.getAnger();
            totalDisgust += dto.getDisgust();
            totalFear += dto.getFear();
            totalJoy += dto.getJoy();
            totalSadness += dto.getSadness();
            totalSurprise += dto.getSurprise();
            totalNeutrality += dto.getNeutrality();
        }

        double avgAnger = totalAnger / count;
        double avgDisgust = totalDisgust / count;
        double avgFear = totalFear / count;
        double avgJoy = totalJoy / count;
        double avgSadness = totalSadness / count;
        double avgSurprise = totalSurprise / count;
        double avgNeutrality = totalNeutrality / count;

        Emotion dominantEmotion = null;
        double maxAverage = Double.NEGATIVE_INFINITY;

        if (avgAnger > maxAverage) {
            maxAverage = avgAnger;
            dominantEmotion = Emotion.ANGER;
        }
        if (avgDisgust > maxAverage) {
            maxAverage = avgDisgust;
            dominantEmotion = Emotion.DISGUST;
        }
        if (avgFear > maxAverage) {
            maxAverage = avgFear;
            dominantEmotion = Emotion.FEAR;
        }
        if (avgJoy > maxAverage) {
            maxAverage = avgJoy;
            dominantEmotion = Emotion.JOY;
        }
        if (avgSadness > maxAverage) {
            maxAverage = avgSadness;
            dominantEmotion = Emotion.SADNESS;
        }
        if (avgSurprise > maxAverage) {
            maxAverage = avgSurprise;
            dominantEmotion = Emotion.SURPRISE;
        }
        if (avgNeutrality > maxAverage) {
            maxAverage = avgNeutrality;
            dominantEmotion = Emotion.NEUTRALITY;
        }

        return dominantEmotion;
    }


    public ActivityResponse chooseActivity(Emotion emotion){
        Activity[] activities = Activity.values();
        Random random = new Random();
        Activity randomActivity = activities[random.nextInt(activities.length)];
        return new ActivityResponse(randomActivity.getCategoryDescription(), randomActivity.assignActivity(emotion));
    }

    private Session getUserActiveSession(User worker) {
        Session session;
        try {
            session = sessionRepo.findTopByUserIdOrderByDateDesc(worker).orElse(null);
            if (session == null) {
                throw new Exception("session not found for user id: " + worker.getId());
            }
        } catch (Exception e) {
            System.err.println("Error retrieving session: " + e.getMessage());
            return null;
        }
        return session;
    }
}

package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import polimi.aui.sentimentaigroup6b.entities.Audio;
import polimi.aui.sentimentaigroup6b.entities.BadgeType;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.models.*;
import polimi.aui.sentimentaigroup6b.models.emotionAI.Emotion;
import polimi.aui.sentimentaigroup6b.models.emotionAI.EmotionAIResponse;
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

    public FinalResponse endSession(User worker){
        Session session = getUserActiveSession(worker);
        int numberOfAudios = audioRepo.countAudiosBySession(session);
        if(session == null) return null;

        Long sessionId = session.getId();
        Emotion dominantEmotion = getDominantEmotion(session);
        ActivityResponse activity = chooseActivity(dominantEmotion);
        System.out.println("Activity: " + activity.getActivityCategory() + " - " + activity.getActivityText());
        int points = pointsManager.calculateXPForSession(activity, numberOfAudios);
        System.out.println("Points: " + points);
        Map<BadgeType, Integer> badges = badgeService.assignBadges(sessionId);
        System.out.println("Badges: " + badges);

        session.setDominantEmotion(dominantEmotion.getEmotion());
        session.setActivityCategory(activity.getActivityCategory());
        session.setActivityText(activity.getActivityText());

        sessionRepo.save(session);
        cachingComponent.deleteChat(sessionId);
        System.out.println("Chat deleted");
        return new FinalResponse(dominantEmotion.getEmotion(),
                activity,
                badges,
                points);
    }

    public Emotion getDominantEmotion(Session session) {
        try {
            List<AudioEmotionsDTO> detectedEmotions = audioRepo.findEmotionsBySession(session);
            System.out.println("Audios: " + detectedEmotions);
            if (detectedEmotions == null || detectedEmotions.isEmpty()) {
                throw new Exception("audios not found for session_id: " + session.getId());
            }
            return computeDominantEmotion(detectedEmotions);
        } catch (Exception e) {
            System.err.println("Error in computing session dominant emotion: " + e.getMessage());
            return null;
        }
    }

    private Emotion computeDominantEmotion(List<AudioEmotionsDTO> detectedEmotions) {
        Emotion dominantEmotion = null;
        double maxIntensity = Double.NEGATIVE_INFINITY;

        for (AudioEmotionsDTO dto : detectedEmotions) {
            if (dto.getAnger() > maxIntensity) {
                maxIntensity = dto.getAnger();
                dominantEmotion = Emotion.ANGER;
            }
            if (dto.getDisgust() > maxIntensity) {
                maxIntensity = dto.getDisgust();
                dominantEmotion = Emotion.DISGUST;
            }
            if (dto.getFear() > maxIntensity) {
                maxIntensity = dto.getFear();
                dominantEmotion = Emotion.FEAR;
            }
            if (dto.getJoy() > maxIntensity) {
                maxIntensity = dto.getJoy();
                dominantEmotion = Emotion.JOY;
            }
            if (dto.getSadness() > maxIntensity) {
                maxIntensity = dto.getSadness();
                dominantEmotion = Emotion.SADNESS;
            }
            if (dto.getSurprise() > maxIntensity) {
                maxIntensity = dto.getSurprise();
                dominantEmotion = Emotion.SURPRISE;
            }
            if (dto.getNeutrality() > maxIntensity) {
                maxIntensity = dto.getNeutrality();
                dominantEmotion = Emotion.NEUTRALITY;
            }
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

package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import polimi.aui.sentimentaigroup6b.entities.Audio;
import polimi.aui.sentimentaigroup6b.entities.Badge;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.Worker;
import polimi.aui.sentimentaigroup6b.models.*;
import polimi.aui.sentimentaigroup6b.models.emotionAI.Emotion;
import polimi.aui.sentimentaigroup6b.models.emotionAI.EmotionAIResponse;
import polimi.aui.sentimentaigroup6b.models.llm.Message;
import polimi.aui.sentimentaigroup6b.repositories.AudioRepo;
import polimi.aui.sentimentaigroup6b.repositories.SessionRepo;
import polimi.aui.sentimentaigroup6b.utils.CachingComponent;
import polimi.aui.sentimentaigroup6b.utils.EmotionAIRequestGenerator;
import polimi.aui.sentimentaigroup6b.utils.ImageManager;
import polimi.aui.sentimentaigroup6b.utils.OpenAIRequestGenerator;

import java.util.Date;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class SessionService {

    private final AudioRepo audioRepo;
    private final BadgeService badgeService;
    private final SessionRepo sessionRepo;

    private final OpenAIRequestGenerator openAIRequestGenerator;
    private final EmotionAIRequestGenerator emotionAIRequestGenerator;

    private final CachingComponent cachingComponent;

    //TODO: check user is not in a session already (?)
    public ServerResponse createSession(Worker worker) {

        return ServerResponse.SESSION_CREATED;
    }

    public ServerResponse startSession(Worker worker){
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

    //TODO: passare l'emozione all'ai generativo
    public Message handleAudio(Session session, byte[] audio, String audioTranscript) {
        Audio audioEntity = new Audio(session, audio);
        try {
            audioRepo.save(audioEntity);
        } catch (Exception e) {
            System.err.println("Error saving audio: " + e.getMessage());
            return null;
        }
        String fileURI;
        EmotionAIResponse response;
        try {
            //Upload audio and analyze emotions
            fileURI = emotionAIRequestGenerator.uploadAudioToAIServer(audio);
            response = emotionAIRequestGenerator.sendEmotionDetectionRequest(fileURI);
            if (response == null)
                throw new Exception("response is null");
            audioEntity.setDetectedEmotions(response.getEmotionsAsList());
            audioEntity.setDominantEmotion(response.getDominantEmotion().getEmotion());
            audioRepo.save(audioEntity);
        } catch (Exception e) {
            System.err.println("Error analyzing audio: " + e.getMessage());
            return null;
        }

        List<Message> chatMessages = null;
        try {
            //Add new audio to the chat as a message from the user
            chatMessages = cachingComponent.getChatMessages(session.getId());
            chatMessages.add(new Message("user", audioTranscript));
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
            cachingComponent.saveChat(session.getId(), chatMessages);
        } catch (Exception e) {
            System.err.println("Error saving chat: " + e.getMessage());
            return null;
        }
        return answer;
    }

    public FinalResponse endSession(Long sessionId){

        Emotion dominantEmotion = getDominantEmotion();
        ActivityResponse activity = chooseActivity(dominantEmotion);
        List<Badge> badges = badgeService.assignBadges(sessionId);
        cachingComponent.deleteChat(sessionId);

        return new FinalResponse(dominantEmotion.getEmotion(),
                activity,
                badges);
    }

    public Emotion getDominantEmotion(EmotionAIResponse emotionAIResponse){
        return emotionAIResponse.getDominantEmotion();
    }

    public ActivityResponse chooseActivity(Emotion emotion){
        Activity[] activities = Activity.values();
        Random random = new Random();
        Activity randomActivity = activities[random.nextInt(activities.length)];
        return new ActivityResponse(randomActivity.getCategoryDescription(), randomActivity.assignActivity(emotion));
    }
}

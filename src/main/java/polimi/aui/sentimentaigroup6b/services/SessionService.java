package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import polimi.aui.sentimentaigroup6b.entities.Badge;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.Worker;
import polimi.aui.sentimentaigroup6b.models.*;
import polimi.aui.sentimentaigroup6b.models.emotionAI.Emotion;
import polimi.aui.sentimentaigroup6b.models.emotionAI.EmotionAIResponse;
import polimi.aui.sentimentaigroup6b.models.llm.Message;
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

    private final BadgeService badgeService;
    private final SessionRepo sessionRepo;

    private final OpenAIRequestGenerator openAIRequestGenerator;
    private final EmotionAIRequestGenerator emotionAIRequestGenerator;

    private final CachingComponent cachingComponent;
    private final ImageManager imageManager;

    private final String aiInstructions;

    public List<ImageResponse> createSession(Worker worker) {

        Session session = new Session(worker, null);
        sessionRepo.save(session);

        return imageManager.extractImages();
    }

    public ServerResponse startSession(Long sessionId){

        sessionRepo.findById(sessionId).ifPresent(session -> {
            session.setDate(new Date());
            sessionRepo.save(session);
        });

        List<Message> chatMessages = cachingComponent.getChatMessages(sessionId);
        chatMessages.add(new Message("system", aiInstructions));

        return ServerResponse.SESSION_STARTED;
    }

    //TODO: passare l'emozione all'ai generativo
    public void handleAudio(Long sessionId, byte[] audio, String audioTranscript) {
        //Upload audio and analyze emotions
        String fileURI = emotionAIRequestGenerator.uploadAudioToAIServer(audio);
        EmotionAIResponse response = emotionAIRequestGenerator.sendEmotionDetectionRequest(fileURI);

        //Add new audio to the chat as a message from the user
        List<Message> chatMessages = cachingComponent.getChatMessages(sessionId);
        chatMessages.add(new Message("user", audioTranscript));

        //Generate response using the llm and add it to the chat as a message
        Message answer = openAIRequestGenerator.sendRequestToAzureOpenAI(chatMessages);
        chatMessages.add(answer);

        //Save the updated chat to the cache
        cachingComponent.saveChat(sessionId, chatMessages);
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

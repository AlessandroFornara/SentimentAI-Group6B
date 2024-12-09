package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import polimi.aui.sentimentaigroup6b.entities.Badge;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.Worker;
import polimi.aui.sentimentaigroup6b.models.*;
import polimi.aui.sentimentaigroup6b.models.ai.Message;
import polimi.aui.sentimentaigroup6b.models.ai.RequestPayloadAI;
import polimi.aui.sentimentaigroup6b.repositories.SessionRepo;
import polimi.aui.sentimentaigroup6b.utils.CachingComponent;
import polimi.aui.sentimentaigroup6b.utils.ImageManager;
import polimi.aui.sentimentaigroup6b.utils.OpenAIRequestGenerator;
import polimi.aui.sentimentaigroup6b.utils.PythonRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SessionService {

    private final BadgeService badgeService;
    private final PythonRunner runner;
    private final SessionRepo sessionRepo;
    private final OpenAIRequestGenerator openAIRequestGenerator;
    private final CachingComponent cachingComponent;
    private final String aiInstructions;
    private final ImageManager imageManager;

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
    public void handleAudio(Long sessionId, String audio, String audioTranscript) {
        //sendEmotionDetectionRequest(audio);

        List<Message> chatMessages = cachingComponent.getChatMessages(sessionId);
        chatMessages.add(new Message("user", audioTranscript));

        Message answer = openAIRequestGenerator.sendRequestToAzureOpenAI(chatMessages);
        chatMessages.add(answer);

        cachingComponent.saveChat(sessionId, chatMessages);

        List<Message> chat = cachingComponent.getChatMessages(sessionId);
        System.out.println(chat);
    }

    public FinalResponse endSession(Long sessionId){

        String dominantEmotion = detectDominantEmotion(sessionId);
        ActivityResponse activity = chooseActivity(sessionId);
        List<Badge> badges = badgeService.assignBadges(sessionId);

        return new FinalResponse(dominantEmotion,
                activity.getActivityCategory(),
                activity.getActivityText(),
                badges);
    }

    public String detectDominantEmotion(Long sessionId){

        return null;
    }

    public ActivityResponse chooseActivity(Long sessionId){

        return null;
    }

    //TODO: Cambiare String a byte[]
    public void sendEmotionDetectionRequest(String audio){
        runner.runPythonFunction(audio);
    }

    public List<String> getAllImages() {
        Path imagesPath = Paths.get("src/main/resources/static/images");
        try {
            return Files.list(imagesPath)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }




}

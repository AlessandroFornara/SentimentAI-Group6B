package polimi.aui.sentimentaigroup6b.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.models.llm.Message;
import polimi.aui.sentimentaigroup6b.services.ProfileService;
import polimi.aui.sentimentaigroup6b.services.SessionService;
import polimi.aui.sentimentaigroup6b.utils.ImageManager;

@RestController
@RequestMapping("/api/worker")
@AllArgsConstructor
public class WorkerController {

    private final SessionService sessionService;
    private final ProfileService profileService;

    private final ImageManager imageManager;

    @PostMapping("/create_session")
    public void createSession(User worker){
        //TODO
        //Extract images from the static folder
        imageManager.extractImages();
    }

    @PostMapping("/start_session")
    public void startSession(){
        //sessionService.startSession();
    }

    @PostMapping("handle_audio")
    public Message handleAudio(){
        //sessionService.handleAudio()
        return null;
    }

    @PostMapping("/end_session")
    public void endSession(){
        //TODO
        //sessionService.endSession();
    }
}

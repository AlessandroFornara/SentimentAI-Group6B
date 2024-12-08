package polimi.aui.sentimentaigroup6b.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polimi.aui.sentimentaigroup6b.entities.Worker;
import polimi.aui.sentimentaigroup6b.services.ProfileService;
import polimi.aui.sentimentaigroup6b.services.SessionService;

@RestController
@RequestMapping("/api/worker")
@AllArgsConstructor
public class WorkerController {

    private final SessionService sessionService;
    private final ProfileService profileService;

    @PostMapping("/create_session")
    public void createSession(Worker worker){
        sessionService.createSession(worker);
    }

    @PostMapping("/start_session")
    public void startSession(Long sessionId){
        sessionService.startSession(sessionId);
    }

    @PostMapping("handle_audio")
    public void handleAudio(){
        //TODO
    }

    @PostMapping("/end_session")
    public void endSession(){
        //TODO
        //sessionService.endSession();
    }
}

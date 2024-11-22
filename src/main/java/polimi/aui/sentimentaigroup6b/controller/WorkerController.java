package polimi.aui.sentimentaigroup6b.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polimi.aui.sentimentaigroup6b.services.ProfileService;
import polimi.aui.sentimentaigroup6b.services.SessionService;

@RestController
@RequestMapping("/api/worker")
@AllArgsConstructor
public class WorkerController {

    private final SessionService sessionService;
    private final ProfileService profileService;


}

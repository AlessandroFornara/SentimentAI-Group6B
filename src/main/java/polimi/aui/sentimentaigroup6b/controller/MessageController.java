package polimi.aui.sentimentaigroup6b.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.Worker;
import polimi.aui.sentimentaigroup6b.repositories.SessionRepo;
import polimi.aui.sentimentaigroup6b.repositories.WorkerRepo;
import polimi.aui.sentimentaigroup6b.services.SessionService;

import java.util.Date;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {

    private final WorkerRepo workerRepo;
    private final SessionRepo sessionRepo;
    private final SessionService sessionService;

    @GetMapping("/hello")
    public String hello() {
        return "Full Stack Java with Spring Boot & VueJS!";
    }

    @GetMapping("/test")
    public String testWorkerRepo() {

        Worker worker = workerRepo.findById(1L).get();

        Session session = new Session(
                worker,
                new Date()
        );
        sessionRepo.save(session);
        return "Full Stack Java with Spring Boot & VueJS!";
    }

    @PostMapping("/test_python_runner")
    public void testJep(){
        sessionService.sendEmotionDetectionRequest("Hello from Java");
    }

}
package polimi.aui.sentimentaigroup6b.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.models.ImageResponse;
import polimi.aui.sentimentaigroup6b.models.ServerResponse;
import polimi.aui.sentimentaigroup6b.models.llm.Message;
import polimi.aui.sentimentaigroup6b.repositories.UserRepo;
import polimi.aui.sentimentaigroup6b.services.ProfileService;
import polimi.aui.sentimentaigroup6b.services.SessionService;
import polimi.aui.sentimentaigroup6b.utils.ImageManager;

import java.util.List;

@RestController
@RequestMapping("/api/worker")
@AllArgsConstructor
public class WorkerController {

    private final SessionService sessionService;
    private final ProfileService profileService;
    private final UserRepo userRepo;

    private final ImageManager imageManager;

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/create_session")
    public ResponseEntity<?> createSession() {
        List<ImageResponse> images = imageManager.extractImages();

        if (images == null || images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error extracting images");
        }

        return ResponseEntity.ok(images);
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/start_session")
    public ResponseEntity<?> startSession(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        ServerResponse response = sessionService.startSession(user);
        if(response != ServerResponse.SESSION_STARTED){
            return ResponseEntity.unprocessableEntity().body(response.getMessage());
        }
        return ResponseEntity.ok(response.getMessage());
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("handle_audio")
    public Message handleAudio(){
        //sessionService.handleAudio()
        return null;
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/end_session")
    public void endSession(){
        //TODO
        //sessionService.endSession();
    }
}

package polimi.aui.sentimentaigroup6b.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.models.*;
import polimi.aui.sentimentaigroup6b.models.llm.Message;
import polimi.aui.sentimentaigroup6b.repositories.UserRepo;
import polimi.aui.sentimentaigroup6b.services.ProfileService;
import polimi.aui.sentimentaigroup6b.services.SessionService;
import polimi.aui.sentimentaigroup6b.utils.ImageManager;

import java.io.IOException;
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
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        ProfileResponse user = profileService.getProfile(email);

        if(user == null){
            return ResponseEntity.unprocessableEntity().body(ServerResponse.PROFILE_ERROR.getMessage());
        }

        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('WORKER')")
    @GetMapping("/history")
    public ResponseEntity<?> getHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        HistoryResponse history = profileService.getHistory(email);
        System.out.println(history);
        return ResponseEntity.ok(history);
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/create_session")
    public ResponseEntity<?> createSession() {
        List<ImageResponse> images = imageManager.getRandomImages(5);

        if (images == null || images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error extracting images");
        }

        return ResponseEntity.ok(images);
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/start_session")
    public ResponseEntity<?> startSession(@RequestBody String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        name = name.replace("\"", "").replaceAll("\\..*", "");
        ServerResponse response = sessionService.startSession(user, name);
        if(response != ServerResponse.SESSION_STARTED){
            return ResponseEntity.unprocessableEntity().body(response.getMessage());
        }
        return ResponseEntity.ok(response.getMessage());
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping(value = "/handle_audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> handleAudio(@RequestPart("audio") MultipartFile audio,
                                         @RequestPart("audioTranscript") String audioTranscript,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "en-US") String language) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        System.out.println(email);
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        byte[] audioBytes;
        try {
            audioBytes = audio.getBytes();
        } catch (IOException e) {
            return ResponseEntity.unprocessableEntity().body(ServerResponse.AUDIO_HANDLING_ERROR.getMessage());
        }

        Message answer = sessionService.handleAudio(user, audioBytes, audioTranscript, language);
        if(answer!=null){
            return ResponseEntity.ok(answer);
        }

        //TODO: just for testing
        //return ResponseEntity.ok(new Message("roleeee", audioTranscript));
        return ResponseEntity.unprocessableEntity().body(ServerResponse.AUDIO_HANDLING_ERROR.getMessage());
    }

    @PreAuthorize("hasRole('WORKER')")
    @PostMapping("/end_session")
    public ResponseEntity<?> endSession(@RequestHeader(value = "Accept-Language", defaultValue = "en-US") String language){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        FinalResponse response = sessionService.endSession(user, language);
        if (response != null) {
            System.out.println(response);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.unprocessableEntity().body(ServerResponse.SESSION_ENDED_ERROR.getMessage());
    }
}

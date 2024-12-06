package polimi.aui.sentimentaigroup6b.services;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.Worker;
import polimi.aui.sentimentaigroup6b.models.ImageResponse;
import polimi.aui.sentimentaigroup6b.models.ServerResponse;
import polimi.aui.sentimentaigroup6b.repositories.SessionRepo;
import polimi.aui.sentimentaigroup6b.repositories.WorkerRepo;
import polimi.aui.sentimentaigroup6b.utils.PythonRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SessionService {

    private final BadgeService badgeService;
    private final PythonRunner runner;
    private final SessionRepo sessionRepo;
    private final WorkerRepo workerRepo;

    public List<ImageResponse> createSession(Worker worker) {

        Session session = new Session(worker, null);
        sessionRepo.save(session);

        List<ImageResponse> images = new ArrayList<>();

        try {
            // Accedi alla directory delle immagini
            ClassPathResource resource = new ClassPathResource("static/images");
            Path imagesPath = resource.getFile().toPath();

            /*TODO: genera immagini con AI*/

            // Scansiona i file nella directory
            Files.list(imagesPath).forEach(filePath -> {
                try {
                    // Leggi il contenuto di ogni immagine
                    byte[] imageBytes = Files.readAllBytes(filePath);
                    String contentType = Files.probeContentType(filePath);
                    // Crea una risposta con metadati e contenuto
                    images.add(new ImageResponse(filePath.getFileName().toString(), contentType, imageBytes));
                } catch (IOException e) {
                    e.printStackTrace(); // Gestione degli errori (puoi aggiungere log o ignorare)
                }
            });

            return images;

        } catch (IOException e) {
            return null;
        }
    }

    public ServerResponse startSession(Long sessionId){

        sessionRepo.findById(sessionId).ifPresent(session -> {
            session.setDate(new Date());
            sessionRepo.save(session);
        });

        return ServerResponse.SESSION_STARTED;
    }

    public void handleAudio(String audio) {
        sendEmotionDetectionRequest(audio);
        /*

         */
    }

    public void endSession(){
        /*
        Salva timestamp nel database di fine sessione
        Calcola badge, activities e restituisce all'utente
         */
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

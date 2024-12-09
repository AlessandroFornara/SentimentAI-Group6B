package polimi.aui.sentimentaigroup6b.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.Worker;
import polimi.aui.sentimentaigroup6b.models.ImageResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageManager {

    public List<ImageResponse> extractImages() {

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
}

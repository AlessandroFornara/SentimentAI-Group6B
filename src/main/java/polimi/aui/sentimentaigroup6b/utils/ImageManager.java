package polimi.aui.sentimentaigroup6b.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.models.ImageResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class ImageManager {

    public List<ImageResponse> extractImages() {

        List<ImageResponse> images = new ArrayList<>();

        try {
            ClassPathResource resource = new ClassPathResource("static/images");
            Path imagesPath = resource.getFile().toPath();

            Files.list(imagesPath).forEach(filePath -> {
                try {
                    byte[] imageBytes = Files.readAllBytes(filePath);
                    String contentType = Files.probeContentType(filePath);

                    String base64Data = Base64.getEncoder().encodeToString(imageBytes);

                    images.add(new ImageResponse(filePath.getFileName().toString(), contentType, base64Data));
                } catch (IOException e) {
                    System.out.println("Error reading image: " + filePath.getFileName());
                }
            });

            return images;

        } catch (IOException e) {
            return null;
        }
    }
}

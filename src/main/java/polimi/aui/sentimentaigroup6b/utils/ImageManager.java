package polimi.aui.sentimentaigroup6b.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.models.ImageResponse;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Component
public class ImageManager {

    public List<ImageResponse> extractImages() {
        List<ImageResponse> images = new ArrayList<>();

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resourceUrl = classLoader.getResource("static/images");

            if (resourceUrl == null) {
                System.out.println("Directory 'static/images' not found");
                return images;
            }

            if (resourceUrl.getProtocol().equals("jar")) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(resourceUrl.toURI(), Collections.emptyMap())) {
                    Path imagesPath = fileSystem.getPath("static/images");
                    processDirectory(imagesPath, images);
                }
            } else {
                Path imagesPath = Paths.get(resourceUrl.toURI());
                processDirectory(imagesPath, images);
            }

        } catch (Exception e) {
            return null;
        }

        return images;
    }

    private void processDirectory(Path directoryPath, List<ImageResponse> images) throws IOException {
        Files.walk(directoryPath).filter(Files::isRegularFile).forEach(filePath -> {
            try {
                byte[] imageBytes = Files.readAllBytes(filePath);
                String contentType = Files.probeContentType(filePath);

                String base64Data = Base64.getEncoder().encodeToString(imageBytes);

                images.add(new ImageResponse(filePath.getFileName().toString(), contentType, base64Data));
            } catch (IOException e) {
                System.out.println("Error reading image: " + filePath.getFileName());
            }
        });
    }
}

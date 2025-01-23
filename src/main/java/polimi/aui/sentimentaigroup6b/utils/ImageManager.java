package polimi.aui.sentimentaigroup6b.utils;

import polimi.aui.sentimentaigroup6b.models.ImageResponse;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class ImageManager {

    private final List<ImageResponse> allImages = new ArrayList<>();

    public void preloadImages() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resourceUrl = classLoader.getResource("static/images");

            if (resourceUrl == null) {
                System.out.println("Directory 'static/images' not found");
                return;
            }

            if (resourceUrl.getProtocol().equals("jar")) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(resourceUrl.toURI(), Collections.emptyMap())) {
                    Path imagesPath = fileSystem.getPath("static/images");
                    processAllFiles(imagesPath);
                }
            } else {
                Path imagesPath = Paths.get(resourceUrl.toURI());
                processAllFiles(imagesPath);
            }

        } catch (Exception e) {
            System.out.println("Error preloading images: " + e.getMessage());
        }
    }

    public List<ImageResponse> getRandomImages(int numberOfRandomImages) {
        if (allImages.isEmpty()) {
            System.out.println("No images preloaded.");
            return Collections.emptyList();
        }

        List<ImageResponse> randomImages = new ArrayList<>(allImages);
        Collections.shuffle(randomImages);
        return randomImages.stream()
                .limit(numberOfRandomImages)
                .toList();
    }

    private void processAllFiles(Path directoryPath) throws IOException {
        List<Path> allFiles = Files.walk(directoryPath)
                .filter(Files::isRegularFile)
                .toList();

        for (Path filePath : allFiles) {
            try {
                byte[] imageBytes = Files.readAllBytes(filePath);
                String contentType = Files.probeContentType(filePath);

                String base64Data = Base64.getEncoder().encodeToString(imageBytes);

                allImages.add(new ImageResponse(filePath.getFileName().toString(), contentType, base64Data));
            } catch (IOException e) {
                System.out.println("Error reading image: " + filePath.getFileName());
            }
        }
    }

    public List<ImageResponse> getAllImages() {
        return new ArrayList<>(allImages);
    }
}

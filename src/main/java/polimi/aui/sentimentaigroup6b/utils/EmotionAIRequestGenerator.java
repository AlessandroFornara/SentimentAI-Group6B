package polimi.aui.sentimentaigroup6b.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Remove;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmotionAIRequestGenerator {

    @Value("${ai.emotion.url}")
    private String API_URL;
    @Value("${ai.emotion.applicationId}")
    private String API_APPLICATION_ID;
    @Value("${ai.emotion.conversationId}")
    private String API_CONVERSATION_ID;
    @Value("${ai.emotion.conversationalStepId}")
    private String API_CONVERSATIONAL_STEP_ID;
    @Value("${ai.emotion.token}")
    private String API_TOKEN;

    @Autowired
    private RestTemplate restTemplate;

    public String uploadAudioToAIServer(byte[] audio){

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "*/*");
        headers.set("applicationId", API_APPLICATION_ID);
        headers.set("conversationId", API_CONVERSATION_ID);
        headers.set("conversationalStepId", API_CONVERSATIONAL_STEP_ID);
        headers.set("token", API_TOKEN);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource fileResource = new ByteArrayResource(audio);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("fileAttachment", fileResource);

        JSONObject serviceData = new JSONObject();
        serviceData.put("name", "service");
        serviceData.put("account", new JSONObject());
        serviceData.put("serviceParams", new JSONObject());

        body.add("service", serviceData.toString());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        String fileUri;
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("File upload successful");
            JSONObject jsonResponse = new JSONObject(response.getBody());
            fileUri = jsonResponse.getJSONObject("resp").getString("fileUri");
            //fileUri = response.getHeaders().getLocation().toString();
            System.out.println("File URI: " + fileUri);
            return fileUri;
        } else {
            System.out.println("File upload failed with status code " + response.getStatusCode());
            System.out.println("Response content: " + response.getBody());
            return null;
        }
    }

    public byte[] readAudioFileToByteArray(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        return Files.readAllBytes(path);
    }
}

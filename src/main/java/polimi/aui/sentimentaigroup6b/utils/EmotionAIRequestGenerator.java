package polimi.aui.sentimentaigroup6b.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import polimi.aui.sentimentaigroup6b.models.emotionAI.EmotionAIResponse;

@Component
public class EmotionAIRequestGenerator {

    @Value("${ai.emotion.upload.url}")
    private String API_UPLOAD_URL;
    @Value("${ai.emotion.analyze.url}")
    private String API_ANALYZE_URL;
    @Value("${ai.emotion.applicationId}")
    private String API_APPLICATION_ID;
    @Value("${ai.emotion.conversationId}")
    private String API_CONVERSATION_ID;
    @Value("${ai.emotion.conversationalStepId}")
    private String API_CONVERSATIONAL_STEP_ID;
    @Value("${ai.emotion.token}")
    private String API_TOKEN;
    @Value("${ai.emotion.name}")
    private String API_NAME;

    @Setter
    private String language = "en-US";

    @Autowired
    private RestTemplate restTemplate;

    public String uploadAudioToAIServer(byte[] audio) throws Exception{

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
                API_UPLOAD_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        String fileUri;
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("File upload successful");
            JSONObject jsonResponse = new JSONObject(response.getBody());
            fileUri = jsonResponse.getJSONObject("resp").getString("fileUri");
            System.out.println("File URI: " + fileUri);
            return fileUri;
        } else {
            System.out.println("File upload failed with status code " + response.getStatusCode());
            System.out.println("Response content: " + response.getBody());
            return null;
        }
    }

    public EmotionAIResponse sendEmotionDetectionRequest(String fileUri) throws Exception{

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "*/*");
        headers.set("applicationId", API_APPLICATION_ID);
        headers.set("conversationId", API_CONVERSATION_ID);
        headers.set("conversationalStepId", API_CONVERSATIONAL_STEP_ID);
        headers.set("token", API_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject payload = new JSONObject();

        JSONObject speech = new JSONObject();
        speech.put("type", "URI");
        speech.put("data", fileUri);
        payload.put("speech", speech);

        payload.put("language", language);

        JSONObject service = new JSONObject();
        service.put("name", API_NAME);
        service.put("account", new JSONObject());
        service.put("serviceParams", new JSONObject());
        payload.put("service", service);

        HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                API_ANALYZE_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        System.out.println("Response: " + response);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());

        JsonNode dataNode = rootNode.path("data");

        if (!dataNode.isEmpty()) {
            JsonNode rawNode = dataNode.path("raw");

            return new EmotionAIResponse(
                    rawNode.path("anger").asDouble(),
                    rawNode.path("disgust").asDouble(),
                    rawNode.path("fear").asDouble(),
                    rawNode.path("joy").asDouble(),
                    rawNode.path("sadness").asDouble(),
                    rawNode.path("surprise").asDouble(),
                    rawNode.path("neutrality").asDouble()
                    );
        }

        return null;
    }

}

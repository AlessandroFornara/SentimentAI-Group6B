package polimi.aui.sentimentaigroup6b.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import polimi.aui.sentimentaigroup6b.models.llm.Message;
import polimi.aui.sentimentaigroup6b.models.llm.RequestPayloadAI;

import java.util.List;

@Component
public class OpenAIRequestGenerator {

    @Value("${ai.llm.url}")
    private String API_URL;
    @Value("${ai.llm.key}")
    private String API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    public Message sendRequestToAzureOpenAI(List<Message> chatMessages) {

        RequestPayloadAI requestPayloadAI = new RequestPayloadAI();
        requestPayloadAI.setMessages(chatMessages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", API_KEY);

        HttpEntity<RequestPayloadAI> entity = new HttpEntity<>(requestPayloadAI, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            JsonNode choicesNode = rootNode.path("choices");

            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                JsonNode messageNode = choicesNode.get(0).path("message");

                String role = messageNode.path("role").asText();
                String content = messageNode.path("content").asText();

                return new Message(role, content);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}

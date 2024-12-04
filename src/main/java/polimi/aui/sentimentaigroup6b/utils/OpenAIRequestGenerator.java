package polimi.aui.sentimentaigroup6b.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import polimi.aui.sentimentaigroup6b.models.RequestPayloadAI;

import java.util.Arrays;

@Component
public class OpenAIRequestGenerator {

    @Value("${ai.llm.url}")
    private String API_URL;
    @Value("${ai.llm.key}")
    private String API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    public String sendRequestToAzureOpenAI() {

        RequestPayloadAI requestPayloadAI = new RequestPayloadAI();
        requestPayloadAI.setMessages(Arrays.asList(
                new RequestPayloadAI.Message("system", "You are a helpful assistant."),
                new RequestPayloadAI.Message("user", "Does Azure OpenAI support customer managed keys?")
        ));

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

        return response.getBody();
    }

    public RequestPayloadAI.Message extractResponseAI(String jsonResponse){

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResponse, RequestPayloadAI.Message.class);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

}

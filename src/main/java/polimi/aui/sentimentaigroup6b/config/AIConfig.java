package polimi.aui.sentimentaigroup6b.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Value("${ai.llm.instructions}")
    private String aiInstructions;

    @Bean
    public String getAiInstructions() {
        return aiInstructions;
    }
}

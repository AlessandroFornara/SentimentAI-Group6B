package polimi.aui.sentimentaigroup6b.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import polimi.aui.sentimentaigroup6b.utils.ImageManager;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@Slf4j
public class GeneralConfig {

    @Value("${ai.llm.instructions}")
    private String aiInstructions;

    @Bean
    public ImageManager imageManager() {
        ImageManager imageManager = new ImageManager();
        imageManager.preloadImages();
        log.info("\u001B[32mCreating bean: Images preloaded\u001B[0m");
        return imageManager;
    }

    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("chatSessions");
    }

    @Bean
    public Cache chatSessionsCache() {
        log.info("\u001B[32mCreating bean: ChatSessionCache\u001B[0m");
        return cacheManager().getCache("chatSessions");
    }
    @Bean
    public String getAiInstructions() {
        log.info("\u001B[32mCreating bean: AiInstructions\u001B[0m");
        return aiInstructions;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        log.info("\u001B[32mCreating bean: RestTemplate\u001B[0m");
        return builder.build();
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

}

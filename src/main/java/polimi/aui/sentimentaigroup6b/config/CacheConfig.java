package polimi.aui.sentimentaigroup6b.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("chatSessions");
    }

    @Bean
    Cache chatSessionsCache() {return cacheManager().getCache("chatSessions");}
}

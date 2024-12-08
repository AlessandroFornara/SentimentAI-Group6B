package polimi.aui.sentimentaigroup6b.utils;

import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.models.ai.Message;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CachingComponent {

    private final Cache cache;

    public List<Message> getChatMessages(Long sessionId){
        List<Message> chatMessages = cache != null ? cache.get(sessionId, List.class) : null;
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
        }
        return chatMessages;
    }

    public void saveChat(Long sessionId, List<Message> chatMessages) {
        cache.put(sessionId, chatMessages);
    }

    public void deleteChat(String sessionId) {
        cache.evict(sessionId);
    }
}

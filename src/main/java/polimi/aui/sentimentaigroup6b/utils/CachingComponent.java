package polimi.aui.sentimentaigroup6b.utils;

import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.models.llm.Message;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CachingComponent {

    private final Cache cache;
    private final String aiInstructions;

    public List<Message> getChatMessages(Long sessionId){
        List<Message> chatMessages = cache != null ? cache.get(sessionId, List.class) : null;
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
            chatMessages.add(new Message("system", aiInstructions));
        }
        return chatMessages;
    }

    public void saveChat(Long sessionId, List<Message> chatMessages) {
        cache.put(sessionId, chatMessages);
    }

    public void deleteChat(Long sessionId) {
        cache.evict(sessionId);
    }
}

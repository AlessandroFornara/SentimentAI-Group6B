package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RequestPayloadAI {

    private List<Message> messages;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Message {
        private String role;
        private String content;
    }
}

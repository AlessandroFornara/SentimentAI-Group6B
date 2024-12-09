package polimi.aui.sentimentaigroup6b.models.llm;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestPayloadAI {

    private List<Message> messages;
}

package polimi.aui.sentimentaigroup6b.models.llm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestPayloadAI {

    private List<Message> messages;
}

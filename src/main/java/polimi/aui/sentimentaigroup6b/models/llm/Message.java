package polimi.aui.sentimentaigroup6b.models.llm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private String role;
    private String content;
}

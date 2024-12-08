package polimi.aui.sentimentaigroup6b.models.ai;

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

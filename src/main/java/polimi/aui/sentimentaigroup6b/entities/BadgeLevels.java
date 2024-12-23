package polimi.aui.sentimentaigroup6b.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class BadgeLevels {
    private int activityBasedBadge;
    private int levelBasedBadge;
    private int timeBasedBadge;
    private int topicBasedBadge;
}

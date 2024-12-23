package polimi.aui.sentimentaigroup6b.utils.gamification;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TopicBasedBadge extends Badge {

    private final int[] thresholds;

    public TopicBasedBadge() {
        super("Topic Badge", "You have discussed a new topic!");
        this.thresholds = new int[]{1, 5, 10, 20};
    }

    public int getLevel(int count) {
        for (int i = 0; i < thresholds.length; i++) {
            if (count <= thresholds[i]) {
                return i;
            }
        }
        return thresholds.length;
    }

}

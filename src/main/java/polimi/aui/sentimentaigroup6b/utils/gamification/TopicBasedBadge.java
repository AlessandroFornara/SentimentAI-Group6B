package polimi.aui.sentimentaigroup6b.utils.gamification;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TopicBasedBadge extends Badge {

    private final int[] thresholds;

    public TopicBasedBadge() {
        super("Topic Badge", "You have discussed a new topic!");
        this.thresholds = new int[]{1, 3, 5, 8};
    }

    @Override
    protected int[] getThresholds() {
        return thresholds;
    }
}

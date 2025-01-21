package polimi.aui.sentimentaigroup6b.utils.gamification;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TimeBasedBadge extends Badge {

    private final int[] thresholds;

    public TimeBasedBadge() {
        super("Persistence Badge", "You completed a new streak!");
        this.thresholds = new int[]{2, 5, 10, 20};
    }

    @Override
    protected int[] getThresholds() {
        return thresholds;
    }
}

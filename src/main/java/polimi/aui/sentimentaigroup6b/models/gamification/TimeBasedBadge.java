package polimi.aui.sentimentaigroup6b.models.gamification;

import lombok.Getter;
import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.entities.Badge;

@Component
@Getter
public class TimeBasedBadge extends Badge {

    private final int[] thresholds;

    public TimeBasedBadge() {
        super("Persistence Badge", "You completed a new streak!");
        this.thresholds = new int[]{1, 5, 10, 20};
    }

    public int getLevel(int streak) {
        for (int i = 0; i < thresholds.length; i++) {
            if (streak <= thresholds[i]) {
                return i;
            }
        }
        return thresholds.length;
    }

}

package polimi.aui.sentimentaigroup6b.utils.gamification;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LevelBasedBadge extends Badge {

    private final int[] thresholds; // Levels thresholds (e.g., [1, 10, 50])

    public LevelBasedBadge() {
        super("Level Badge", "You have reached a new level!");
        this.thresholds = new int[]{1, 5, 10, 20};
    }

    public int getLevel(int level) {
        for (int i = 0; i < thresholds.length; i++) {
            if (level <= thresholds[i]) {
                return i;
            }
        }
        return thresholds.length;
    }


}

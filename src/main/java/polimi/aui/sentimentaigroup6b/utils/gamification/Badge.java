package polimi.aui.sentimentaigroup6b.utils.gamification;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public abstract class Badge {
    private String name;
    private String description;

    protected abstract int[] getThresholds();

    public int getLevel(int level) {
        int badgeLevel = 0;
        int[] thresholds = getThresholds();
        for (int i = 0; i < thresholds.length; i++) {
            if (level >= thresholds[i]) {
                badgeLevel = i + 1;
            }
        }
        return badgeLevel;
    }
}

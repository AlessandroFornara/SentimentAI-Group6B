package polimi.aui.sentimentaigroup6b.utils.gamification;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ActivityBasedBadge extends Badge {

    private final int[] thresholds;

    public ActivityBasedBadge() {
        super("Activities Badge", "You have unlocked a new activity!");
        this.thresholds = new int[]{1, 5, 10, 20};
    }

    public int getLevel(int activities) {
        for (int i = 0; i < thresholds.length; i++) {
            if (activities <= thresholds[i]) {
                return i;
            }
        }
        return thresholds.length;
    }

}

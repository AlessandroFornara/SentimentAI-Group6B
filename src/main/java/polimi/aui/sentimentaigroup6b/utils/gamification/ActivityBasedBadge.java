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

    @Override
    protected int[] getThresholds() {
        return thresholds;
    }
}

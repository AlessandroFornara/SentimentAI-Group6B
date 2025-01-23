package polimi.aui.sentimentaigroup6b.utils.gamification;

import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.models.Activity;
import polimi.aui.sentimentaigroup6b.models.ActivityResponse;

@Component
public class PointsManager {

    public int calculateXPForSession(ActivityResponse activityResponse, int numberOfAudios) {
        int baseXP = 50; // Base XP for attending a session

        // Add duration-based XP
        int durationXP = numberOfAudios * 10; // e.g., 10 XP per minute

        // Convert the activity category from String to Activity enum
        Activity activityCategory = Activity.valueOf(activityResponse.getActivityCategory().toUpperCase().replace(" ", "_").replace("-", "_"));

        // Add activity-based or bonus XP
        int activityXP = switch (activityCategory) {
            case PERSONALIZED_QUOTE -> 40;
            case SELF_CARE_SUGGESTION -> 30;
            case CREATIVE_MINI_ACTIVITY -> 40;
            case SHORT_GUIDED_MEDITATION -> 50;
            case CHALLENGE -> 60;
            case RECOMMENDED_MUSIC -> 30;
        };

        return baseXP + durationXP + activityXP;
    }

    public int calculateUserLevel(int totalXP) {
        int level = 0;
        int xpForNextLevel = 80;

        while (totalXP >= xpForNextLevel) {
            level++;
            totalXP -= xpForNextLevel;
            xpForNextLevel += 40; // Increase XP requirement for the next level
        }

        return level;
    }
}

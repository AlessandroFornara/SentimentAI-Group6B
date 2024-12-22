package polimi.aui.sentimentaigroup6b.models.gamification;

import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.repositories.SessionRepo;
import polimi.aui.sentimentaigroup6b.models.Activity;

import static polimi.aui.sentimentaigroup6b.models.Activity.*;

@Component
public class PointsManager {

    public int calculateXPForSession(Session session) {
        int baseXP = 50; // Base XP for attending a session

        // Add duration-based XP
        int numberOfAudios = session.getAudios().size();
        int durationXP = numberOfAudios * 10; // e.g., 10 XP per minute

        // Convert the activity category from String to Activity enum
        Activity activityCategory = Activity.valueOf(session.getActivityCategory().toUpperCase().replace(" ", "_"));

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


}

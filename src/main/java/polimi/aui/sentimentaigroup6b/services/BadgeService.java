package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import polimi.aui.sentimentaigroup6b.entities.Badge;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.models.gamification.ActivityBasedBadge;
import polimi.aui.sentimentaigroup6b.models.gamification.LevelBasedBadge;
import polimi.aui.sentimentaigroup6b.models.gamification.TimeBasedBadge;
import polimi.aui.sentimentaigroup6b.models.gamification.TopicBasedBadge;
import polimi.aui.sentimentaigroup6b.repositories.BadgeRepo;
import polimi.aui.sentimentaigroup6b.repositories.SessionRepo;
import polimi.aui.sentimentaigroup6b.repositories.UserRepo;

import java.util.*;

@Service
@AllArgsConstructor
public class BadgeService {

    private final SessionRepo sessionRepo;
    private final BadgeRepo badgeRepo;
    private final UserRepo userRepo;

    private final ActivityBasedBadge activityBasedBadge;
    private final LevelBasedBadge levelBasedBadge;
    private final TimeBasedBadge timeBasedBadge;
    private final TopicBasedBadge topicBasedBadge;


    public Map<Badge, Integer> assignBadges(Long userId) {

        Map<Badge, Integer> newBadges = new HashMap<>();

        assignLevelBadge(userId, newBadges);
        assignActivityBadge(userId, newBadges);
        assignTimeBadge(userId, newBadges);
        assignTopicBadge(userId, newBadges);

        return newBadges;
    }

    public void assignLevelBadge(Long userId, Map<Badge, Integer> badges) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        int actualBadgeLevel = levelBasedBadge.getLevel(user.getLevel());

        Badge badge = badgeRepo.findByName("Level Badge")
                .orElseThrow(() -> new IllegalArgumentException("Badge not found"));

        int oldBadgeLevel = user.getBadges().get(badge);

        if (actualBadgeLevel > oldBadgeLevel) {
            user.getBadges().put(badge, actualBadgeLevel);
            userRepo.save(user);

            badges.put(badge, actualBadgeLevel);
        }
    }

    public void assignActivityBadge(Long userId, Map<Badge, Integer> badges) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        int actualBadgeLevel = activityBasedBadge.getLevel((int)
                sessionRepo.findAllByUserId(user).stream()
                .map(s -> ((Session) s).getActivityText())
                .distinct()
                .count()
                );

        Badge badge = badgeRepo.findByName("Activity Badge")
                .orElseThrow(() -> new IllegalArgumentException("Badge not found"));

        int oldBadgeLevel = user.getBadges().get(badge);

        if (actualBadgeLevel > oldBadgeLevel) {
            user.getBadges().put(badge, actualBadgeLevel);
            userRepo.save(user);

            badges.put(badge, actualBadgeLevel);
        }

    }

    public void assignTimeBadge(Long userId, Map<Badge, Integer> badges) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch all sessions and sort them by date descending
        List<Object> sessions = sessionRepo.findAllByUserId(user).stream()
                .sorted(( s1, s2) -> ((Session) s2).getDate().compareTo(((Session) s1).getDate())) // Sort by date descending
                .toList();

        // Calculate consecutive days streak
        int consecutiveDays = 0;
        if (!sessions.isEmpty()) {
            Date lastDate = ((Session) sessions.getFirst()).getDate();
            consecutiveDays = 1;

            for (int i = 1; i < sessions.size(); i++) {
                Date currentDate = ((Session) sessions.get(i)).getDate();

                // Check if the current session is exactly one day before the previous
                if (isOneDayBefore(currentDate, lastDate)) {
                    consecutiveDays++;
                    lastDate = currentDate; // Update last date to current
                } else {
                    break; // Gap in the streak, stop counting
                }
            }
        }

        int actualBadgeLevel = timeBasedBadge.getLevel(consecutiveDays);

        Badge badge = badgeRepo.findByName("Time Badge")
                .orElseThrow(() -> new IllegalArgumentException("Badge not found"));

        int oldBadgeLevel = user.getBadges().get(badge);

        if (actualBadgeLevel > oldBadgeLevel) {
            user.getBadges().put(badge, actualBadgeLevel);
            userRepo.save(user);

            badges.put(badge, actualBadgeLevel);
        }

    }

    private boolean isOneDayBefore(Date currentDate, Date lastDate) {
        Calendar calendar = Calendar.getInstance();

        // Set calendar to lastDate and subtract one day
        calendar.setTime(lastDate);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date expectedDate = calendar.getTime();

        // Compare expectedDate with currentDate
        return currentDate.equals(expectedDate);
    }

    public void assignTopicBadge(Long userId, Map<Badge, Integer> badges) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        int actualBadgeLevel = topicBasedBadge.getLevel((int)
                sessionRepo.findAllByUserId(user).stream()
                .map(s -> ((Session) s).getTopic())
                .distinct()
                .count()
                );

        Badge badge = badgeRepo.findByName("Topic Badge")
                .orElseThrow(() -> new IllegalArgumentException("Badge not found"));

        int oldBadgeLevel = user.getBadges().get(badge);

        if (actualBadgeLevel > oldBadgeLevel) {
            user.getBadges().put(badge, actualBadgeLevel);
            userRepo.save(user);

            badges.put(badge, actualBadgeLevel);
        }

    }
}

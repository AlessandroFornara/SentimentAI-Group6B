package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import polimi.aui.sentimentaigroup6b.entities.BadgeType;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.utils.gamification.ActivityBasedBadge;
import polimi.aui.sentimentaigroup6b.utils.gamification.LevelBasedBadge;
import polimi.aui.sentimentaigroup6b.utils.gamification.TimeBasedBadge;
import polimi.aui.sentimentaigroup6b.utils.gamification.TopicBasedBadge;
import polimi.aui.sentimentaigroup6b.repositories.SessionRepo;
import polimi.aui.sentimentaigroup6b.repositories.UserRepo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class BadgeService {

    private final SessionRepo sessionRepo;
    private final UserRepo userRepo;

    private final ActivityBasedBadge activityBasedBadge;
    private final LevelBasedBadge levelBasedBadge;
    private final TimeBasedBadge timeBasedBadge;
    private final TopicBasedBadge topicBasedBadge;


    public Map<BadgeType, Integer> assignBadges(User user) {

        Map<BadgeType, Integer> newBadges = new HashMap<>();

        assignLevelBadge(user, newBadges);
        assignActivityBadge(user, newBadges);
        assignTimeBadge(user, newBadges);
        assignTopicBadge(user, newBadges);

        return newBadges;
    }

    public void assignLevelBadge(User user, Map<BadgeType, Integer> badges) {

        int actualBadgeLevel = levelBasedBadge.getLevel(user.getLevel());
        int oldBadgeLevel = user.getBadges().getLevelBasedBadge();

        if (actualBadgeLevel > oldBadgeLevel) {
            user.getBadges().setLevelBasedBadge(actualBadgeLevel);
            badges.put(BadgeType.LEVEL_BASED, actualBadgeLevel);
        }
    }

    public void assignActivityBadge(User user, Map<BadgeType, Integer> badges) {

        int actualBadgeLevel = activityBasedBadge.getLevel((int)
                sessionRepo.findAllByUserId(user).stream()
                .map(s -> ((Session) s).getActivityText())
                .distinct()
                .count()
                );

        int oldBadgeLevel = user.getBadges().getActivityBasedBadge();

        if (actualBadgeLevel > oldBadgeLevel) {
            user.getBadges().setActivityBasedBadge(actualBadgeLevel);
            badges.put(BadgeType.ACTIVITY_BASED, actualBadgeLevel);
        }
    }

    public void assignTimeBadge(User user, Map<BadgeType, Integer> badges) {

        // Fetch all sessions and sort them by date descending
        List<Session> sessions = sessionRepo.findAllByUserId(user).stream()
                .map(s -> (Session) s)
                .sorted(( s1, s2) -> (s2).getDate().compareTo((s1).getDate())) // Sort by date descending
                .toList();

        // Calculate consecutive days streak
        int consecutiveDays = 0;
        if (!sessions.isEmpty()) {
            Date lastDate = (sessions.getFirst()).getDate();
            consecutiveDays = 1;

            for (int i = 1; i < sessions.size(); i++) {
                Date currentDate = (sessions.get(i)).getDate();
                System.out.println("Current date: " + currentDate);
                System.out.println("Last date: " + lastDate);
                // Check if the current session is exactly one day before the previous
                if (isOneDayBefore(currentDate, lastDate)) {
                    System.out.println("One day before");
                    consecutiveDays++;
                    lastDate = currentDate; // Update last date to current
                } else {
                    break; // Gap in the streak, stop counting
                }
            }
        }

        int actualBadgeLevel = timeBasedBadge.getLevel(consecutiveDays);

        int oldBadgeLevel = user.getBadges().getTimeBasedBadge();

        if (actualBadgeLevel > oldBadgeLevel) {
            user.getBadges().setTimeBasedBadge(actualBadgeLevel);

            badges.put(BadgeType.TIME_BASED, actualBadgeLevel);
        }

    }

    private boolean isOneDayBefore(Date currentDate, Date lastDate) {
        if (currentDate == null || lastDate == null) {
            return false;
        }

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);

        Calendar lastCalendar = Calendar.getInstance();
        lastCalendar.setTime(lastDate);
        lastCalendar.set(Calendar.HOUR_OF_DAY, 0);
        lastCalendar.set(Calendar.MINUTE, 0);
        lastCalendar.set(Calendar.SECOND, 0);
        lastCalendar.set(Calendar.MILLISECOND, 0);

        currentCalendar.add(Calendar.DAY_OF_YEAR, 1);
        return currentCalendar.getTime().equals(lastCalendar.getTime());
    }


    public void assignTopicBadge(User user, Map<BadgeType, Integer> badges) {

        int actualBadgeLevel = topicBasedBadge.getLevel((int)
                sessionRepo.findAllByUserId(user).stream()
                .map(s -> ((Session) s).getTopic())
                .distinct()
                .count()
                );

        int oldBadgeLevel = user.getBadges().getTopicBasedBadge();

        if (actualBadgeLevel > oldBadgeLevel) {
            user.getBadges().setTopicBasedBadge(actualBadgeLevel);

            badges.put(BadgeType.TOPIC_BASED, actualBadgeLevel);
        }

    }
}

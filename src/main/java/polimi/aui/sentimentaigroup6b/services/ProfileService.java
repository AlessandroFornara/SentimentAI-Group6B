package polimi.aui.sentimentaigroup6b.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.models.HistoryResponse;
import polimi.aui.sentimentaigroup6b.models.ProfileResponse;
import polimi.aui.sentimentaigroup6b.repositories.SessionRepo;
import polimi.aui.sentimentaigroup6b.repositories.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileService {

    private final BadgeService badgeService;
    private final UserRepo userRepo;
    private final SessionRepo sessionRepo;

    public ProfileResponse getProfile(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return new ProfileResponse(user.getName(), user.getSurname(), user.getEmail(), user.getCompany(), user.getLevel(), user.getPoints(), user.getRole(), user.getBadges());
    }

    public HistoryResponse getHistory(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        List<Session> sessions = sessionRepo.findAllByUserId(user).stream()
                .map(s -> (Session) s)
                .toList();

        List<HistoryResponse.SessionData> sessionDataList = sessions.stream()
                .map(session -> new HistoryResponse.SessionData(
                        session.getDate(),
                        session.getDominantEmotion(),
                        session.getActivityCategory(),
                        session.getActivityText()))
                .toList();

        return new HistoryResponse(sessionDataList);
    }
}

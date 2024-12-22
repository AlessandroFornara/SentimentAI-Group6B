package polimi.aui.sentimentaigroup6b.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.entities.User;

import java.util.Collection;

public interface SessionRepo extends JpaRepository<Session, Long> {
    Collection<Object> findAllByUserId(User user);
    Collection<Object> findActivitiesTextByUserId(User user);
}

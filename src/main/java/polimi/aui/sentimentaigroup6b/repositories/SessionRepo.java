package polimi.aui.sentimentaigroup6b.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import polimi.aui.sentimentaigroup6b.entities.Session;

public interface SessionRepo extends JpaRepository<Session, Long> {
}

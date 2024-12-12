package polimi.aui.sentimentaigroup6b.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import polimi.aui.sentimentaigroup6b.entities.Audio;
import polimi.aui.sentimentaigroup6b.entities.Session;

import java.util.List;

public interface AudioRepo extends JpaRepository<Audio, Long> {
    List<Audio> findAllBySession(Session session);
}

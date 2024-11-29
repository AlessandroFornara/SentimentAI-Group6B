package polimi.aui.sentimentaigroup6b.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import polimi.aui.sentimentaigroup6b.entities.Audio;

public interface AudioRepo extends JpaRepository<Audio, Long> {
}

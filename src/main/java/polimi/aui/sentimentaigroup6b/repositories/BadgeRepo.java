package polimi.aui.sentimentaigroup6b.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import polimi.aui.sentimentaigroup6b.entities.Badge;

import java.util.Optional;

public interface BadgeRepo extends JpaRepository<Badge, Long> {

    Optional<Badge> findByName(String name);
}

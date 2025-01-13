package polimi.aui.sentimentaigroup6b.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import polimi.aui.sentimentaigroup6b.entities.Audio;
import polimi.aui.sentimentaigroup6b.entities.Session;
import polimi.aui.sentimentaigroup6b.models.AudioEmotionsDTO;

import java.util.List;

public interface AudioRepo extends JpaRepository<Audio, Long> {
    @Query("SELECT new polimi.aui.sentimentaigroup6b.models.AudioEmotionsDTO(a.anger, a.disgust, a.fear, a.joy, a.sadness, a.surprise, a.neutrality) " +
            "FROM Audio a WHERE a.session = :session")
    List<AudioEmotionsDTO> findEmotionsBySession(@Param("session") Session session);
    @Query("SELECT COUNT(a) FROM Audio a WHERE a.session = :session")
    int countAudiosBySession(@Param("session") Session session);
}

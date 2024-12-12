package polimi.aui.sentimentaigroup6b.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "audio")
@Data
@NoArgsConstructor
public class Audio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ElementCollection
    private List<Double> detectedEmotions;

    @Lob
    private byte[] audioData;

    private String dominantEmotion;

    public Audio(Session session, byte[] audioData) {
        this.session = session;
        this.audioData = audioData;
    }
}

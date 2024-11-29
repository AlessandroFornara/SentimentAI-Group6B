package polimi.aui.sentimentaigroup6b.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "audio")
@Getter
@Setter
public class Audio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    private Date date;

    @ElementCollection
    private List<Float> detectedEmotions;

    @Lob
    private byte[] audioData;

    private String dominantEmotion;
}

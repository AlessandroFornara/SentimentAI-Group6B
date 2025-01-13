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

    private double anger;
    private double disgust;
    private double fear;
    private double joy;
    private double sadness;
    private double surprise;
    private double neutrality;

    @Lob
    private byte[] audioData;

    public Audio(Session session, byte[] audioData) {
        this.session = session;
        this.audioData = audioData;
    }

    public void setDetectedEmotions(List<Double> emotions){
        this.anger = emotions.get(0);
        this.disgust = emotions.get(1);
        this.fear = emotions.get(2);
        this.joy = emotions.get(3);
        this.sadness = emotions.get(4);
        this.surprise = emotions.get(5);
        this.neutrality = emotions.get(6);
    }
}

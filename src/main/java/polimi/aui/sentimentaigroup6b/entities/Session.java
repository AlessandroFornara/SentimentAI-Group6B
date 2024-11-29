package polimi.aui.sentimentaigroup6b.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "session")
@Data
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Worker userId;

    private Date date;

    private String topic;

    //private Image image;

    private String dominantEmotion;

    private String activityCategory;

    private String activityText;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Audio> audios;

    public Session(Worker userId, Date date) {
        this.userId = userId;
        this.date = date;
    }
}

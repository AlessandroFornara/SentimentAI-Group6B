package polimi.aui.sentimentaigroup6b.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "worker")
@Setter
@Getter
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private String company;

    private int level;

    private int points;

    @ElementCollection
    private List<Integer> badge;

    @ElementCollection
    private List<Integer> badgeStatus;

}

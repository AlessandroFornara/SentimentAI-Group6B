package polimi.aui.sentimentaigroup6b.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "badge")
@Data
@NoArgsConstructor

public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "badges")
    private List<Worker> workers;

    public Badge(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

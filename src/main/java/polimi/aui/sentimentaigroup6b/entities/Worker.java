package polimi.aui.sentimentaigroup6b.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("worker")
@Data
@NoArgsConstructor
public class Worker extends User {

    private int level;

    private int points;

    @ManyToMany
    @JoinTable(
            name = "worker_badge",
            joinColumns = @JoinColumn(name = "worker_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    private List<Badge> badges;

    @ElementCollection
    private List<Integer> badgeStatus;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;

    public Worker(String name, String surname, String email, String password, String company, int level, int points) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.company = company;
        this.level = level;
        this.points = points;
    }
}

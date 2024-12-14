package polimi.aui.sentimentaigroup6b.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(
        name = "\"user\"",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
@Data
@NoArgsConstructor
public class User {

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

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    @ManyToMany
    @JoinTable(
            name = "user_badge",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    private List<Badge> badges;

    @ElementCollection
    private List<Integer> badgeStatus;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;

    public User(String name, String surname, String email, String password, String company, int level, int points, UserRoles role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.company = company;
        this.level = level;
        this.points = points;
        this.role = role;
    }

    public User(String email, String password, UserRoles role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}

package polimi.aui.sentimentaigroup6b.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("hr")
@Data
@NoArgsConstructor
public class HR extends User {

    public HR(String name, String surname, String email, String password, String company) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.company = company;
    }
}

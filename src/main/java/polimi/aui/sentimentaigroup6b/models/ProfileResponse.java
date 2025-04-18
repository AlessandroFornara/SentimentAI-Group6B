package polimi.aui.sentimentaigroup6b.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import polimi.aui.sentimentaigroup6b.entities.BadgeLevels;
import polimi.aui.sentimentaigroup6b.entities.UserRoles;

@AllArgsConstructor
@Getter
public class ProfileResponse {

    private String name;

    private String surname;

    private String email;

    private String company;

    private int level;

    private int points;

    private UserRoles role;

    private BadgeLevels badges;

}

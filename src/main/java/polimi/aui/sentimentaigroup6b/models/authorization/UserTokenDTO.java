package polimi.aui.sentimentaigroup6b.models.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import polimi.aui.sentimentaigroup6b.entities.UserRoles;

@AllArgsConstructor
@Getter
public class UserTokenDTO {

    private String email;
    private UserRoles role;
    private String token;

}

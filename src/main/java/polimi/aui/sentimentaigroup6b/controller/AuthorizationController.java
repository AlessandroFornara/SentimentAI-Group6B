package polimi.aui.sentimentaigroup6b.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.entities.UserRoles;
import polimi.aui.sentimentaigroup6b.models.authorization.LoginDTO;
import polimi.aui.sentimentaigroup6b.models.authorization.RegisterDTO;
import polimi.aui.sentimentaigroup6b.models.ServerResponse;
import polimi.aui.sentimentaigroup6b.models.authorization.UserTokenDTO;
import polimi.aui.sentimentaigroup6b.repositories.UserRepo;
import polimi.aui.sentimentaigroup6b.security.JwtUtil;

import java.util.Collection;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            String email = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String grantedAuthority = null;
            for (GrantedAuthority authority : authorities) {
                grantedAuthority = authority.getAuthority();
            }

            User user = new User(email,"", grantedAuthority.equals("ROLE_WORKER") ? UserRoles.WORKER : UserRoles.HR);
            String token = jwtUtil.createToken(user);
            UserTokenDTO userTokenDTO = new UserTokenDTO(email, grantedAuthority.equals("ROLE_WORKER") ? UserRoles.WORKER : UserRoles.HR, token);

            System.out.println("User: " + email + " has logged in");
            return ResponseEntity.ok(userTokenDTO);

        }catch (BadCredentialsException e){
            return ResponseEntity.unprocessableEntity().body(ServerResponse.INVALID_CREDENTIALS.getMessage());
        }catch (Exception e){
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PostMapping ("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO){
        UserRoles role = registerDTO.getRole();
        int level = 0;

        User u = new User(
                registerDTO.getName(),
                registerDTO.getSurname(),
                registerDTO.getEmail(),
                registerDTO.getPassword(),
                registerDTO.getCompany(),
                level,
                level,
                registerDTO.getRole());
        try{
            userRepo.save(u);
        }catch (Exception e){
            return ResponseEntity.unprocessableEntity().body(ServerResponse.USER_REGISTRATION_ERROR.getMessage());
        }

        System.out.println((registerDTO.getEmail() + " has registered to CKB as " + (registerDTO.getRole().equals(UserRoles.WORKER) ? "a Worker" : "an HR manager")));
        return ResponseEntity.ok(ServerResponse.USER_REGISTRATION_SUCCESS.getMessage());
    }
}

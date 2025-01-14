package polimi.aui.sentimentaigroup6b.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.entities.User;
import polimi.aui.sentimentaigroup6b.entities.UserRoles;
import polimi.aui.sentimentaigroup6b.repositories.UserRepo;

import java.util.Collections;
import java.util.List;

/**
 * Custom implementation of UserDetailsService.
 * This class provides a custom way to retrieve user details for Spring Security authentication processes.
 */
@Component("CustomUserDetailsService")
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email).orElse(null);

        if (user != null) {
            UserRoles userRole = user.getRole();

            if (userRole == null) {
                throw new UsernameNotFoundException("User does not have a role assigned: " + email);
            }

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole.name()));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail() + "-" + user.getName())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
    }
}
package hu.mik.pte.prog5.survey.auth;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUser {

    private final AppUserRepository users;

    public CurrentUser(AppUserRepository users){
        this.users = users;
    }

    @Transactional
    public  AppUser resolve(Jwt jwt){
        String sub = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        return users.findByKeycloakId(sub).map(user -> {
            if(username != null && !username.equals(user.getUsername())){
                user.setUsername(username);
            }
            return user;
        }).orElseGet(() -> users.save(new AppUser(sub, username != null ? username : sub)));
    }

}

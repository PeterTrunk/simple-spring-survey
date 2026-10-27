package hu.mik.pte.prog5.survey.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeController {
    private final CurrentUser currentUser;

    public MeController(CurrentUser currentUser){
        this.currentUser = currentUser;
    }

    @GetMapping("/api/me")
    Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
        AppUser user = currentUser.resolve(jwt);
        return Map.of("id", user.getId(), "username", user.getUsername());
    }
}

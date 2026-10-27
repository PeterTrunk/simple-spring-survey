package hu.mik.pte.prog5.survey.auth;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId;

    @Column(nullable = false)
    private String username;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    protected AppUser() {}

    public Long getId() {
        return id;
    }

    public AppUser(String keycloakId, String username){
        this.keycloakId = keycloakId;
        this.username = username;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public String getUsername() {
        return username;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

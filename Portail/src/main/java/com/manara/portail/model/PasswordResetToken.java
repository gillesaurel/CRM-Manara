package com.manara.portail.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @Column(nullable = false)
    private boolean utilise = false;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, Utilisateur utilisateur) {
        this.token = token;
        this.utilisateur = utilisateur;
        this.expiration = LocalDateTime.now().plusMinutes(30);
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expiration) || utilise;
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public LocalDateTime getExpiration() { return expiration; }
    public boolean isUtilise() { return utilise; }
    public void setUtilise(boolean utilise) { this.utilise = utilise; }
}

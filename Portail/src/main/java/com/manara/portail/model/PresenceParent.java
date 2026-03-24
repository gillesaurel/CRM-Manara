package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "presences_parent")
public class PresenceParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Le statut de présence est requis")
    @Column(nullable = false)
    private boolean present;

    @NotNull(message = "La date de saisie est requise")
    @Column(name = "date_saisie", nullable = false)
    private LocalDateTime dateSaisie;

    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    @Column(length = 500)
    private String commentaire;

    @NotNull(message = "L'enfant est requis")
    @ManyToOne
    @JoinColumn(name = "enfant_id", nullable = false)
    private EnfantParent enfant;

    @NotNull(message = "La session est requise")
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private SessionParent session;

    // Constructeurs
    public PresenceParent() {}

    public PresenceParent(boolean present, EnfantParent enfant, SessionParent session) {
        this.present = present;
        this.enfant = enfant;
        this.session = session;
        this.dateSaisie = LocalDateTime.now();
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }

    public LocalDateTime getDateSaisie() { return dateSaisie; }
    public void setDateSaisie(LocalDateTime dateSaisie) { this.dateSaisie = dateSaisie; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public EnfantParent getEnfant() { return enfant; }
    public void setEnfant(EnfantParent enfant) { this.enfant = enfant; }

    public SessionParent getSession() { return session; }
    public void setSession(SessionParent session) { this.session = session; }
}
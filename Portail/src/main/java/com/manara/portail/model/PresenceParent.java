package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Presence {
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
    private Enfant enfant;

    @NotNull(message = "La session est requise")
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // Constructeurs
    public Presence() {}

    public Presence(boolean present, Enfant enfant, Session session) {
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

    public Enfant getEnfant() { return enfant; }
    public void setEnfant(Enfant enfant) { this.enfant = enfant; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
}
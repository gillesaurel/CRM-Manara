package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions_parent")
public class InscriptionParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La date d'inscription est requise")
    @Column(name = "date_inscription", nullable = false)
    private LocalDateTime dateInscription;

    @NotBlank(message = "Le statut est requis")
    @Column(length = 50, nullable = false)
    private String statut;

    @Min(value = 0, message = "La position d'attente doit être positive")
    @Column(name = "position_attente")
    private Integer positionAttente;

    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    @Column(name = "commentaire_parent", length = 500)
    private String commentaireParent;

    @NotNull(message = "L'enfant est requis")
    @ManyToOne
    @JoinColumn(name = "enfant_id", nullable = false)
    private EnfantParent enfant;

    @NotNull(message = "La session est requise")
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private SessionParent session;

    // Constructeurs
    public InscriptionParent() {}

    public InscriptionParent(EnfantParent enfant, SessionParent session) {
        this.enfant = enfant;
        this.session = session;
        this.dateInscription = LocalDateTime.now();
        this.statut = "EN_ATTENTE";
        this.positionAttente = 0;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Integer getPositionAttente() { return positionAttente; }
    public void setPositionAttente(Integer positionAttente) { this.positionAttente = positionAttente; }

    public String getCommentaireParent() { return commentaireParent; }
    public void setCommentaireParent(String commentaireParent) { this.commentaireParent = commentaireParent; }

    public EnfantParent getEnfant() { return enfant; }
    public void setEnfant(EnfantParent enfant) { this.enfant = enfant; }

    public SessionParent getSession() { return session; }
    public void setSession(SessionParent session) { this.session = session; }
}
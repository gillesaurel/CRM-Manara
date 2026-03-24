package com.manara.portail.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class InscriptionDTO {

    private Integer id;

    @NotNull(message = "L'ID de l'enfant est requis")
    private Integer enfantId;

    @NotNull(message = "L'ID de la session est requis")
    private Integer sessionId;

    private LocalDate dateInscription;
    private String statut;
    private Integer positionAttente;
    private String commentaireParent;

    // Getters et setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEnfantId() { return enfantId; }
    public void setEnfantId(Integer enfantId) { this.enfantId = enfantId; }

    public Integer getSessionId() { return sessionId; }
    public void setSessionId(Integer sessionId) { this.sessionId = sessionId; }

    public LocalDate getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDate dateInscription) { this.dateInscription = dateInscription; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Integer getPositionAttente() { return positionAttente; }
    public void setPositionAttente(Integer positionAttente) { this.positionAttente = positionAttente; }

    public String getCommentaireParent() { return commentaireParent; }
    public void setCommentaireParent(String commentaireParent) { this.commentaireParent = commentaireParent; }
}
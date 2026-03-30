package com.manara.portail.dto;

import jakarta.validation.constraints.NotNull;

public class InscriptionEnfantDTO {

    private Long id;

    @NotNull(message = "L'ID de l'enfant est requis")
    private Integer enfantId;

    @NotNull(message = "L'ID de la session est requis")
    private Integer sessionId;

    private String commentaireParent;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getEnfantId() { return enfantId; }
    public void setEnfantId(Integer enfantId) { this.enfantId = enfantId; }

    public Integer getSessionId() { return sessionId; }
    public void setSessionId(Integer sessionId) { this.sessionId = sessionId; }

    public String getCommentaireParent() { return commentaireParent; }
    public void setCommentaireParent(String commentaireParent) { this.commentaireParent = commentaireParent; }
}
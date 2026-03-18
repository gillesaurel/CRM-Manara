package com.manara.portail.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class IncidentDTO {

    private Integer id;

    @NotBlank(message = "Le type d'incident est requis")
    @Size(max = 100, message = "Le type ne peut pas dépasser 100 caractères")
    private String type;

    @NotBlank(message = "La description est requise")
    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;

    @NotNull(message = "La gravité est requise")
    @Min(value = 1, message = "La gravité doit être au moins 1")
    @Max(value = 5, message = "La gravité ne peut pas dépasser 5")
    private Integer gravite;

    private LocalDateTime dateHeure;

    private String statut;

    @Size(max = 1000, message = "Les actions prises ne peuvent pas dépasser 1000 caractères")
    private String actionsPrises;

    @NotNull(message = "L'ID de l'enfant est requis")
    private Integer enfantId;

    private String enfantNom;
    private String enfantPrenom;

    private Integer animateurId;
    private String animateurNom;
    private String animateurPrenom;

    // Constructeurs
    public IncidentDTO() {}

    public IncidentDTO(Integer id, String type, String description, Integer gravite,
                       LocalDateTime dateHeure, String statut, Integer enfantId) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.gravite = gravite;
        this.dateHeure = dateHeure;
        this.statut = statut;
        this.enfantId = enfantId;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGravite() {
        return gravite;
    }

    public void setGravite(Integer gravite) {
        this.gravite = gravite;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getActionsPrises() {
        return actionsPrises;
    }

    public void setActionsPrises(String actionsPrises) {
        this.actionsPrises = actionsPrises;
    }

    public Integer getEnfantId() {
        return enfantId;
    }

    public void setEnfantId(Integer enfantId) {
        this.enfantId = enfantId;
    }

    public String getEnfantNom() {
        return enfantNom;
    }

    public void setEnfantNom(String enfantNom) {
        this.enfantNom = enfantNom;
    }

    public String getEnfantPrenom() {
        return enfantPrenom;
    }

    public void setEnfantPrenom(String enfantPrenom) {
        this.enfantPrenom = enfantPrenom;
    }

    public Integer getAnimateurId() {
        return animateurId;
    }

    public void setAnimateurId(Integer animateurId) {
        this.animateurId = animateurId;
    }

    public String getAnimateurNom() {
        return animateurNom;
    }

    public void setAnimateurNom(String animateurNom) {
        this.animateurNom = animateurNom;
    }

    public String getAnimateurPrenom() {
        return animateurPrenom;
    }

    public void setAnimateurPrenom(String animateurPrenom) {
        this.animateurPrenom = animateurPrenom;
    }

    // Méthodes utilitaires
    public String getGraviteLabel() {
        switch (gravite) {
            case 1: return "Mineur";
            case 2: return "Faible";
            case 3: return "Modéré";
            case 4: return "Grave";
            case 5: return "Critique";
            default: return "Inconnu";
        }
    }

    public String getGraviteColor() {
        switch (gravite) {
            case 1: return "#28a745"; // Vert
            case 2: return "#ffc107"; // Jaune
            case 3: return "#fd7e14"; // Orange
            case 4: return "#dc3545"; // Rouge
            case 5: return "#721c24"; // Rouge foncé
            default: return "#6c757d"; // Gris
        }
    }

    public String getStatutLabel() {
        if (statut == null) return "Non défini";

        switch (statut) {
            case "SIGNALÉ": return "Signalé";
            case "EN_COURS": return "En cours de traitement";
            case "EN_ATTENTE": return "En attente";
            case "RESOLU": return "Résolu";
            case "CLOTURE": return "Clôturé";
            default: return statut;
        }
    }

    public String getStatutColor() {
        if (statut == null) return "#6c757d";

        switch (statut) {
            case "SIGNALÉ": return "#dc3545"; // Rouge
            case "EN_COURS": return "#fd7e14"; // Orange
            case "EN_ATTENTE": return "#ffc107"; // Jaune
            case "RESOLU": return "#28a745"; // Vert
            case "CLOTURE": return "#6c757d"; // Gris
            default: return "#6c757d";
        }
    }

    @Override
    public String toString() {
        return "IncidentDTO{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", gravite=" + gravite +
                ", statut='" + statut + '\'' +
                ", enfantId=" + enfantId +
                ", animateurId=" + animateurId +
                '}';
    }
}
package com.manara.portail.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommunicationDTO {

    private Integer id;

    @NotBlank(message = "Le sujet est requis")
    @Size(max = 200, message = "Le sujet ne peut pas dépasser 200 caractères")
    private String sujet;

    @NotBlank(message = "Le message est requis")
    @Size(max = 2000, message = "Le message ne peut pas dépasser 2000 caractères")
    private String message;

    private LocalDateTime dateEnvoi;

    @NotBlank(message = "Le canal est requis")
    @Pattern(regexp = "EMAIL|SMS|NOTIFICATION|PUSH",
            message = "Le canal doit être EMAIL, SMS, NOTIFICATION ou PUSH")
    private String canal;

    private boolean lu = false;

    // Destinataires
    private Integer parentId;
    private String parentNom;
    private String parentPrenom;

    private Integer animateurId;
    private String animateurNom;
    private String animateurPrenom;

    // Pour envoi à tous
    private boolean envoyerATous = false;

    // Statistiques (pour l'affichage)
    private Integer nbDestinataires;
    private Integer nbLus;

    // Constructeurs
    public CommunicationDTO() {}

    public CommunicationDTO(Integer id, String sujet, String message, String canal, LocalDateTime dateEnvoi) {
        this.id = id;
        this.sujet = sujet;
        this.message = message;
        this.canal = canal;
        this.dateEnvoi = dateEnvoi;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public boolean isLu() {
        return lu;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentNom() {
        return parentNom;
    }

    public void setParentNom(String parentNom) {
        this.parentNom = parentNom;
    }

    public String getParentPrenom() {
        return parentPrenom;
    }

    public void setParentPrenom(String parentPrenom) {
        this.parentPrenom = parentPrenom;
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

    public boolean isEnvoyerATous() {
        return envoyerATous;
    }

    public void setEnvoyerATous(boolean envoyerATous) {
        this.envoyerATous = envoyerATous;
    }

    public Integer getNbDestinataires() {
        return nbDestinataires;
    }

    public void setNbDestinataires(Integer nbDestinataires) {
        this.nbDestinataires = nbDestinataires;
    }

    public Integer getNbLus() {
        return nbLus;
    }

    public void setNbLus(Integer nbLus) {
        this.nbLus = nbLus;
    }

    // Méthodes utilitaires
    public String getDateEnvoiFormatee() {
        if (dateEnvoi == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateEnvoi.format(formatter);
    }

    public String getDateEnvoiCourte() {
        if (dateEnvoi == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateEnvoi.format(formatter);
    }

    public String getHeureEnvoi() {
        if (dateEnvoi == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateEnvoi.format(formatter);
    }

    public String getCanalLibelle() {
        if (canal == null) return "Inconnu";

        switch (canal) {
            case "EMAIL": return "Email";
            case "SMS": return "SMS";
            case "NOTIFICATION": return "Notification";
            case "PUSH": return "Notification push";
            default: return canal;
        }
    }

    public String getCanalIcone() {
        if (canal == null) return "📨";

        switch (canal) {
            case "EMAIL": return "📧";
            case "SMS": return "📱";
            case "NOTIFICATION": return "🔔";
            case "PUSH": return "📲";
            default: return "📨";
        }
    }

    public String getDestinataireLibelle() {
        if (parentId != null) {
            return parentPrenom + " " + parentNom;
        } else if (animateurId != null) {
            return animateurPrenom + " " + animateurNom + " (Animateur)";
        } else if (envoyerATous) {
            return "Tous les parents";
        } else {
            return "Communication générale";
        }
    }

    public String getStatutLibelle() {
        return lu ? "Lu" : "Non lu";
    }

    public String getStatutColor() {
        return lu ? "#28a745" : "#dc3545";
    }

    public String getStatutBadge() {
        return lu ? "badge-success" : "badge-danger";
    }

    public String getApercuMessage() {
        if (message == null) return "";
        if (message.length() <= 100) return message;
        return message.substring(0, 100) + "...";
    }

    public boolean estPourParent() {
        return parentId != null;
    }

    public boolean estPourAnimateur() {
        return animateurId != null;
    }

    public boolean estGeneral() {
        return !estPourParent() && !estPourAnimateur() && !envoyerATous;
    }

    @Override
    public String toString() {
        return "CommunicationDTO{" +
                "id=" + id +
                ", sujet='" + sujet + '\'' +
                ", canal='" + canal + '\'' +
                ", parentId=" + parentId +
                ", animateurId=" + animateurId +
                ", envoyerATous=" + envoyerATous +
                '}';
    }
}
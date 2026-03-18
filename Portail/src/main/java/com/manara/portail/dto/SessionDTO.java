package com.manara.portail.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SessionDTO {

    private Integer id;

    @NotNull(message = "L'heure de début est requise")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est requise")
    private LocalTime heureFin;

    @NotNull(message = "La date est requise")
    @FutureOrPresent(message = "La date doit être dans le présent ou le futur")
    private LocalDate date;

    @NotNull(message = "La capacité maximale est requise")
    @Min(value = 1, message = "La capacité maximale doit être au moins 1")
    @Max(value = 100, message = "La capacité maximale ne peut pas dépasser 100")
    private Integer capaciteMax;

    @NotBlank(message = "Le lieu est requis")
    @Size(max = 255, message = "Le lieu ne peut pas dépasser 255 caractères")
    private String lieu;

    private String statut = "PLANIFIÉE";

    @NotNull(message = "L'ID de l'activité est requis")
    private Integer activiteId;

    private String activiteNom;

    private Integer animateurId;
    private String animateurNom;
    private String animateurPrenom;

    private List<Integer> inscriptionIds = new ArrayList<>();

    // Statistiques (pour l'affichage)
    private Integer nbInscrits;
    private Integer nbPlacesDisponibles;
    private Double tauxRemplissage;
    private List<String> enfantsInscrits;

    // Constructeurs
    public SessionDTO() {}

    public SessionDTO(Integer id, LocalTime heureDebut, LocalTime heureFin, LocalDate date,
                      Integer capaciteMax, String lieu, Integer activiteId) {
        this.id = id;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.date = date;
        this.capaciteMax = capaciteMax;
        this.lieu = lieu;
        this.activiteId = activiteId;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(Integer capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Integer getActiviteId() {
        return activiteId;
    }

    public void setActiviteId(Integer activiteId) {
        this.activiteId = activiteId;
    }

    public String getActiviteNom() {
        return activiteNom;
    }

    public void setActiviteNom(String activiteNom) {
        this.activiteNom = activiteNom;
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

    public List<Integer> getInscriptionIds() {
        return inscriptionIds;
    }

    public void setInscriptionIds(List<Integer> inscriptionIds) {
        this.inscriptionIds = inscriptionIds;
    }

    public Integer getNbInscrits() {
        return nbInscrits;
    }

    public void setNbInscrits(Integer nbInscrits) {
        this.nbInscrits = nbInscrits;
    }

    public Integer getNbPlacesDisponibles() {
        return nbPlacesDisponibles;
    }

    public void setNbPlacesDisponibles(Integer nbPlacesDisponibles) {
        this.nbPlacesDisponibles = nbPlacesDisponibles;
    }

    public Double getTauxRemplissage() {
        return tauxRemplissage;
    }

    public void setTauxRemplissage(Double tauxRemplissage) {
        this.tauxRemplissage = tauxRemplissage;
    }

    public List<String> getEnfantsInscrits() {
        return enfantsInscrits;
    }

    public void setEnfantsInscrits(List<String> enfantsInscrits) {
        this.enfantsInscrits = enfantsInscrits;
    }

    // Méthodes utilitaires
    public String getHoraire() {
        return heureDebut + " - " + heureFin;
    }

    public String getDateFormatee() {
        if (date == null) return "";
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
        return date.format(formatter);
    }

    public boolean isComplete() {
        return nbPlacesDisponibles != null && nbPlacesDisponibles <= 0;
    }

    public boolean hasAnimateur() {
        return animateurId != null;
    }

    public String getAnimateurNomComplet() {
        if (animateurNom == null || animateurPrenom == null) {
            return "Non assigné";
        }
        return animateurPrenom + " " + animateurNom;
    }

    public String getStatutLibelle() {
        if (statut == null) return "Non défini";

        switch (statut) {
            case "PLANIFIÉE": return "Planifiée";
            case "EN_COURS": return "En cours";
            case "TERMINEE": return "Terminée";
            case "ANNULEE": return "Annulée";
            case "COMPLETE": return "Complete";
            default: return statut;
        }
    }

    public String getStatutColor() {
        if (statut == null) return "#6c757d";

        switch (statut) {
            case "PLANIFIÉE": return "#17a2b8"; // Bleu
            case "EN_COURS": return "#28a745";  // Vert
            case "TERMINEE": return "#6c757d";   // Gris
            case "ANNULEE": return "#dc3545";    // Rouge
            case "COMPLETE": return "#ffc107";   // Jaune
            default: return "#6c757d";
        }
    }

    public String getTauxRemplissageLibelle() {
        if (tauxRemplissage == null) return "N/A";
        return String.format("%.1f%%", tauxRemplissage);
    }

    public String getCapaciteLibelle() {
        if (nbInscrits == null || capaciteMax == null) {
            return capaciteMax + " places";
        }
        return nbInscrits + " / " + capaciteMax + " places";
    }

    @Override
    public String toString() {
        return "SessionDTO{" +
                "id=" + id +
                ", date=" + date +
                ", heureDebut=" + heureDebut +
                ", lieu='" + lieu + '\'' +
                ", activiteId=" + activiteId +
                ", animateurId=" + animateurId +
                '}';
    }
}
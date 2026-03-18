package com.manara.portail.dto;

import jakarta.validation.constraints.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ActiviteDTO {

    private Integer id;

    @NotBlank(message = "Le nom de l'activité est requis")
    @Size(min = 3, max = 100, message = "Le nom doit contenir entre 3 et 100 caractères")
    private String nom;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    private String type;

    @NotNull(message = "L'âge minimum est requis")
    @Min(value = 0, message = "L'âge minimum doit être au moins 0")
    @Max(value = 18, message = "L'âge minimum ne peut pas dépasser 18")
    private Integer ageMin;

    @NotNull(message = "L'âge maximum est requis")
    @Min(value = 1, message = "L'âge maximum doit être au moins 1")
    @Max(value = 99, message = "L'âge maximum ne peut pas dépasser 99")
    private Integer ageMax;

    @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être positif")
    @DecimalMax(value = "9999.99", message = "Le prix ne peut pas dépasser 9999.99")
    private Float prix = 0.0f;  // Par défaut gratuit

    private boolean active = true;

    private String imageUrl;

    private List<Integer> sessionIds = new ArrayList<>();

    // Statistiques (pour l'affichage)
    private Integer nbSessions;
    private Integer nbInscriptions;
    private Double tauxRemplissageMoyen;

    // Constructeurs
    public ActiviteDTO() {}

    public ActiviteDTO(Integer id, String nom, String description, Integer ageMin, Integer ageMax, Float prix) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.prix = prix != null ? prix : 0.0f;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix != null ? prix : 0.0f;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Integer> getSessionIds() {
        return sessionIds;
    }

    public void setSessionIds(List<Integer> sessionIds) {
        this.sessionIds = sessionIds;
    }

    public Integer getNbSessions() {
        return nbSessions;
    }

    public void setNbSessions(Integer nbSessions) {
        this.nbSessions = nbSessions;
    }

    public Integer getNbInscriptions() {
        return nbInscriptions;
    }

    public void setNbInscriptions(Integer nbInscriptions) {
        this.nbInscriptions = nbInscriptions;
    }

    public Double getTauxRemplissageMoyen() {
        return tauxRemplissageMoyen;
    }

    public void setTauxRemplissageMoyen(Double tauxRemplissageMoyen) {
        this.tauxRemplissageMoyen = tauxRemplissageMoyen;
    }

    // Méthodes utilitaires
    public String getTrancheAge() {
        return ageMin + " - " + ageMax + " ans";
    }

    public String getPrixLibelle() {
        if (prix == null || prix == 0) {
            return "Gratuit";
        }
        return String.format("%.2f €", prix);
    }

    public String getStatutLibelle() {
        return active ? "Active" : "Inactive";
    }

    public String getStatutColor() {
        return active ? "#28a745" : "#dc3545";
    }

    @Override
    public String toString() {
        return "ActiviteDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", ageMin=" + ageMin +
                ", ageMax=" + ageMax +
                ", prix=" + prix +
                ", active=" + active +
                '}';
    }
}
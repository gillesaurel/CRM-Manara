package com.manara.portail.dto;

import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

public class ActiviteDTO {

    private Integer id;

    @NotBlank(message = "Le nom de l'activité est requis")
    @Size(min = 3, max = 100)
    private String nom;

    @Size(max = 500)
    private String description;

    @NotNull(message = "L'âge minimum est requis")
    @Min(value = 0)
    @Max(value = 18)
    private Integer ageMin;

    @NotNull(message = "L'âge maximum est requis")
    @Min(value = 1)
    @Max(value = 99)
    private Integer ageMax;

    @NotNull(message = "Le prix est requis")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "9999.99")
    private Float prix = 0.0f;

    private boolean active = true;

    private List<Integer> sessionIds = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getAgeMin() { return ageMin; }
    public void setAgeMin(Integer ageMin) { this.ageMin = ageMin; }

    public Integer getAgeMax() { return ageMax; }
    public void setAgeMax(Integer ageMax) { this.ageMax = ageMax; }

    public Float getPrix() { return prix; }
    public void setPrix(Float prix) { this.prix = prix; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<Integer> getSessionIds() { return sessionIds; }
    public void setSessionIds(List<Integer> sessionIds) { this.sessionIds = sessionIds; }
}
package com.manara.portail.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class SessionDTO {

    private Integer id;

    @NotNull(message = "L'heure de début est requise")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est requise")
    private LocalTime heureFin;

    @NotNull(message = "La date est requise")
    @FutureOrPresent
    private LocalDate date;

    @NotNull(message = "La capacité maximale est requise")
    @Min(value = 1)
    @Max(value = 100)
    private Integer capaciteMax;

    @NotBlank(message = "Le lieu est requis")
    @Size(max = 255)
    private String lieu;

    private String statut = "PLANIFIEE";

    @NotNull(message = "L'ID de l'activité est requis")
    private Integer activiteId;

    private Integer animateurId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(Integer capaciteMax) { this.capaciteMax = capaciteMax; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Integer getActiviteId() { return activiteId; }
    public void setActiviteId(Integer activiteId) { this.activiteId = activiteId; }

    public Integer getAnimateurId() { return animateurId; }
    public void setAnimateurId(Integer animateurId) { this.animateurId = animateurId; }
}
package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La date et heure sont requises")
    @Column(name = "date_heure", nullable = false)
    private LocalDateTime dateHeure;

    @NotBlank(message = "Le type d'incident est requis")
    @Column(length = 100, nullable = false)
    private String type;

    @NotBlank(message = "La description est requise")
    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    @Column(length = 1000, nullable = false)
    private String description;

    @Min(value = 1, message = "La gravite doit être au moins 1")
    @Max(value = 5, message = "La gravite ne peut pas dépasser 5")
    @Column(nullable = false)
    private Integer gravite;

    @NotBlank(message = "Le statut est requis")
    @Column(length = 50, nullable = false)
    private String statut = "SIGNALE";

    @Size(max = 1000, message = "Les actions prises ne peuvent pas dépasser 1000 caractères")
    @Column(name = "actions_prises", length = 1000)
    private String actionsPrises;

    @NotNull(message = "L'enfant est requis")
    @ManyToOne
    @JoinColumn(name = "enfant_id", nullable = false)
    private Enfant enfant;

    @ManyToOne
    @JoinColumn(name = "animateur_id")
    private Utilisateur animateur;

    public Incident() {}

    public Incident(String type, String description, Integer gravite, Enfant enfant) {
        this.type = type;
        this.description = description;
        this.gravite = gravite;
        this.enfant = enfant;
        this.dateHeure = LocalDateTime.now();
        this.statut = "SIGNALE";
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getGravite() { return gravite; }
    public void setGravite(Integer gravite) { this.gravite = gravite; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getActionsPrises() { return actionsPrises; }
    public void setActionsPrises(String actionsPrises) { this.actionsPrises = actionsPrises; }

    public Enfant getEnfant() { return enfant; }
    public void setEnfant(Enfant enfant) { this.enfant = enfant; }

    public Utilisateur getAnimateur() { return animateur; }
    public void setAnimateur(Utilisateur animateur) { this.animateur = animateur; }
}
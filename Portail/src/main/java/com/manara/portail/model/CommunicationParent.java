package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "communications_parent")
public class CommunicationParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le sujet ne peut pas être vide")
    @Size(max = 200, message = "Le sujet ne peut pas dépasser 200 caractères")
    @Column(length = 200, nullable = false)
    private String sujet;

    @NotBlank(message = "Le message ne peut pas être vide")
    @Size(max = 2000, message = "Le message ne peut pas dépasser 2000 caractères")
    @Column(length = 2000, nullable = false)
    private String message;

    @NotNull(message = "La date d'envoi est requise")
    @Column(name = "date_envoi", nullable = false)
    private LocalDateTime dateEnvoi = LocalDateTime.now();

    @NotBlank(message = "Le canal est requis")
    @Column(length = 50, nullable = false)
    private String canal;

    @Column(nullable = false)
    private boolean lu = false;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;  // ← Parent (votre classe)

    @ManyToOne
    @JoinColumn(name = "animateur_id")
    private Utilisateur animateur;  // ← Utilisateur (collègue)

    // Constructeurs
    public CommunicationParent() {}

    public CommunicationParent(String sujet, String message, String canal) {
        this.sujet = sujet;
        this.message = message;
        this.canal = canal;
        this.dateEnvoi = LocalDateTime.now();
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }

    public boolean isLu() { return lu; }
    public void setLu(boolean lu) { this.lu = lu; }

    public Parent getParent() { return parent; }
    public void setParent(Parent parent) { this.parent = parent; }

    public Utilisateur getAnimateur() { return animateur; }
    public void setAnimateur(Utilisateur animateur) { this.animateur = animateur; }
}
package com.manara.portail.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activites_parent")
public class ActiviteParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(length = 100, nullable = false, unique = true)
    private String nom;

    @Column(length = 500)
    private String description;


    @Column(name = "age_min", nullable = false)
    private Integer ageMin;

    @Column(name = "age_max", nullable = false)
    private Integer ageMax;
    @Column(nullable = false)
    private Float prix = 0.0f;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "activite", cascade = CascadeType.ALL)
    private List<SessionParent> sessions = new ArrayList<>();

    public ActiviteParent() {}

    public ActiviteParent(String nom, String description, Integer ageMin, Integer ageMax, Float prix) {
        this.nom = nom;
        this.description = description;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.prix = prix != null ? prix : 0.0f;
    }

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

    public List<SessionParent> getSessions() { return sessions; }
    public void setSessions(List<SessionParent> sessions) { this.sessions = sessions; }
}
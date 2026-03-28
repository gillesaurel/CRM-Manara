<<<<<<< HEAD
package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activites_parent")
public class ActiviteParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le nom de l'activité est requis")
    @Size(min = 3, max = 100, message = "Le nom doit contenir entre 3 et 100 caractères")
    @Column(length = 100, nullable = false, unique = true)
    private String nom;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    @NotNull(message = "L'âge minimum est requis")
    @Min(value = 0, message = "L'âge minimum doit être au moins 0")
    @Max(value = 18, message = "L'âge minimum ne peut pas dépasser 18")
    @Column(name = "age_min", nullable = false)
    private Integer ageMin;

    @NotNull(message = "L'âge maximum est requis")
    @Min(value = 1, message = "L'âge maximum doit être au moins 1")
    @Max(value = 99, message = "L'âge maximum ne peut pas dépasser 99")
    @Column(name = "age_max", nullable = false)
    private Integer ageMax;

    @NotNull(message = "Le prix est requis")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être positif")
    @DecimalMax(value = "9999.99", message = "Le prix ne peut pas dépasser 9999.99")
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
=======
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
>>>>>>> temporary-brench-manu
}
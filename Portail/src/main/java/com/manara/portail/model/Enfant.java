package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Enfant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le nom de l'enfant ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    @Column(length = 50, nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom de l'enfant ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    @Column(length = 50, nullable = false)
    private String prenom;

    @NotNull(message = "La date de naissance est requise")
    @Past(message = "La date de naissance doit être dans le passé")
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Size(max = 500, message = "Les allergies ne peuvent pas dépasser 500 caractères")
    @Column(length = 500)
    private String allergies;

    @Size(max = 1000, message = "Les notes médicales ne peuvent pas dépasser 1000 caractères")
    @Column(name = "notes_medicales", length = 1000)
    private String notesMedicales;

    @NotNull(message = "Le parent est requis")
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @OneToMany(mappedBy = "enfant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Presence> presences = new ArrayList<>();

    @OneToMany(mappedBy = "enfant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscription> inscriptions = new ArrayList<>();

    // Constructeurs
    public Enfant() {}

    public Enfant(String nom, String prenom, LocalDate dateNaissance, Parent parent) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.parent = parent;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getNotesMedicales() { return notesMedicales; }
    public void setNotesMedicales(String notesMedicales) { this.notesMedicales = notesMedicales; }

    public Parent getParent() { return parent; }
    public void setParent(Parent parent) { this.parent = parent; }

    public List<Presence> getPresences() { return presences; }
    public void setPresences(List<Presence> presences) { this.presences = presences; }

    public List<Inscription> getInscriptions() { return inscriptions; }
    public void setInscriptions(List<Inscription> inscriptions) { this.inscriptions = inscriptions; }
}
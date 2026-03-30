package com.manara.portail.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enfant")
public class Enfant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(length = 50, nullable = false)
    private String nom;


    @Column(length = 50, nullable = false)
    private String prenom;



    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(length = 500)
    private String allergies;

    @Column(name = "notes_medicales", length = 1000)
    private String notesMedicales;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @OneToMany(mappedBy = "enfant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Presence> presences = new ArrayList<>();

    @OneToMany(mappedBy = "enfant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InscriptionEnfant> inscriptions = new ArrayList<>();

    public Enfant() {}

    public Enfant(String nom, String prenom, LocalDate dateNaissance, Parent parent) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.parent = parent;
    }

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

    public List<InscriptionEnfant> getInscriptions() { return inscriptions; }
    public void setInscriptions(List<InscriptionEnfant> inscriptions) { this.inscriptions = inscriptions; }
}
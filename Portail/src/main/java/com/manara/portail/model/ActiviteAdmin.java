// fait par Gilles Aurel

package com.manara.portail.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
<<<<<<< HEAD
@Table(name = "activites")
=======
@Table(name = "activite")
>>>>>>> temporary-brench-manu
public class ActiviteAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int capacite;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateDebut;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToMany(mappedBy = "activite", cascade = CascadeType.ALL)
    private List<Inscription> inscriptions =  new ArrayList<>();

    public long getNbEnAttente() {
        return inscriptions.stream().filter(Inscription::isEnAttente).count();
    }

    public long getNbInscrits() {
        return inscriptions.stream().filter(i -> !i.isEnAttente()).count();
    }

    public ActiviteAdmin() {

    }

    public ActiviteAdmin(String nom, String description, int capacite) {
        this.nom = nom;
        this.description = description;
        this.capacite = capacite;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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

    public int getCapacite() {
        return capacite;
    }
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }
}

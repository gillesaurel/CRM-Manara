package com.manara.portail.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "enfant")
public class Enfants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Utilisateur parent;

    public Enfants() {
    }

    public Enfants(String nom, LocalDate dateNaissance, Utilisateur parent) {
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.parent = parent;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Utilisateur getParent() {
        return parent;
    }

    public void setParent(Utilisateur parent) {
        this.parent = parent;
    }
}
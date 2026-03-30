// fait par Gilles Aurel

package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activites")
public class ActiviteAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Min(1)
    @Column(nullable = false)
    private int capacite;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    private LocalDateTime dateDebut;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToMany(mappedBy = "activite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InscriptionEnfant> inscriptions = new ArrayList<>();

    public long getNbEnAttente() {
        return inscriptions.stream().filter(InscriptionEnfant::isEnAttente).count();
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

    public List<InscriptionEnfant> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<InscriptionEnfant> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public int getTauxRemplissage() {
        if (capacite == 0) return 0;
        return (int)((getNbInscrits() * 100) / capacite);
    }

    public boolean isComplet() {
        return getNbInscrits() >= capacite;
    }
}

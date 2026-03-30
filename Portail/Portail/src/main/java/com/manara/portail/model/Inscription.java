package com.manara.portail.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inscriptions")
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomEnfant;
    private String contactParent;

    @Column(name = "en_attente")
    private boolean enAttente;

    @ManyToOne
    @JoinColumn(name = "activites_id")
    private ActiviteAdmin activite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEnfant() {
        return nomEnfant;
    }

    public void setNomEnfant(String nomEnfant) {
        this.nomEnfant = nomEnfant;
    }

    public String getContactParent() {
        return contactParent;
    }

    public void setContactParent(String contactParent) {
        this.contactParent = contactParent;
    }

    public ActiviteAdmin getActivite() {
        return activite;
    }

    public void setActivite(ActiviteAdmin activite) {
        this.activite = activite;
    }

    public boolean isEnAttente() {
        return enAttente;
    }

    public void setEnAttente(boolean enAttente) {
        this.enAttente = enAttente;
    }
}

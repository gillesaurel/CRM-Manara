package com.manara.portail.model;


import jakarta.persistence.*;

@Entity
@Table(name = "inscription_enfant")
public class InscriptionEnfant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomEnfant;

    private boolean enAttente;

    @ManyToOne
    @JoinColumn(name = "activites_id")
    private ActiviteAdmin activite;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "enfant_id")
    private Enfant enfant;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private SessionParent session;

    public SessionParent getSession() { return session; }
    public void setSession(SessionParent session) { this.session = session; }

    // + getter and setter
    public Enfant getEnfant() { return enfant; }
    public void setEnfant(Enfant enfant) { this.enfant = enfant; }

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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}

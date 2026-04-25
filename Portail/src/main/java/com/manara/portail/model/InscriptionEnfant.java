package com.manara.portail.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "inscription_enfant")
public class InscriptionEnfant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String nomEnfant;

    private boolean enAttente;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "activite_id", nullable = false)
    private ActiviteAdmin activite;

    @ManyToOne
    @JsonIgnore

    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne
    @JsonIgnore

    @JoinColumn(name = "enfant_id")
    private Enfant enfant;

    @ManyToOne
    @JsonIgnore

    @JoinColumn(name = "session_id")
    private SessionParent session;

    public SessionParent getSession() { return session; }
    public void setSession(SessionParent session) { this.session = session; }
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

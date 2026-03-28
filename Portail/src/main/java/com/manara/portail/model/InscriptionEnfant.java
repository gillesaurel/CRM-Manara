package com.manara.portail.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inscriptions_enfant")
public class InscriptionEnfant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean enAttente;

    @ManyToOne
    @JoinColumn(name = "activite_id")
    private ActiviteParent activite;

    @ManyToOne
    @JoinColumn(name = "enfant_id")
    private Enfant enfant;

    @ManyToOne
<<<<<<< HEAD
    @JoinColumn(name = "session_id")
    private SessionParent session;
=======
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
>>>>>>> temporary-brench-manu

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isEnAttente() { return enAttente; }
    public void setEnAttente(boolean enAttente) { this.enAttente = enAttente; }

    public ActiviteParent getActivite() { return activite; }
    public void setActivite(ActiviteParent activite) { this.activite = activite; }

    public Enfant getEnfant() { return enfant; }
    public void setEnfant(Enfant enfant) { this.enfant = enfant; }

    public SessionParent getSession() { return session; }
    public void setSession(SessionParent session) { this.session = session; }
}
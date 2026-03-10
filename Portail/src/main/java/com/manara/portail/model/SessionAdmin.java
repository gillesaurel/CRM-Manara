package com.manara.portail.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SessionAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(nullable = false, name = "date_fin")
    private LocalDateTime dateFin;

    @ManyToOne
    @JoinColumn(name = "activite_id", nullable = false)
    private ActiviteAdmin activite;

    public SessionAdmin() {

    }

    public SessionAdmin(LocalDateTime dateDebut, LocalDateTime dateFin, ActiviteAdmin activite) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.activite = activite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public ActiviteAdmin getActivite() {
        return activite;
    }

    public void setActivite(ActiviteAdmin activite) {
        this.activite = activite;
    }
}

package com.manara.portail.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Table(name = "activite_seance")
public class ActiviteSeance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @ManyToOne
    @JoinColumn(name = "activite_id", nullable = false)
    @JsonBackReference
    private ActiviteAdmin activite;

    public ActiviteSeance() {}

    public ActiviteSeance(LocalDateTime dateHeure, ActiviteAdmin activite) {
        this.dateHeure = dateHeure;
        this.activite = activite;
    }

    public ActiviteAdmin getActivite() {
        return activite;
    }

    public void setActivite(ActiviteAdmin activite) {
        this.activite = activite;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
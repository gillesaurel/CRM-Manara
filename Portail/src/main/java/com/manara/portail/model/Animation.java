package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Animation extends Personne {

    @NotBlank(message = "La spécialité ne peut pas être vide")
    @Size(max = 100, message = "La spécialité ne peut pas dépasser 100 caractères")
    @Column(length = 100, nullable = false)
    private String specialite;

    @Size(max = 500, message = "Les notes ne peuvent pas dépasser 500 caractères")
    @Column(length = 500)
    private String notes;

    @OneToMany(mappedBy = "animateur")
    private List<Session> sessions = new ArrayList<>();

    // Constructeurs
    public Animation() {}

    public Animation(String lNom, String pPrenom, String email, String telephone, String motDePasseHash,
                     String specialite, String notes) {
        super(lNom, pPrenom, email, telephone, motDePasseHash);
        this.specialite = specialite;
        this.notes = notes;
    }

    // Getters et Setters
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<Session> getSessions() { return sessions; }
    public void setSessions(List<Session> sessions) { this.sessions = sessions; }
}
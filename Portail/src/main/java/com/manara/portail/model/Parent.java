package com.manara.portail.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parent")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enfant> enfants = new ArrayList<>();

    public Parent() {}

    public Parent(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public List<Enfant> getEnfants() { return enfants; }
    public void setEnfants(List<Enfant> enfants) { this.enfants = enfants; }

    public void addEnfant(Enfant enfant) {
        enfants.add(enfant);
        enfant.setParent(this);
    }

    public void removeEnfant(Enfant enfant) {
        enfants.remove(enfant);
        enfant.setParent(null);
    }
}
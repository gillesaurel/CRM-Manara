package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent extends Personne {

    @NotBlank(message = "L'adresse ne peut pas être vide")
    @Size(max = 255, message = "L'adresse ne peut pas dépasser 255 caractères")
    @Column(length = 255, nullable = false)
    private String adresse;

    @NotBlank(message = "La ville ne peut pas être vide")
    @Size(max = 100, message = "La ville ne peut pas dépasser 100 caractères")
    @Column(length = 100, nullable = false)
    private String ville;

    @NotBlank(message = "Le code postal ne peut pas être vide")
    @Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit contenir 5 chiffres")
    @Column(name = "code_postale", length = 10, nullable = false)
    private String codePostale;

    @NotBlank(message = "Le type de lien ne peut pas être vide")
    @Column(name = "type_lien", length = 50, nullable = false)
    private String typeLien;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enfant> enfants = new ArrayList<>();

    // Constructeurs
    public Parent() {}

    public Parent(String lNom, String pPrenom, String email, String telephone, String motDePasseHash,
                  String adresse, String ville, String codePostale, String typeLien) {
        super(lNom, pPrenom, email, telephone, motDePasseHash);
        this.adresse = adresse;
        this.ville = ville;
        this.codePostale = codePostale;
        this.typeLien = typeLien;
    }

    // Getters et Setters
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCodePostale() { return codePostale; }
    public void setCodePostale(String codePostale) { this.codePostale = codePostale; }

    public String getTypeLien() { return typeLien; }
    public void setTypeLien(String typeLien) { this.typeLien = typeLien; }

    public List<Enfant> getEnfants() { return enfants; }
    public void setEnfants(List<Enfant> enfants) { this.enfants = enfants; }

    // Méthodes utilitaires
    public void addEnfant(Enfant enfant) {
        enfants.add(enfant);
        enfant.setParent(this);
    }

    public void removeEnfant(Enfant enfant) {
        enfants.remove(enfant);
        enfant.setParent(null);
    }
}
// fait par Manu Charland-Rivard

package com.manara.portail.model;

import jakarta.persistence.*;
import com.manara.portail.model.Role;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prenom;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String telephone;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String motdepasse;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    // CHAMPS PARENT
    @Column(name = "adresse")
    private String adresse;

    @Column(name = "ville")
    private String ville;

    @Column(name = "code_postal", length = 10)
    private String codePostal;

    @Column(name = "type_lien", length = 50)
    private String typeLien;

    // getters and setters



    public Utilisateur() {
    }

    public Utilisateur(String email, String motdepasse) {
        this.email = email;
        this.motdepasse = motdepasse;
    }

    public Utilisateur( String nom, String prenom,String email, String motdepasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
    }

    public Utilisateur(String nom, String prenom,String email, String motdepasse, Role role) {

        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
        this.role = role;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}


    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }


    //Méthode parents
    // Getters et Setters
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getTypeLien() { return typeLien; }
    public void setTypeLien(String typeLien) { this.typeLien = typeLien; }

}

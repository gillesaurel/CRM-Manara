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
    @Column(nullable = false)
    @Transient
    private String confirmMotdepasse;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // getters and setters



    public Utilisateur() {
    }

    public Utilisateur(String email, String motdepasse) {
        this.email = email;
        this.motdepasse = motdepasse;
    }

    public Utilisateur( String nom, String prenom,String email, String motdepasse, String confirmMotdepasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
        this.confirmMotdepasse = confirmMotdepasse;
    }

    public Utilisateur(String nom, String prenom,String email, String motdepasse,String confirmMotdepasse, Role role) {

        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
        this.confirmMotdepasse = confirmMotdepasse;
        this.role = role;
    }

    public Long getId() {
        return id;
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

    public String getConfirmMotdepasse() {
        return confirmMotdepasse;
    }
    public void setConfirmMotdepasse(String confirmMotdepasse) {
        this.confirmMotdepasse = confirmMotdepasse;
    }

}

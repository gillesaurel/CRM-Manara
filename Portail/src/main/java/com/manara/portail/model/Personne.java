package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    @Column(name = "l_nom", length = 50, nullable = false)
    private String lNom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    @Column(name = "p_prenom", length = 50, nullable = false)
    private String pPrenom;

    @NotBlank(message = "L'adresse e-mail ne peut pas être vide")
    @Email(message = "L'adresse email n'est pas valide")
    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le téléphone ne peut pas être vide")
    @Pattern(regexp = "^[0-9]{10}$", message = "Le téléphone doit contenir 10 chiffres")
    @Column(length = 20, nullable = false)
    private String telephone;

    @Column(nullable = false)
    private boolean actif = true;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Le mot de passe doit contenir au moins une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial")
    @Column(name = "mot_de_passe_hash", length = 255, nullable = false)
    private String motDePasseHash;

    @Column(name = "qr_id")
    private Integer qrId;

    // Constructeurs
    public Personne() {}

    public Personne(String lNom, String pPrenom, String email, String telephone, String motDePasseHash) {
        this.lNom = lNom;
        this.pPrenom = pPrenom;
        this.email = email;
        this.telephone = telephone;
        this.motDePasseHash = motDePasseHash;
        this.actif = true;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getlNom() { return lNom; }
    public void setlNom(String lNom) { this.lNom = lNom; }

    public String getpPrenom() { return pPrenom; }
    public void setpPrenom(String pPrenom) { this.pPrenom = pPrenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public String getMotDePasseHash() { return motDePasseHash; }
    public void setMotDePasseHash(String motDePasseHash) { this.motDePasseHash = motDePasseHash; }

    public Integer getQrId() { return qrId; }
    public void setQrId(Integer qrId) { this.qrId = qrId; }
}
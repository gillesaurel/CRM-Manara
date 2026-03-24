package com.manara.portail.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ParentDTO {

    private Integer id;

    @NotBlank(message = "Le nom est requis")
    @Size(min = 2, max = 50)
    private String nom;

    @NotBlank(message = "Le prénom est requis")
    @Size(min = 2, max = 50)
    private String prenom;

    @NotBlank(message = "L'email est requis")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Le téléphone est requis")
    @Pattern(regexp = "^[0-9]{10}$", message = "Téléphone invalide")
    private String telephone;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String motDePasse;

    @NotBlank(message = "L'adresse est requise")
    private String adresse;

    @NotBlank(message = "La ville est requise")
    private String ville;

    @NotBlank(message = "Le code postal est requis")
    @Pattern(regexp = "^[0-9]{5}$", message = "Code postal invalide")
    private String codePostal;

    @NotBlank(message = "Le type de lien est requis")
    private String typeLien;

    // Getters et setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getTypeLien() { return typeLien; }
    public void setTypeLien(String typeLien) { this.typeLien = typeLien; }
}
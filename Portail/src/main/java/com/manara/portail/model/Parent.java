package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parents")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Lien avec l'utilisateur (authentification)
    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    // Attributs spécifiques aux parents
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

    @Column(nullable = false)
    private boolean actif = true;

    @Column(name = "qr_id")
    private Integer qrId;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnfantParent> enfants = new ArrayList<>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunicationParent> communications = new ArrayList<>();

    // Constructeurs
    public Parent() {}

    public Parent(Utilisateur utilisateur, String lNom, String pPrenom, String email, String telephone,
                  String adresse, String ville, String codePostale, String typeLien) {
        this.utilisateur = utilisateur;
        this.lNom = lNom;
        this.pPrenom = pPrenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostale = codePostale;
        this.typeLien = typeLien;
        this.actif = true;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public String getlNom() { return lNom; }
    public void setlNom(String lNom) { this.lNom = lNom; }

    public String getpPrenom() { return pPrenom; }
    public void setpPrenom(String pPrenom) { this.pPrenom = pPrenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCodePostale() { return codePostale; }
    public void setCodePostale(String codePostale) { this.codePostale = codePostale; }

    public String getTypeLien() { return typeLien; }
    public void setTypeLien(String typeLien) { this.typeLien = typeLien; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public Integer getQrId() { return qrId; }
    public void setQrId(Integer qrId) { this.qrId = qrId; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public List<EnfantParent> getEnfants() { return enfants; }
    public void setEnfants(List<EnfantParent> enfants) { this.enfants = enfants; }

    public List<CommunicationParent> getCommunications() { return communications; }
    public void setCommunications(List<CommunicationParent> communications) { this.communications = communications; }

    // Méthodes utilitaires
    public void addEnfant(EnfantParent enfant) {
        enfants.add(enfant);
        enfant.setParent(this);
    }

    public void removeEnfant(EnfantParent enfant) {
        enfants.remove(enfant);
        enfant.setParent(null);
    }
}
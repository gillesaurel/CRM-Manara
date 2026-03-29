package com.manara.portail.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class EnfantDTO {

    private Integer id;

    @NotBlank(message = "Le nom de l'enfant est requis")
    private String nom;

    @NotBlank(message = "Le prénom de l'enfant est requis")
    private String prenom;

    @NotNull(message = "La date de naissance est requise")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;

    private String allergies;
    private String notesMedicales;

    private Integer parentId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getNotesMedicales() { return notesMedicales; }
    public void setNotesMedicales(String notesMedicales) { this.notesMedicales = notesMedicales; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }
}
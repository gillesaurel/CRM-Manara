package com.manara.portail.dto;

import jakarta.validation.constraints.*;

public class IncidentDTO {

    private Integer id;

    @NotBlank(message = "Le type d'incident est requis")
    @Size(max = 100)
    private String type;

    @NotBlank(message = "La description est requise")
    @Size(max = 1000)
    private String description;

    @NotNull(message = "La gravité est requise")
    @Min(1)
    @Max(5)
    private Integer gravite;

    @NotNull(message = "L'ID de l'enfant est requis")
    private Integer enfantId;

    private Integer animateurId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getGravite() { return gravite; }
    public void setGravite(Integer gravite) { this.gravite = gravite; }

    public Integer getEnfantId() { return enfantId; }
    public void setEnfantId(Integer enfantId) { this.enfantId = enfantId; }

    public Integer getAnimateurId() { return animateurId; }
    public void setAnimateurId(Integer animateurId) { this.animateurId = animateurId; }
}
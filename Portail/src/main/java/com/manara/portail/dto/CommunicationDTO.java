package com.manara.portail.dto;

import jakarta.validation.constraints.*;

public class CommunicationDTO {

    private Integer id;

    @NotBlank(message = "Le sujet est requis")
    @Size(max = 200)
    private String sujet;

    @NotBlank(message = "Le message est requis")
    @Size(max = 2000)
    private String message;

    @NotBlank(message = "Le canal est requis")
    @Pattern(regexp = "EMAIL|SMS|NOTIFICATION|PUSH")
    private String canal;

    private Integer parentId;
    private boolean envoyerATous = false;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }

    public boolean isEnvoyerATous() { return envoyerATous; }
    public void setEnvoyerATous(boolean envoyerATous) { this.envoyerATous = envoyerATous; }
}
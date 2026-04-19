package com.manara.portail.service;

import com.manara.portail.model.Enfant;
import com.manara.portail.model.Incident;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.EnfantRepository;
import com.manara.portail.repository.IncidentRepository;
import com.manara.portail.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private EnfantRepository enfantRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Incident> getIncidentsByAnimateur(Integer animateurId) {
        return incidentRepository.findByAnimateurId(animateurId);
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    @Transactional
    public Incident signalerIncident(Integer enfantId, Integer animateurId,
                                     String type, String description,
                                     Integer gravite, String actionsPrises) {
        Enfant enfant = enfantRepository.findById(enfantId)
                .orElseThrow(() -> new RuntimeException("Enfant introuvable avec id: " + enfantId));

        Utilisateur animateur = utilisateurRepository.findById(animateurId.longValue())
                .orElseThrow(() -> new RuntimeException("Animateur introuvable avec id: " + animateurId));

        Incident incident = new Incident();
        incident.setEnfant(enfant);
        incident.setAnimateur(animateur);
        incident.setType(type);
        incident.setDescription(description);
        incident.setGravite(gravite);
        incident.setActionsPrises(actionsPrises);
        incident.setDateHeure(LocalDateTime.now());
        incident.setStatut("SIGNALE");

        return incidentRepository.save(incident);
    }
}
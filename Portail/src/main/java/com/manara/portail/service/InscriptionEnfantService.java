package com.manara.portail.service;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.ActiviteRepository;
import com.manara.portail.repository.InscriptionEnfantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscriptionEnfantService {
    @Autowired
    private InscriptionEnfantRepository inscriptionRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private ActiviteRepository activiteRepository;
    public InscriptionEnfant inscriptionEnfant(String nom, Long activite_id, Utilisateur parent) {
        ActiviteAdmin activite = activiteRepository.findById(activite_id).orElseThrow(
                () -> new ActiviteIntrouvableException("L'activité n'existe pas")
        );
        if (activite.getNbInscrits() >= activite.getCapacite()) {
            throw new ActiviteDejaFullException("L'activité est complète");
        }

        InscriptionEnfant inscriptionEnfant = new InscriptionEnfant();
        inscriptionEnfant.setActivite(activite);
        inscriptionEnfant.setUtilisateur(parent);
        inscriptionEnfant.setNomEnfant(nom);
        inscriptionEnfant.setEnAttente(false);

        emailService.sendEmail(
                parent.getEmail(),
                "Confirmation inscription",
                "Votre enfant est inscrit à l'activité " + activite.getNom() + "qui aura lieu le "
                + activite.getDateDebut()
        );

        return inscriptionRepository.save(inscriptionEnfant);
    }
}

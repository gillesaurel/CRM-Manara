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
    private ActiviteRepository activiteRepository;
    public InscriptionEnfant InscriptionEnfant(String nom, Long activite_id, Utilisateur parent) {
        ActiviteAdmin activite = activiteRepository.findById(activite_id).orElseThrow(
                () -> new ActiviteIntrouvableException("Le activite n'existe pas")
        );
        if (activite.getNbInscrits() >= activite.getCapacite()) {
            throw new ActiviteDejaFullException("Le activite n'existe pas");
        }

        InscriptionEnfant inscriptionEnfant = new InscriptionEnfant();
        inscriptionEnfant.setActivite(activite);
        inscriptionEnfant.setUtilisateur(parent);
        inscriptionEnfant.setNomEnfant(nom);
        inscriptionEnfant.setEnAttente(false);

        return inscriptionRepository.save(inscriptionEnfant);
    }
}

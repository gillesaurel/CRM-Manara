package com.manara.portail.service;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.ActiviteRepository;
import com.manara.portail.repository.InscriptionEnfantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscriptionEnfantService {
    @Autowired
    private InscriptionEnfantRepository inscriptionRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private ActiviteRepository activiteRepository;
    public InscriptionEnfantService(
            InscriptionEnfantRepository inscriptionRepository,
            ActiviteRepository activiteRepository,
            EmailService emailService) {
        this.inscriptionRepository = inscriptionRepository;
        this.activiteRepository = activiteRepository;
        this.emailService = emailService;
    }
    public InscriptionEnfant inscriptionEnfant(String nom, Long activite_id, Utilisateur parent) {

        ActiviteAdmin activite = activiteRepository.findById(activite_id)
                .orElseThrow(() -> new ActiviteIntrouvableException("L'activité n'existe pas"));

        InscriptionEnfant inscription = new InscriptionEnfant();
        inscription.setActivite(activite);
        inscription.setUtilisateur(parent);
        inscription.setNomEnfant(nom);

        boolean estComplet = activite.getNbInscrits() >= activite.getCapacite();

        if (estComplet) {
            inscription.setEnAttente(true);

            emailService.sendEmail(
                    parent.getEmail(),
                    "Liste d'attente",
                    "L'activité " + activite.getNom() + " est complète.\n" +
                            "Votre enfant est placé sur liste d'attente."
            );

        } else {
            inscription.setEnAttente(false);

            emailService.sendEmail(
                    parent.getEmail(),
                    "Confirmation inscription",
                    "Votre enfant est inscrit à l'activité " + activite.getNom() +
                            "\nDate : " + activite.getDateDebut()
            );
        }

        return inscriptionRepository.save(inscription);
    }

    public InscriptionEnfant getById(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription introuvable"));
    }

    public InscriptionEnfant save(InscriptionEnfant inscription) {
        return inscriptionRepository.save(inscription);
    }

    public void deleteById(Long id) {
        InscriptionEnfant inscription = getById(id);
        Long activiteId = inscription.getActivite().getId();

        inscriptionRepository.deleteById(id);

        List<InscriptionEnfant> listeAttente =
                inscriptionRepository.findByActiviteIdAndEnAttenteTrueOrderByIdAsc(activiteId);

        if (!listeAttente.isEmpty()) {
            InscriptionEnfant next = listeAttente.get(0);
            next.setEnAttente(false);
            inscriptionRepository.save(next);

            envoyerEmailInscription(next);
        }
    }
    private void envoyerEmailInscription(InscriptionEnfant inscription) {
        emailService.sendEmail(
                inscription.getUtilisateur().getEmail(),
                "Votre enfant est inscrit !",
                "Bonjour " + inscription.getUtilisateur().getNom() + ",\n\n" +
                        "Une place s'est libérée pour l'activité " +
                        inscription.getActivite().getNom() + ".\n" +
                        "Votre enfant " + inscription.getNomEnfant() +
                        " est maintenant inscrit.\n" +
                        "Date de l'activité : " + inscription.getActivite().getDateDebut() + "\n\n" +
                        "Merci."
        );
    }

    public long countInscrits(Long activiteId) {
        return inscriptionRepository.countByActiviteIdAndEnAttenteFalse(activiteId);
    }
}

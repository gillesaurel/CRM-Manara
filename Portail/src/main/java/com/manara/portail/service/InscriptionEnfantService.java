package com.manara.portail.service;

import com.manara.portail.exception.ResourceNotFoundException;
import com.manara.portail.model.*;
import com.manara.portail.repository.ActiviteRepository;
import com.manara.portail.repository.EnfantRepository;
import com.manara.portail.repository.InscriptionEnfantRepository;
import com.manara.portail.repository.SessionParentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InscriptionEnfantService {
    @Autowired
    private InscriptionEnfantRepository inscriptionRepository;
    @Autowired
    private EmailService emailService;
    private final EnfantRepository enfantRepository;
    private final SessionParentRepository sessionRepository;
    private final CommunicationService communicationService;
    @Autowired
    private ActiviteRepository activiteRepository;
    public InscriptionEnfantService(
            InscriptionEnfantRepository inscriptionRepository,
            ActiviteRepository activiteRepository,
            EmailService emailService,
            EnfantRepository enfantRepository,
            SessionParentRepository sessionRepository,
            CommunicationService communicationService) {
        this.inscriptionRepository = inscriptionRepository;
        this.activiteRepository = activiteRepository;
        this.emailService = emailService;
        this.enfantRepository = enfantRepository;
        this.sessionRepository = sessionRepository;
        this.communicationService = communicationService;
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

    public List<InscriptionEnfant> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }

    public InscriptionEnfant getInscriptionById(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvee avec id: " + id));
    }

    public List<InscriptionEnfant> getInscriptionsBySession(Integer sessionId) {
        return inscriptionRepository.findBySessionId(sessionId);
    }

    public long getNombreConfirmationsBySession(Integer sessionId) {
        return inscriptionRepository.countConfirmationsBySession(sessionId);
    }

    public List<Enfant> getEnfantsConfirmesBySession(Integer sessionId) {
        return enfantRepository.findBySessionIdAndConfirmee(sessionId);
    }

    public List<InscriptionEnfant> getInscriptionsByEnfant(Integer enfantId) {
        return inscriptionRepository.findByEnfantId(enfantId);
    }

    public List<InscriptionEnfant> getInscriptionsByParent(Integer parentId) {
        return inscriptionRepository.findAllByParent(parentId);
    }

    public List<InscriptionEnfant> getInscriptionsFuturesParent(Integer parentId) {
        return inscriptionRepository.findFuturesByParent(parentId, LocalDate.now());
    }

    public List<InscriptionEnfant> getInscriptionsPasseesParent(Integer parentId) {
        return inscriptionRepository.findPassesByParent(parentId, LocalDate.now());
    }

    public List<InscriptionEnfant> getInscriptionsAttente(Integer parentId) {
        return inscriptionRepository.findAttenteByParent(parentId);
    }

    public List<InscriptionEnfant> getListeAttenteBySession(Integer sessionId) {
        return inscriptionRepository.findListeAttenteBySession(sessionId);
    }

    @Transactional
    public InscriptionEnfant inscrireEnfant(Integer enfantId, Integer sessionId, String commentaire) {
        Enfant enfant = enfantRepository.findById(enfantId)
                .orElseThrow(() -> new ResourceNotFoundException("Enfant non trouve avec id: " + enfantId));

        SessionParent session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session non trouvee avec id: " + sessionId));

        if (inscriptionRepository.findByEnfantIdAndSessionId(enfantId, sessionId).isPresent()) {
            throw new RuntimeException("L'enfant est deja inscrit a cette session");
        }

        int age = LocalDate.now().getYear() - enfant.getDateNaissance().getYear();
        if (enfant.getDateNaissance().plusYears(age).isAfter(LocalDate.now())) {
            age--;
        }

        if (age < session.getActivite().getAgeMin() || age > session.getActivite().getAgeMax()) {
            throw new RuntimeException("L'age de l'enfant ne correspond pas aux criteres de l'activite");
        }

        InscriptionEnfant inscription = new InscriptionEnfant();
        inscription.setEnfant(enfant);
        inscription.setSession(session);
        inscription.setEnAttente(false);

        long nbInscrits = inscriptionRepository.countConfirmationsBySession(sessionId);
        if (nbInscrits < session.getCapaciteMax()) {
            inscription.setEnAttente(false);
        } else {
            inscription.setEnAttente(true);
        }

        InscriptionEnfant saved = inscriptionRepository.save(inscription);
        communicationService.envoyerConfirmationInscription(enfant, session, saved.isEnAttente() ? "EN_ATTENTE" : "CONFIRMEE");

        return saved;
    }

    @Transactional
    public InscriptionEnfant confirmerInscription(Long inscriptionId) {
        InscriptionEnfant inscription = getInscriptionById(inscriptionId);

        if (!inscription.isEnAttente()) {
            throw new RuntimeException("Seules les inscriptions en attente peuvent etre confirmees");
        }

        inscription.setEnAttente(false);
        return inscriptionRepository.save(inscription);
    }

    @Transactional
    public InscriptionEnfant annulerInscription(Long inscriptionId) {
        InscriptionEnfant inscription = getInscriptionById(inscriptionId);

        inscriptionRepository.delete(inscription);
        communicationService.notifierAnnulation(inscription);

        return inscription;
    }

    public boolean estInscrit(Integer enfantId, Integer sessionId) {
        return inscriptionRepository.findByEnfantIdAndSessionId(enfantId, sessionId).isPresent();
    }
}

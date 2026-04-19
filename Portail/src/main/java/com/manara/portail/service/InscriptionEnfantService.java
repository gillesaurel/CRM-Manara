package com.manara.portail.service;

import com.manara.portail.exception.ResourceNotFoundException;
import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.SessionParent;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.ActiviteRepository;
import com.manara.portail.repository.EnfantRepository;
import com.manara.portail.repository.InscriptionEnfantRepository;
import com.manara.portail.repository.SessionParentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InscriptionEnfantService {

    private final InscriptionEnfantRepository inscriptionRepository;
    private final EnfantRepository enfantRepository;
    private final SessionParentRepository sessionRepository;
    private final CommunicationService communicationService;
    private final ActiviteRepository activiteRepository;
    private final EmailService emailService;

    public InscriptionEnfantService(InscriptionEnfantRepository inscriptionRepository,
                                    EnfantRepository enfantRepository,
                                    SessionParentRepository sessionRepository,
                                    CommunicationService communicationService,
                                    ActiviteRepository activiteRepository,
                                    EmailService emailService) {
        this.inscriptionRepository = inscriptionRepository;
        this.enfantRepository = enfantRepository;
        this.sessionRepository = sessionRepository;
        this.communicationService = communicationService;
        this.activiteRepository = activiteRepository;
        this.emailService = emailService;
    }

    public List<InscriptionEnfant> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }

    public InscriptionEnfant getInscriptionById(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvee avec id: " + id));
    }

    public InscriptionEnfant getById(Long id) {
        return getInscriptionById(id);
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

    public List<InscriptionEnfant> getListeAttente() {
        return inscriptionRepository.findListeAttente();
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

        long nbInscrits = inscriptionRepository.countConfirmationsBySession(sessionId);
        inscription.setEnAttente(nbInscrits >= session.getCapaciteMax());

        InscriptionEnfant saved = inscriptionRepository.save(inscription);
        communicationService.envoyerConfirmationInscription(
                enfant,
                session,
                saved.isEnAttente() ? "EN_ATTENTE" : "CONFIRMEE"
        );

        return saved;
    }

    @Transactional
    public InscriptionEnfant inscriptionEnfant(String nom, Long activiteId, Utilisateur parent) {
        ActiviteAdmin activite = activiteRepository.findById(activiteId)
                .orElseThrow(() -> new ActiviteIntrouvableException("L'activité n'existe pas"));

        InscriptionEnfant inscription = new InscriptionEnfant();
        inscription.setActivite(activite);
        inscription.setUtilisateur(parent);
        inscription.setNomEnfant(nom);

        boolean estComplet = activite.getNbInscrits() >= activite.getCapacite();
        inscription.setEnAttente(estComplet);

        InscriptionEnfant saved = inscriptionRepository.save(inscription);
        if (estComplet) {
            emailService.sendEmail(
                    parent.getEmail(),
                    "Liste d'attente",
                    "L'activité " + activite.getNom() + " est complète.\nVotre enfant est placé sur liste d'attente."
            );
        } else {
            envoyerEmailInscription(saved);
        }

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
    public InscriptionEnfant confirmerInscriptionAvecEmail(Long inscriptionId) {
        InscriptionEnfant inscription = confirmerInscription(inscriptionId);
        if (inscription.getUtilisateur() != null && inscription.getActivite() != null) {
            envoyerEmailInscription(inscription);
        }
        return inscription;
    }

    @Transactional
    public InscriptionEnfant annulerInscription(Long inscriptionId) {
        InscriptionEnfant inscription = getInscriptionById(inscriptionId);

        inscriptionRepository.delete(inscription);
        if (inscription.getEnfant() != null && inscription.getSession() != null) {
            communicationService.notifierAnnulation(inscription);
        }

        return inscription;
    }

    public boolean estInscrit(Integer enfantId, Integer sessionId) {
        return inscriptionRepository.findByEnfantIdAndSessionId(enfantId, sessionId).isPresent();
    }

    public InscriptionEnfant save(InscriptionEnfant inscription) {
        return inscriptionRepository.save(inscription);
    }

    @Transactional
    public void deleteById(Long id) {
        InscriptionEnfant inscription = getById(id);
        Long activiteId = inscription.getActivite() != null ? inscription.getActivite().getId() : null;

        inscriptionRepository.deleteById(id);

        if (activiteId == null) {
            return;
        }

        List<InscriptionEnfant> listeAttente =
                inscriptionRepository.findByActiviteIdAndEnAttenteTrueOrderByIdAsc(activiteId);

        if (!listeAttente.isEmpty()) {
            InscriptionEnfant next = listeAttente.get(0);
            next.setEnAttente(false);
            inscriptionRepository.save(next);
            envoyerEmailInscription(next);
        }
    }

    public long countInscrits(Long activiteId) {
        return inscriptionRepository.countByActiviteIdAndEnAttenteFalse(activiteId);
    }

    private void envoyerEmailInscription(InscriptionEnfant inscription) {
        if (inscription.getUtilisateur() == null || inscription.getActivite() == null) {
            return;
        }

        emailService.sendEmail(
                inscription.getUtilisateur().getEmail(),
                "Votre enfant est inscrit !",
                "Bonjour " + inscription.getUtilisateur().getNom() + ",\n\n"
                        + "Une place est confirmée pour l'activité "
                        + inscription.getActivite().getNom() + ".\n"
                        + "Votre enfant " + inscription.getNomEnfant()
                        + " est maintenant inscrit.\n"
                        + "Date de l'activité : " + inscription.getActivite().getDateDebut() + "\n\nMerci."
        );
    }
}

package com.manara.portail.service;

import com.manara.portail.model.InscriptionEnfant;
<<<<<<< HEAD
import com.manara.portail.model.Enfant;
import com.manara.portail.model.SessionParent;
import com.manara.portail.repository.InscriptionEnfantRepository;
import com.manara.portail.repository.EnfantRepository;
import com.manara.portail.repository.SessionParentRepository;
import com.manara.portail.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscriptionEnfantService {

    private final InscriptionEnfantRepository inscriptionRepository;
    private final EnfantRepository enfantRepository;
    private final SessionParentRepository sessionRepository;
    private final CommunicationService communicationService;

    public InscriptionEnfantService(InscriptionEnfantRepository inscriptionRepository,
                                    EnfantRepository enfantRepository,
                                    SessionParentRepository sessionRepository,
                                    CommunicationService communicationService) {
        this.inscriptionRepository = inscriptionRepository;
        this.enfantRepository = enfantRepository;
        this.sessionRepository = sessionRepository;
        this.communicationService = communicationService;
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
=======
import com.manara.portail.repository.InscriptionEnfantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscriptionEnfantService {
    @Autowired
    private InscriptionEnfantRepository inscriptionEnfantRepository;

    public List<InscriptionEnfant> getInscriptionsBySession(Integer sessionId) {
        return inscriptionEnfantRepository.findBySessionId(sessionId);
    }
}
>>>>>>> temporary-brench-manu

package com.manara.portail.service;

import com.manara.portail.model.InscriptionParent;
import com.manara.portail.model.EnfantParent;
import com.manara.portail.model.SessionParent;
import com.manara.portail.repository.InscriptionParentRepository;
import com.manara.portail.repository.EnfantParentRepository;
import com.manara.portail.repository.SessionParentRepository;
import com.manara.portail.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscriptionParentService {

    private final InscriptionParentRepository inscriptionRepository;
    private final EnfantParentRepository enfantRepository;
    private final SessionParentRepository sessionRepository;
    private final CommunicationParentService communicationService;

    public InscriptionParentService(InscriptionParentRepository inscriptionRepository,
                                    EnfantParentRepository enfantRepository,
                                    SessionParentRepository sessionRepository,
                                    CommunicationParentService communicationService) {
        this.inscriptionRepository = inscriptionRepository;
        this.enfantRepository = enfantRepository;
        this.sessionRepository = sessionRepository;
        this.communicationService = communicationService;
    }

    // ========== RECHERCHE DE BASE ==========

    public List<InscriptionParent> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }

    public InscriptionParent getInscriptionById(Integer id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée avec id: " + id));
    }

    // ========== RECHERCHE PAR SESSION ==========

    public List<InscriptionParent> getInscriptionsBySession(Integer sessionId) {
        return inscriptionRepository.findBySessionId(sessionId);
    }

    public List<InscriptionParent> getConfirmationsBySession(Integer sessionId) {
        return inscriptionRepository.findBySessionId(sessionId).stream()
                .filter(i -> "CONFIRMEE".equals(i.getStatut()))
                .collect(Collectors.toList());
    }

    public long getNombreConfirmationsBySession(Integer sessionId) {
        return inscriptionRepository.countConfirmationsBySession(sessionId);
    }

    public long getNombreTotalInscriptionsBySession(Integer sessionId) {
        return inscriptionRepository.countTotalBySession(sessionId);
    }

    public List<EnfantParent> getEnfantsConfirmesBySession(Integer sessionId) {
        return enfantRepository.findBySessionIdAndConfirmee(sessionId);
    }

    // ========== RECHERCHE PAR ENFANT ==========

    public List<InscriptionParent> getInscriptionsByEnfant(Integer enfantId) {
        return inscriptionRepository.findByEnfantId(enfantId);
    }

    public List<InscriptionParent> getConfirmationsByEnfant(Integer enfantId) {
        return inscriptionRepository.findByEnfantId(enfantId).stream()
                .filter(i -> "CONFIRMEE".equals(i.getStatut()))
                .collect(Collectors.toList());
    }

    public long getNombreInscriptionsByEnfant(Integer enfantId) {
        return inscriptionRepository.countByEnfantId(enfantId);
    }

    // ========== RECHERCHE PAR PARENT ==========

    public List<InscriptionParent> getInscriptionsByParent(Integer parentId) {
        return inscriptionRepository.findAllByParent(parentId);
    }

    public List<InscriptionParent> getInscriptionsFuturesParent(Integer parentId) {
        return inscriptionRepository.findFuturesByParent(parentId, LocalDate.now());
    }

    public List<InscriptionParent> getInscriptionsPasseesParent(Integer parentId) {
        return inscriptionRepository.findPassesByParent(parentId, LocalDate.now());
    }

    public List<InscriptionParent> getInscriptionsEnCours(Integer parentId) {
        return inscriptionRepository.findAllByParent(parentId).stream()
                .filter(i -> "CONFIRMEE".equals(i.getStatut()) &&
                        i.getSession().getDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public List<InscriptionParent> getInscriptionsAttente(Integer parentId) {
        return inscriptionRepository.findAttenteByParent(parentId);
    }

    // ========== LISTE D'ATTENTE ==========

    public List<InscriptionParent> getListeAttente() {
        return inscriptionRepository.findListeAttente();
    }

    public List<InscriptionParent> getListeAttenteBySession(Integer sessionId) {
        return inscriptionRepository.findListeAttenteBySession(sessionId);
    }

    public Integer getPositionAttente(Integer enfantId, Integer sessionId) {
        return inscriptionRepository.findByEnfantIdAndSessionId(enfantId, sessionId)
                .map(InscriptionParent::getPositionAttente)
                .orElse(null);
    }

    // ========== CRÉATION ET MODIFICATION ==========

    @Transactional
    public InscriptionParent inscrireEnfant(Integer enfantId, Integer sessionId, String commentaire) {
        EnfantParent enfant = enfantRepository.findById(enfantId)
                .orElseThrow(() -> new ResourceNotFoundException("Enfant non trouvé avec id: " + enfantId));

        SessionParent session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session non trouvée avec id: " + sessionId));

        if (inscriptionRepository.findByEnfantIdAndSessionId(enfantId, sessionId).isPresent()) {
            throw new RuntimeException("L'enfant est déjà inscrit à cette session");
        }

        // Vérifier l'âge
        int age = LocalDate.now().getYear() - enfant.getDateNaissance().getYear();
        if (enfant.getDateNaissance().plusYears(age).isAfter(LocalDate.now())) {
            age--;
        }

        if (age < session.getActivite().getAgeMin() || age > session.getActivite().getAgeMax()) {
            throw new RuntimeException("L'âge de l'enfant ne correspond pas aux critères de l'activité");
        }

        InscriptionParent inscription = new InscriptionParent(enfant, session);
        inscription.setCommentaireParent(commentaire);

        long nbInscrits = inscriptionRepository.countConfirmationsBySession(sessionId);
        if (nbInscrits < session.getCapaciteMax()) {
            inscription.setStatut("CONFIRMEE");
            inscription.setPositionAttente(0);
        } else {
            inscription.setStatut("EN_ATTENTE");
            int position = (int) (nbInscrits - session.getCapaciteMax() + 1);
            inscription.setPositionAttente(position);
        }

        InscriptionParent saved = inscriptionRepository.save(inscription);
        communicationService.envoyerConfirmationInscription(enfant, session, saved.getStatut());

        return saved;
    }

    @Transactional
    public InscriptionParent confirmerInscription(Integer inscriptionId) {
        InscriptionParent inscription = getInscriptionById(inscriptionId);

        if (!"EN_ATTENTE".equals(inscription.getStatut())) {
            throw new RuntimeException("Seules les inscriptions en attente peuvent être confirmées");
        }

        inscription.setStatut("CONFIRMEE");
        inscription.setPositionAttente(0);
        miseAJourListeAttente(inscription.getSession().getId());

        return inscriptionRepository.save(inscription);
    }

    @Transactional
    public InscriptionParent annulerInscription(Integer inscriptionId) {
        InscriptionParent inscription = getInscriptionById(inscriptionId);

        if ("ANNULEE".equals(inscription.getStatut())) {
            throw new RuntimeException("L'inscription est déjà annulée");
        }

        String ancienStatut = inscription.getStatut();
        inscription.setStatut("ANNULEE");

        if ("CONFIRMEE".equals(ancienStatut)) {
            gererListeAttenteApresAnnulation(inscription.getSession().getId());
        }

        InscriptionParent annulee = inscriptionRepository.save(inscription);
        communicationService.notifierAnnulation(inscription);

        return annulee;
    }

    @Transactional
    public void gererListeAttenteApresAnnulation(Integer sessionId) {
        List<InscriptionParent> attente = getListeAttenteBySession(sessionId);

        if (!attente.isEmpty()) {
            InscriptionParent prochain = attente.get(0);
            prochain.setStatut("CONFIRMEE");
            prochain.setPositionAttente(0);
            inscriptionRepository.save(prochain);
            communicationService.notifierPlaceDisponible(prochain);
            miseAJourListeAttente(sessionId);
        }
    }

    @Transactional
    public void miseAJourListeAttente(Integer sessionId) {
        List<InscriptionParent> attente = getListeAttenteBySession(sessionId);
        for (int i = 0; i < attente.size(); i++) {
            InscriptionParent insc = attente.get(i);
            insc.setPositionAttente(i + 1);
            inscriptionRepository.save(insc);
        }
    }

    // ========== STATISTIQUES ==========

    public long getNombreTotalInscriptions() {
        return inscriptionRepository.count();
    }

    public long getNombreInscriptionsParStatut(String statut) {
        return inscriptionRepository.countByStatut(statut);
    }

    public List<Object[]> getStatistiquesParMois() {
        return inscriptionRepository.getStatistiquesParMois();
    }

    public List<Object[]> getStatistiquesParActivite() {
        return inscriptionRepository.countInscriptionsParActivite();
    }

    public Double getMoyenneInscriptionsParSession() {
        return inscriptionRepository.getMoyenneInscriptionsParSession();
    }

    // ========== RECHERCHE AVANCÉE ==========

    public List<InscriptionParent> rechercherInscriptions(Integer enfantId, Integer sessionId,
                                                          String statut, LocalDateTime dateDebut,
                                                          LocalDateTime dateFin) {
        return inscriptionRepository.rechercherInscriptions(enfantId, sessionId, statut,
                dateDebut, dateFin);
    }

    // ========== VÉRIFICATIONS ==========

    public boolean estInscrit(Integer enfantId, Integer sessionId) {
        return inscriptionRepository.findByEnfantIdAndSessionId(enfantId, sessionId).isPresent();
    }

    // ========== POUR NOTIFICATIONS ==========

    public List<InscriptionParent> getInscriptionsPourRappel(LocalDate date) {
        return inscriptionRepository.findPourRappel(date);
    }

    public List<InscriptionParent> getProchainsListeAttente() {
        return inscriptionRepository.findProchainsListeAttente();
    }

    // ========== SUPPRESSION ==========

    @Transactional
    public void supprimerInscription(Integer id) {
        InscriptionParent inscription = getInscriptionById(id);
        inscriptionRepository.delete(inscription);
    }

    @Transactional
    public void supprimerInscriptionsAnciennes(LocalDate dateLimite) {
        inscriptionRepository.deleteAnciennesInscriptionsAnnulees(dateLimite);
    }
}
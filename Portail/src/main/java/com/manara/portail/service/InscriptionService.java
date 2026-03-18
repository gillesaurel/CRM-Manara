package com.manara.portail.service;

import com.manara.portail.model.Inscription;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.Session;
import com.manara.portail.repository.InscriptionRepository;
import com.manara.portail.repository.EnfantRepository;
import com.manara.portail.repository.SessionRepository;
import com.manara.portail.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EnfantRepository enfantRepository;
    private final SessionRepository sessionRepository;
    private final CommunicationService communicationService;

    public InscriptionService(InscriptionRepository inscriptionRepository,
                              EnfantRepository enfantRepository,
                              SessionRepository sessionRepository,
                              CommunicationService communicationService) {
        this.inscriptionRepository = inscriptionRepository;
        this.enfantRepository = enfantRepository;
        this.sessionRepository = sessionRepository;
        this.communicationService = communicationService;
    }

    // ========== RECHERCHE DE BASE ==========

    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }

    public Inscription getInscriptionById(Integer id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée avec id: " + id));
    }

    // ========== RECHERCHE PAR SESSION ==========

    public List<Inscription> getInscriptionsBySession(Integer sessionId) {
        return inscriptionRepository.findBySessionId(sessionId);
    }

    public List<Inscription> getConfirmationsBySession(Integer sessionId) {
        return inscriptionRepository.findConfirmationsBySessionId(sessionId);
    }

    public long getNombreConfirmationsBySession(Integer sessionId) {
        return inscriptionRepository.countConfirmationsBySession(sessionId);
    }

    public long getNombreTotalInscriptionsBySession(Integer sessionId) {
        return inscriptionRepository.countTotalBySession(sessionId);
    }

    public List<Enfant> getEnfantsConfirmesBySession(Integer sessionId) {
        return enfantRepository.findBySessionIdAndConfirmee(sessionId);
    }

    // ========== RECHERCHE PAR ENFANT ==========

    public List<Inscription> getInscriptionsByEnfant(Integer enfantId) {
        return inscriptionRepository.findByEnfantId(enfantId);
    }

    public List<Inscription> getConfirmationsByEnfant(Integer enfantId) {
        return inscriptionRepository.findConfirmationsByEnfant(enfantId);
    }

    public long getNombreInscriptionsByEnfant(Integer enfantId) {
        return inscriptionRepository.countByEnfantId(enfantId);
    }

    // ========== RECHERCHE PAR PARENT ==========

    public List<Inscription> getInscriptionsByParent(Integer parentId) {
        return inscriptionRepository.findAllByParent(parentId);
    }

    public List<Inscription> getInscriptionsFuturesParent(Integer parentId) {
        return inscriptionRepository.findFuturesByParent(parentId, LocalDate.now());
    }

    public List<Inscription> getInscriptionsPasseesParent(Integer parentId) {
        return inscriptionRepository.findPassesByParent(parentId, LocalDate.now());
    }

    public List<Inscription> getInscriptionsEnCours(Integer parentId) {
        // Inscriptions confirmées pour des sessions futures
        return inscriptionRepository.findAllByParent(parentId).stream()
                .filter(i -> i.getStatut().equals("CONFIRMEE") &&
                        i.getSession().getDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public List<Inscription> getInscriptionsAttente(Integer parentId) {
        return inscriptionRepository.findAttenteByParent(parentId);
    }

    // ========== LISTE D'ATTENTE ==========

    public List<Inscription> getListeAttente() {
        return inscriptionRepository.findListeAttente();
    }

    public List<Inscription> getListeAttenteBySession(Integer sessionId) {
        return inscriptionRepository.findListeAttenteBySession(sessionId);
    }

    public Integer getPositionAttente(Integer enfantId, Integer sessionId) {
        return inscriptionRepository.findByEnfantIdAndSessionId(enfantId, sessionId)
                .map(Inscription::getPositionAttente)
                .orElse(null);
    }

    // ========== CRÉATION ET MODIFICATION ==========

    @Transactional
    public Inscription inscrireEnfant(Integer enfantId, Integer sessionId, String commentaire) {
        Enfant enfant = enfantRepository.findById(enfantId)
                .orElseThrow(() -> new ResourceNotFoundException("Enfant non trouvé avec id: " + enfantId));

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session non trouvée avec id: " + sessionId));

        // Vérifier si déjà inscrit
        if (inscriptionRepository.findByEnfantIdAndSessionId(enfantId, sessionId).isPresent()) {
            throw new RuntimeException("L'enfant est déjà inscrit à cette session");
        }

        // Vérifier l'âge de l'enfant
        int age = LocalDate.now().getYear() - enfant.getDateNaissance().getYear();
        if (enfant.getDateNaissance().plusYears(age).isAfter(LocalDate.now())) {
            age--;
        }

        if (age < session.getActivite().getAgeMin() || age > session.getActivite().getAgeMax()) {
            throw new RuntimeException("L'âge de l'enfant ne correspond pas aux critères de l'activité");
        }

        Inscription inscription = new Inscription(enfant, session);
        inscription.setCommentaireParent(commentaire);

        // Vérifier la capacité
        long nbInscrits = inscriptionRepository.countConfirmationsBySession(sessionId);
        if (nbInscrits < session.getCapaciteMax()) {
            inscription.setStatut("CONFIRMEE");
            inscription.setPositionAttente(0);
        } else {
            inscription.setStatut("EN_ATTENTE");
            int position = (int) (nbInscrits - session.getCapaciteMax() + 1);
            inscription.setPositionAttente(position);
        }

        Inscription saved = inscriptionRepository.save(inscription);

        // Envoyer notification
        communicationService.envoyerConfirmationInscription(enfant, session, saved.getStatut());

        return saved;
    }

    @Transactional
    public Inscription confirmerInscription(Integer inscriptionId) {
        Inscription inscription = getInscriptionById(inscriptionId);

        if (!"EN_ATTENTE".equals(inscription.getStatut())) {
            throw new RuntimeException("Seules les inscriptions en attente peuvent être confirmées");
        }

        inscription.setStatut("CONFIRMEE");
        inscription.setPositionAttente(0);

        // Mettre à jour les positions des autres en attente
        miseAJourListeAttente(inscription.getSession().getId());

        return inscriptionRepository.save(inscription);
    }

    @Transactional
    public Inscription annulerInscription(Integer inscriptionId) {
        Inscription inscription = getInscriptionById(inscriptionId);

        if ("ANNULEE".equals(inscription.getStatut())) {
            throw new RuntimeException("L'inscription est déjà annulée");
        }

        String ancienStatut = inscription.getStatut();
        inscription.setStatut("ANNULEE");

        // Si c'était une confirmation, il faut gérer la liste d'attente
        if ("CONFIRMEE".equals(ancienStatut)) {
            gererListeAttenteApresAnnulation(inscription.getSession().getId());
        }

        Inscription annulee = inscriptionRepository.save(inscription);

        // Notifier le parent
        communicationService.notifierAnnulation(inscription);

        return annulee;
    }

    @Transactional
    public void gererListeAttenteApresAnnulation(Integer sessionId) {
        List<Inscription> attente = getListeAttenteBySession(sessionId);

        if (!attente.isEmpty()) {
            Inscription prochain = attente.get(0);
            prochain.setStatut("CONFIRMEE");
            prochain.setPositionAttente(0);
            inscriptionRepository.save(prochain);

            // Notifier le parent
            communicationService.notifierPlaceDisponible(prochain);

            // Mettre à jour les positions restantes
            miseAJourListeAttente(sessionId);
        }
    }

    @Transactional
    public void miseAJourListeAttente(Integer sessionId) {
        List<Inscription> attente = getListeAttenteBySession(sessionId);
        for (int i = 0; i < attente.size(); i++) {
            Inscription insc = attente.get(i);
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

    public List<Inscription> rechercherInscriptions(Integer enfantId, Integer sessionId,
                                                    String statut, LocalDateTime dateDebut,
                                                    LocalDateTime dateFin) {
        return inscriptionRepository.rechercherInscriptions(enfantId, sessionId, statut,
                dateDebut, dateFin);
    }

    public List<Inscription> getInscriptionsEntreDates(LocalDateTime debut, LocalDateTime fin) {
        return inscriptionRepository.findByDateInscriptionBetween(debut, fin);
    }

    public List<Inscription> getInscriptionsDuJour(LocalDate date) {
        return inscriptionRepository.findByDateInscription(date);
    }

    // ========== VÉRIFICATIONS ==========

    public boolean estInscrit(Integer enfantId, Integer sessionId) {
        return inscriptionRepository.findByEnfantIdAndSessionId(enfantId, sessionId).isPresent();
    }


    /**
     * Vérifie s'il y a un conflit d'horaire pour un enfant
     * @param enfantId ID de l'enfant
     * @param date Date de la session
     * @param heureDebut Heure de début (au format LocalDateTime)
     * @param heureFin Heure de fin (au format LocalDateTime)
     * @return true si conflit, false sinon
     */
    public boolean verifierConflitHoraire(Integer enfantId, LocalDate date,
                                          LocalDateTime heureDebut, LocalDateTime heureFin) {
        // Conversion des LocalDateTime en LocalTime pour la requête
        return inscriptionRepository.existsConflitHoraire(
                enfantId,
                date,
                heureDebut.toLocalTime(),  // ✅ Conversion en LocalTime
                heureFin.toLocalTime()      // ✅ Conversion en LocalTime
        );
    }

    // ========== POUR NOTIFICATIONS ==========

    public List<Inscription> getInscriptionsPourRappel(LocalDate date) {
        return inscriptionRepository.findPourRappel(date);
    }

    public List<Inscription> getProchainsListeAttente() {
        return inscriptionRepository.findProchainsListeAttente();
    }

    // ========== SUPPRESSION ==========

    @Transactional
    public void supprimerInscription(Integer id) {
        Inscription inscription = getInscriptionById(id);
        inscriptionRepository.delete(inscription);
    }

    @Transactional
    public void supprimerInscriptionsAnciennes(LocalDate dateLimite) {
        inscriptionRepository.deleteAnciennesInscriptionsAnnulees(dateLimite);
    }
}
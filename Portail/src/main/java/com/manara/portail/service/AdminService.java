package com.manara.portail.service;

import com.manara.portail.dto.IncidentDTO;
import com.manara.portail.model.*;
import com.manara.portail.repository.*;
import com.manara.portail.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final ActiviteRepository activiteRepository;
    private final SessionRepository sessionRepository;
    private final InscriptionRepository inscriptionRepository;
    private final ParentRepository parentRepository;
    private final EnfantRepository enfantRepository;
    private final CommunicationRepository communicationRepository;
    private final IncidentRepository incidentRepository;
    private final PresenceRepository presenceRepository;
    private final AnimationRepository animationRepository;

    public AdminService(ActiviteRepository activiteRepository,
                        SessionRepository sessionRepository,
                        InscriptionRepository inscriptionRepository,
                        ParentRepository parentRepository,
                        EnfantRepository enfantRepository,
                        CommunicationRepository communicationRepository,
                        IncidentRepository incidentRepository,
                        PresenceRepository presenceRepository,
                        AnimationRepository animationRepository) {
        this.activiteRepository = activiteRepository;
        this.sessionRepository = sessionRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.parentRepository = parentRepository;
        this.enfantRepository = enfantRepository;
        this.communicationRepository = communicationRepository;
        this.incidentRepository = incidentRepository;
        this.presenceRepository = presenceRepository;
        this.animationRepository = animationRepository;
    }

    // ========== MÉTHODES POUR LE HOME CONTROLLER ==========

    /**
     * Récupère le nombre de parents actifs
     */
    public long getNombreParentsActifs() {
        return parentRepository.countByActif(true);
    }

    /**
     * Récupère le nombre total d'enfants
     */
    public long getNombreEnfants() {
        return enfantRepository.count();
    }

    /**
     * Récupère le nombre de sessions futures
     */
    public long getNombreSessionsFutures() {
        return sessionRepository.countFuturesSessions(LocalDate.now());
    }

    // ========== TABLEAU DE BORD ==========

    public Map<String, Object> getTableauDeBord() {
        Map<String, Object> dashboard = new HashMap<>();

        // Statistiques générales
        dashboard.put("totalParents", parentRepository.count());
        dashboard.put("totalParentsActifs", parentRepository.countByActif(true));
        dashboard.put("totalEnfants", enfantRepository.count());
        dashboard.put("totalActivites", activiteRepository.count());
        dashboard.put("totalActivitesActives", activiteRepository.countByActiveTrue());
        dashboard.put("totalSessions", sessionRepository.count());
        dashboard.put("totalSessionsFutures", sessionRepository.countFuturesSessions(LocalDate.now()));
        dashboard.put("totalInscriptions", inscriptionRepository.count());
        dashboard.put("totalIncidents", incidentRepository.count());
        dashboard.put("totalIncidentsNonResolus", incidentRepository.countNonResolus());

        // Taux de remplissage
        List<Session> sessions = sessionRepository.findFuturesSessions(LocalDate.now());
        double tauxRemplissageMoyen = sessions.stream()
                .mapToDouble(s -> {
                    long inscrits = inscriptionRepository.countConfirmationsBySession(s.getId());
                    return s.getCapaciteMax() > 0 ? (double) inscrits / s.getCapaciteMax() * 100 : 0;
                })
                .average()
                .orElse(0.0);
        dashboard.put("tauxRemplissageMoyen", String.format("%.1f", tauxRemplissageMoyen) + "%");

        // Prochaines sessions (7 jours)
        dashboard.put("prochainesSessions",
                sessionRepository.findBetweenDates(LocalDate.now(),
                        LocalDate.now().plusDays(7)));

        // Activités populaires (top 5)
        dashboard.put("activitesPopulaires", activiteRepository.findTop5ByOrderByNbInscriptionsDesc());

        // Dernières inscriptions
        dashboard.put("dernieresInscriptions", inscriptionRepository.findTop10ByOrderByDateInscriptionDesc());

        // Incidents récents
        dashboard.put("incidentsRecents", incidentRepository.findTop10ByOrderByDateHeureDesc());

        return dashboard;
    }

    // ========== GESTION DES PARENTS ==========

    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    public Parent getParentParId(Integer id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé avec id: " + id));
    }

    public List<Parent> getParentsActifs() {
        return parentRepository.findAllActive();
    }

    // ========== GESTION DES ENFANTS ==========

    public List<Enfant> getAllEnfants() {
        return enfantRepository.findAll();
    }

    public Enfant getEnfantParId(Integer id) {
        return enfantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfant non trouvé avec id: " + id));
    }

    public List<Enfant> getEnfantsInscritsBySession(Integer sessionId) {
        return enfantRepository.findBySessionId(sessionId);
    }

    // ========== GESTION DES ANIMATEURS ==========

    public List<Animation> getAllAnimateurs() {
        return animationRepository.findAll();
    }

    public Animation getAnimateurParId(Integer id) {
        return animationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animateur non trouvé avec id: " + id));
    }

    public List<Animation> getAnimateursDisponibles(LocalDate date) {
        return animationRepository.findDisponiblesByDate(date);
    }

    // ========== GESTION DES PRÉSENCES ==========

    public List<Presence> getPresencesBySession(Integer sessionId) {
        return presenceRepository.findBySessionId(sessionId);
    }

    public List<Presence> getPresencesByDate(LocalDate date) {
        return presenceRepository.findByDateSaisie(date);
    }

    @Transactional
    public void enregistrerPresences(Integer sessionId, List<Integer> enfantsPresents) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session non trouvée"));

        List<Enfant> enfantsInscrits = enfantRepository.findBySessionId(sessionId);

        for (Enfant enfant : enfantsInscrits) {
            Presence presence = presenceRepository.findByEnfantAndSession(enfant, session)
                    .orElse(new Presence());

            presence.setEnfant(enfant);
            presence.setSession(session);
            presence.setPresent(enfantsPresents != null && enfantsPresents.contains(enfant.getId()));
            presence.setDateSaisie(LocalDateTime.now());

            presenceRepository.save(presence);
        }
    }

    // ========== GESTION DES INCIDENTS ==========

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public List<Incident> getIncidentsRecents() {
        return incidentRepository.findTop10ByOrderByDateHeureDesc();
    }

    public Incident getIncidentById(Integer id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident non trouvé avec id: " + id));
    }

    public List<Incident> getIncidentsByAnimateur(Integer animateurId) {
        return incidentRepository.findByAnimationId(animateurId);
    }

    public Map<String, Long> getStatistiquesIncidents() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", incidentRepository.count());
        stats.put("nonResolus", incidentRepository.countNonResolus());
        stats.put("graves", incidentRepository.countByGraviteGreaterThanEqual(4));
        stats.put("tresGraves", incidentRepository.countByGravite(5));
        return stats;
    }

    @Transactional
    public Incident creerIncident(IncidentDTO incidentDTO) {
        Incident incident = new Incident();
        incident.setType(incidentDTO.getType());
        incident.setDescription(incidentDTO.getDescription());
        incident.setGravite(incidentDTO.getGravite());
        incident.setDateHeure(LocalDateTime.now());
        incident.setStatut("SIGNALÉ");

        Enfant enfant = getEnfantParId(incidentDTO.getEnfantId());
        incident.setEnfant(enfant);

        if (incidentDTO.getAnimateurId() != null) {
            Animation animateur = getAnimateurParId(incidentDTO.getAnimateurId());
            incident.setAnimation(animateur);
        }

        return incidentRepository.save(incident);
    }

    @Transactional
    public void traiterIncident(Integer id, String actionsPrises, String statut) {
        Incident incident = getIncidentById(id);
        incident.setActionsPrises(actionsPrises);
        incident.setStatut(statut);
        incidentRepository.save(incident);
    }

    // ========== GESTION DES COMMUNICATIONS ==========

    public List<Communication> getAllCommunications() {
        return communicationRepository.findAll();
    }

    public Communication envoyerCommunicationATous(String sujet, String message, String canal) {
        Communication comm = new Communication(sujet, message, canal);
        return communicationRepository.save(comm);
    }

    public Communication envoyerCommunicationAParent(Integer parentId, String sujet,
                                                     String message, String canal) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé"));

        Communication comm = new Communication(sujet, message, canal);
        comm.setParent(parent);
        return communicationRepository.save(comm);
    }

    public List<Communication> getCommunicationsByAnimateur(Integer animateurId) {
        return communicationRepository.findByAnimationId(animateurId);
    }

    // ========== STATISTIQUES AVANCÉES ==========

    public Map<String, Object> getStatistiquesGlobales() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("evolutionInscriptions", inscriptionRepository.getStatistiquesParMois());
        stats.put("repartitionActivites", activiteRepository.getStatistiquesParType());
        stats.put("tauxRemplissageMoyen", getTauxRemplissageMoyen());
        stats.put("parentsParVille", parentRepository.getStatistiquesParVille());
        stats.put("enfantsParAge", enfantRepository.getStatistiquesParAge());

        return stats;
    }

    public Map<String, Object> getStatistiquesActivites() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("activitesPlusPopulaires", activiteRepository.findTop5ByOrderByNbInscriptionsDesc());
        stats.put("repartitionParAge", activiteRepository.getStatistiquesParAge());
        stats.put("tauxRemplissageParActivite", activiteRepository.getTauxRemplissageParActivite());

        return stats;
    }

    public Map<String, Object> getStatistiquesInscriptions() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("total", inscriptionRepository.count());
        stats.put("confirmees", inscriptionRepository.countByStatut("CONFIRMEE"));
        stats.put("enAttente", inscriptionRepository.countByStatut("EN_ATTENTE"));
        stats.put("annulees", inscriptionRepository.countByStatut("ANNULEE"));
        stats.put("moyenneParSession", inscriptionRepository.getMoyenneInscriptionsParSession());

        return stats;
    }

    private double getTauxRemplissageMoyen() {
        List<Session> sessions = sessionRepository.findFuturesSessions(LocalDate.now());
        return sessions.stream()
                .mapToDouble(s -> {
                    long inscrits = inscriptionRepository.countConfirmationsBySession(s.getId());
                    return s.getCapaciteMax() > 0 ? (double) inscrits / s.getCapaciteMax() * 100 : 0;
                })
                .average()
                .orElse(0.0);
    }

    // ========== GESTION DES RAPPORTS ==========

    public List<String> getRapportsDisponibles() {
        return List.of(
                "Rapport d'activités mensuel",
                "Rapport de fréquentation",
                "Rapport des incidents",
                "Rapport financier (gratuit)",
                "Liste des inscriptions"
        );
    }

    public String genererRapport(String type, String periode) {
        // Simulation de génération de rapport
        return "/rapports/" + type + "_" + periode + ".pdf";
    }
}
package com.manara.portail.service;

import com.manara.portail.dto.ActiviteDTO;
import com.manara.portail.dto.SessionDTO;
import com.manara.portail.model.ActiviteParent;
import com.manara.portail.model.SessionParent;
import com.manara.portail.repository.ActiviteParentRepository;
import com.manara.portail.repository.SessionParentRepository;
import com.manara.portail.repository.InscriptionEnfantRepository;
import com.manara.portail.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActiviteParentService {

    private final ActiviteParentRepository activiteRepository;
    private final SessionParentRepository sessionRepository;
    private final InscriptionEnfantRepository inscriptionRepository;

    public ActiviteParentService(ActiviteParentRepository activiteRepository,
                                 SessionParentRepository sessionRepository,
                                 InscriptionEnfantRepository inscriptionRepository) {
        this.activiteRepository = activiteRepository;
        this.sessionRepository = sessionRepository;
        this.inscriptionRepository = inscriptionRepository;
    }

    public List<ActiviteParent> getAllActivites() {
        return activiteRepository.findAll();
    }

    public List<ActiviteParent> getAllActivitesActives() {
        return activiteRepository.findAllActive();
    }

    public ActiviteParent getActiviteById(Integer id) {
        return activiteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activite non trouvee avec id: " + id));
    }

    @Transactional
    public ActiviteParent creerActivite(ActiviteDTO activiteDTO) {
        ActiviteParent activite = new ActiviteParent();
        activite.setNom(activiteDTO.getNom());
        activite.setDescription(activiteDTO.getDescription());
        activite.setAgeMin(activiteDTO.getAgeMin());
        activite.setAgeMax(activiteDTO.getAgeMax());
        activite.setPrix(activiteDTO.getPrix());
        activite.setActive(activiteDTO.isActive());
        return activiteRepository.save(activite);
    }

    @Transactional
    public ActiviteParent mettreAJourActivite(Integer id, ActiviteDTO activiteDTO) {
        ActiviteParent activite = getActiviteById(id);
        activite.setNom(activiteDTO.getNom());
        activite.setDescription(activiteDTO.getDescription());
        activite.setAgeMin(activiteDTO.getAgeMin());
        activite.setAgeMax(activiteDTO.getAgeMax());
        activite.setPrix(activiteDTO.getPrix());
        activite.setActive(activiteDTO.isActive());
        return activiteRepository.save(activite);
    }

    @Transactional
    public void supprimerActivite(Integer id) {
        ActiviteParent activite = getActiviteById(id);
        if (!activite.getSessions().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une activite qui a des sessions");
        }
        activiteRepository.delete(activite);
    }

    public List<SessionParent> getAllSessions() {
        return sessionRepository.findAll();
    }

    public SessionParent getSessionById(Integer id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session non trouvee avec id: " + id));
    }

    public List<SessionParent> getSessionsByActivite(Integer activiteId) {
        ActiviteParent activite = getActiviteById(activiteId);
        return sessionRepository.findByActivite(activite);
    }

    public List<SessionParent> getSessionsByDate(LocalDate date) {
        return sessionRepository.findByDate(date);
    }

    public List<SessionParent> getSessionsFuturesByActivite(Integer activiteId) {
        return sessionRepository.findFuturesByActivite(activiteId, LocalDate.now());
    }

    public List<SessionParent> getSessionsFuturesSessions() {
        return sessionRepository.findFuturesSessions(LocalDate.now());
    }

    public List<SessionParent> getSessionsByAnimateur(Integer animateurId) {
        return sessionRepository.findByAnimateurId(animateurId);
    }

    @Transactional
    public SessionParent ajouterSession(SessionDTO sessionDTO) {
        ActiviteParent activite = getActiviteById(sessionDTO.getActiviteId());

        SessionParent session = new SessionParent();
        session.setHeureDebut(sessionDTO.getHeureDebut());
        session.setHeureFin(sessionDTO.getHeureFin());
        session.setDate(sessionDTO.getDate());
        session.setCapaciteMax(sessionDTO.getCapaciteMax());
        session.setLieu(sessionDTO.getLieu());
        session.setStatut(sessionDTO.getStatut() != null ? sessionDTO.getStatut() : "PLANIFIEE");
        session.setActivite(activite);

        return sessionRepository.save(session);
    }

    @Transactional
    public SessionParent modifierSession(Integer id, SessionDTO sessionDTO) {
        SessionParent session = getSessionById(id);
        session.setHeureDebut(sessionDTO.getHeureDebut());
        session.setHeureFin(sessionDTO.getHeureFin());
        session.setDate(sessionDTO.getDate());
        session.setCapaciteMax(sessionDTO.getCapaciteMax());
        session.setLieu(sessionDTO.getLieu());
        session.setStatut(sessionDTO.getStatut());
        return sessionRepository.save(session);
    }

    @Transactional
    public void supprimerSession(Integer id) {
        SessionParent session = getSessionById(id);
        long nbInscriptions = inscriptionRepository.countTotalBySession(id);
        if (nbInscriptions > 0) {
            throw new RuntimeException("Impossible de supprimer une session qui a des inscriptions");
        }
        sessionRepository.delete(session);
    }

    public boolean checkPlaceDisponible(Integer sessionId) {
        SessionParent session = getSessionById(sessionId);
        long nbInscrits = inscriptionRepository.countConfirmationsBySession(sessionId);
        return nbInscrits < session.getCapaciteMax();
    }

    public int getNombrePlacesRestantes(Integer sessionId) {
        SessionParent session = getSessionById(sessionId);
        long nbInscrits = inscriptionRepository.countConfirmationsBySession(sessionId);
        return session.getCapaciteMax() - (int) nbInscrits;
    }

    public List<ActiviteParent> getActivitesAdapteesPourAge(int age) {
        return activiteRepository.findAdapteesPourAge(age);
    }

    public List<ActiviteParent> rechercherActivitesParNom(String nom) {
        return activiteRepository.rechercherParNom(nom);
    }

    public List<SessionParent> getAllSessionsFutures() {
        return sessionRepository.findFuturesSessions(LocalDate.now());
    }

    public List<SessionParent> getProchainesSessions(int limit) {
        return sessionRepository.findFuturesSessions(LocalDate.now())
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
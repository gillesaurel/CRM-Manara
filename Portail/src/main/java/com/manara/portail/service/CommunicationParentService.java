package com.manara.portail.service;

import com.manara.portail.model.CommunicationParent;
import com.manara.portail.model.Parent;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.model.EnfantParent;
import com.manara.portail.model.SessionParent;
import com.manara.portail.model.InscriptionParent;
import com.manara.portail.repository.CommunicationParentRepository;
import com.manara.portail.repository.ParentRepository;
import com.manara.portail.repository.UtilisateurRepository;
import com.manara.portail.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CommunicationParentService {

    private final CommunicationParentRepository communicationRepository;
    private final ParentRepository parentRepository;
    private final UtilisateurRepository utilisateurRepository;

    public CommunicationParentService(CommunicationParentRepository communicationRepository,
                                      ParentRepository parentRepository,
                                      UtilisateurRepository utilisateurRepository) {
        this.communicationRepository = communicationRepository;
        this.parentRepository = parentRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // ========== Envoi de messages ==========

    @Transactional
    public CommunicationParent envoyerMessageAParent(Integer parentId, String sujet, String message, String canal) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé avec id: " + parentId));

        CommunicationParent communication = new CommunicationParent(sujet, message, canal);
        communication.setParent(parent);
        communication.setDateEnvoi(LocalDateTime.now());
        communication.setLu(false);

        return communicationRepository.save(communication);
    }

    @Transactional
    public CommunicationParent envoyerMessageAAnimateur(Integer animateurId, String sujet, String message, String canal) {
        Utilisateur animateur = utilisateurRepository.findById(animateurId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Animateur non trouvé avec id: " + animateurId));

        CommunicationParent communication = new CommunicationParent(sujet, message, canal);
        communication.setAnimateur(animateur);
        communication.setDateEnvoi(LocalDateTime.now());
        communication.setLu(false);

        return communicationRepository.save(communication);
    }

    @Transactional
    public List<CommunicationParent> envoyerMessageATous(String sujet, String message, String canal) {
        List<Parent> tousLesParents = parentRepository.findAll();

        return tousLesParents.stream()
                .map(parent -> {
                    CommunicationParent comm = new CommunicationParent(sujet, message, canal);
                    comm.setParent(parent);
                    comm.setDateEnvoi(LocalDateTime.now());
                    comm.setLu(false);
                    return communicationRepository.save(comm);
                })
                .toList();
    }

    // ========== Notifications automatiques ==========

    @Transactional
    public void envoyerConfirmationInscription(EnfantParent enfant, SessionParent session, String statut) {
        String sujet = "Confirmation d'inscription";
        String message = String.format(
                "Bonjour %s %s,\n\n" +
                        "Votre enfant %s %s a été inscrit à l'activité '%s' le %s de %s à %s.\n" +
                        "Statut de l'inscription : %s\n\n" +
                        "Cordialement,\nL'équipe Manara",
                enfant.getParent().getlNom(),
                enfant.getParent().getpPrenom(),
                enfant.getPrenom(),
                enfant.getNom(),
                session.getActivite().getNom(),
                formatDate(session.getDate()),
                formatHeure(session.getHeureDebut()),
                formatHeure(session.getHeureFin()),
                statut
        );

        envoyerMessageAParent(enfant.getParent().getId(), sujet, message, "EMAIL");
    }

    @Transactional
    public void notifierPlaceDisponible(InscriptionParent inscription) {
        String sujet = "🎉 Place disponible - Votre enfant passe en liste principale";
        String message = String.format(
                "Bonjour %s %s,\n\n" +
                        "🎉 Bonne nouvelle ! Une place s'est libérée pour l'activité '%s' du %s.\n\n" +
                        "Votre enfant %s %s passe désormais en liste principale.\n\n" +
                        "Cordialement,\nL'équipe Manara",
                inscription.getEnfant().getParent().getlNom(),
                inscription.getEnfant().getParent().getpPrenom(),
                inscription.getSession().getActivite().getNom(),
                formatDate(inscription.getSession().getDate()),
                inscription.getEnfant().getPrenom(),
                inscription.getEnfant().getNom()
        );

        envoyerMessageAParent(inscription.getEnfant().getParent().getId(), sujet, message, "EMAIL");
    }

    @Transactional
    public void notifierAnnulation(InscriptionParent inscription) {
        String sujet = "❌ Annulation d'inscription";
        String message = String.format(
                "Bonjour %s %s,\n\n" +
                        "Nous vous confirmons l'annulation de l'inscription de votre enfant %s %s " +
                        "pour l'activité '%s' du %s.\n\n" +
                        "Cordialement,\nL'équipe Manara",
                inscription.getEnfant().getParent().getlNom(),
                inscription.getEnfant().getParent().getpPrenom(),
                inscription.getEnfant().getPrenom(),
                inscription.getEnfant().getNom(),
                inscription.getSession().getActivite().getNom(),
                formatDate(inscription.getSession().getDate())
        );

        envoyerMessageAParent(inscription.getEnfant().getParent().getId(), sujet, message, "EMAIL");
    }

    // ========== Gestion des messages ==========

    public List<CommunicationParent> getCommunicationsParent(Integer parentId) {
        return communicationRepository.findByParentId(parentId);
    }

    public List<CommunicationParent> getMessagesNonLusParent(Integer parentId) {
        return communicationRepository.findNonLuesByParent(parentId);
    }

    public long getNombreMessagesNonLusParent(Integer parentId) {
        return communicationRepository.countNonLuesByParent(parentId);
    }

    public List<CommunicationParent> getAllCommunications() {
        return communicationRepository.findAll();
    }

    public CommunicationParent getCommunicationById(Integer id) {
        return communicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Communication non trouvée avec id: " + id));
    }

    @Transactional
    public void marquerCommeLu(Integer id) {
        communicationRepository.marquerCommeLue(id);
    }

    @Transactional
    public void marquerToutesCommeLues(Integer parentId) {
        communicationRepository.marquerToutesCommeLues(parentId);
    }

    @Transactional
    public void supprimerCommunication(Integer id) {
        CommunicationParent communication = getCommunicationById(id);
        communicationRepository.delete(communication);
    }

    // ========== Méthodes privées ==========

    private String formatDate(LocalDate date) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private String formatHeure(LocalTime heure) {
        if (heure == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return heure.format(formatter);
    }
}
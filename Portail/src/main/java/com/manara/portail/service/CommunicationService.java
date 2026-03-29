package com.manara.portail.service;

import com.manara.portail.model.Communication;
import com.manara.portail.model.Parent;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.SessionParent;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.repository.CommunicationRepository;
import com.manara.portail.repository.ParentRepository;
import com.manara.portail.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CommunicationService {

    private final CommunicationRepository communicationRepository;
    private final ParentRepository parentRepository;

    public CommunicationService(CommunicationRepository communicationRepository,
                                ParentRepository parentRepository) {
        this.communicationRepository = communicationRepository;
        this.parentRepository = parentRepository;
    }

    @Transactional
    public Communication envoyerMessageAParent(Integer parentId, String sujet, String message, String canal) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouve avec id: " + parentId));

        Communication communication = new Communication(sujet, message, canal);
        communication.setParent(parent);
        communication.setDateEnvoi(LocalDateTime.now());
        communication.setLu(false);

        return communicationRepository.save(communication);
    }

    @Transactional
    public List<Communication> envoyerMessageATous(String sujet, String message, String canal) {
        List<Parent> tousLesParents = parentRepository.findAll();

        return tousLesParents.stream()
                .map(parent -> {
                    Communication comm = new Communication(sujet, message, canal);
                    comm.setParent(parent);
                    comm.setDateEnvoi(LocalDateTime.now());
                    comm.setLu(false);
                    return communicationRepository.save(comm);
                })
                .toList();
    }

    @Transactional
    public void envoyerConfirmationInscription(Enfant enfant, SessionParent session, String statut) {
        String sujet = "Confirmation d'inscription";
        String message = String.format(
                "Bonjour %s %s,\n\nVotre enfant %s %s a ete inscrit a l'activite '%s' le %s de %s a %s.\nStatut de l'inscription : %s\n\nCordialement,\nL'equipe Manara",
                enfant.getParent().getUtilisateur().getNom(),
                enfant.getParent().getUtilisateur().getPrenom(),
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
    public void notifierAnnulation(InscriptionEnfant inscription) {
        String sujet = "Annulation d'inscription";
        String message = String.format(
                "Bonjour %s %s,\n\nNous vous confirmons l'annulation de l'inscription de votre enfant %s %s pour l'activite '%s' du %s.\n\nCordialement,\nL'equipe Manara",
                inscription.getEnfant().getParent().getUtilisateur().getNom(),
                inscription.getEnfant().getParent().getUtilisateur().getPrenom(),
                inscription.getEnfant().getPrenom(),
                inscription.getEnfant().getNom(),
                inscription.getSession().getActivite().getNom(),
                formatDate(inscription.getSession().getDate())
        );

        envoyerMessageAParent(inscription.getEnfant().getParent().getId(), sujet, message, "EMAIL");
    }

    public List<Communication> getCommunicationsParent(Integer parentId) {
        return communicationRepository.findByParentId(parentId);
    }

    public List<Communication> getMessagesNonLusParent(Integer parentId) {
        return communicationRepository.findNonLuesByParent(parentId);
    }

    public long getNombreMessagesNonLusParent(Integer parentId) {
        return communicationRepository.countNonLuesByParent(parentId);
    }

    public List<Communication> getAllCommunications() {
        return communicationRepository.findAll();
    }

    public Communication getCommunicationById(Integer id) {
        return communicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Communication non trouvee avec id: " + id));
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
        Communication communication = getCommunicationById(id);
        communicationRepository.delete(communication);
    }

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
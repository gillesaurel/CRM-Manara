package com.manara.portail.service;

import com.manara.portail.model.Communication;
import com.manara.portail.model.Parent;
import com.manara.portail.model.Animation;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.Session;
import com.manara.portail.model.Inscription;
import com.manara.portail.repository.CommunicationRepository;
import com.manara.portail.repository.ParentRepository;
import com.manara.portail.repository.AnimationRepository;
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
    private final AnimationRepository animationRepository;

    public CommunicationService(CommunicationRepository communicationRepository,
                                ParentRepository parentRepository,
                                AnimationRepository animationRepository) {
        this.communicationRepository = communicationRepository;
        this.parentRepository = parentRepository;
        this.animationRepository = animationRepository;
    }

    // ========== Envoi de messages ==========

    @Transactional
    public Communication envoyerMessageAParent(Integer parentId, String sujet, String message, String canal) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé avec id: " + parentId));

        Communication communication = new Communication(sujet, message, canal);
        communication.setParent(parent);
        communication.setDateEnvoi(LocalDateTime.now());
        communication.setLu(false);

        return communicationRepository.save(communication);
    }

    @Transactional
    public Communication envoyerMessageAAnimateur(Integer animateurId, String sujet, String message, String canal) {
        Animation animateur = animationRepository.findById(animateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Animateur non trouvé avec id: " + animateurId));

        Communication communication = new Communication(sujet, message, canal);
        communication.setAnimation(animateur);
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

    // ========== Notifications automatiques ==========

    @Transactional
    public void envoyerConfirmationInscription(Enfant enfant, Session session, String statut) {
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
    public void notifierPlaceDisponible(Inscription inscription) {
        String sujet = "🎉 Place disponible - Votre enfant passe en liste principale";
        String message = String.format(
                "Bonjour %s %s,\n\n" +
                        "🎉 Bonne nouvelle ! Une place s'est libérée pour l'activité '%s' du %s.\n\n" +
                        "Votre enfant %s %s passe désormais en liste principale. " +
                        "Son inscription est maintenant confirmée.\n\n" +
                        "Détails de l'activité :\n" +
                        "• Activité : %s\n" +
                        "• Date : %s\n" +
                        "• Horaire : %s - %s\n" +
                        "• Lieu : %s\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Manara",
                inscription.getEnfant().getParent().getlNom(),
                inscription.getEnfant().getParent().getpPrenom(),
                inscription.getSession().getActivite().getNom(),
                formatDate(inscription.getSession().getDate()),
                inscription.getEnfant().getPrenom(),
                inscription.getEnfant().getNom(),
                inscription.getSession().getActivite().getNom(),
                formatDate(inscription.getSession().getDate()),
                formatHeure(inscription.getSession().getHeureDebut()),
                formatHeure(inscription.getSession().getHeureFin()),
                inscription.getSession().getLieu()
        );

        envoyerMessageAParent(inscription.getEnfant().getParent().getId(), sujet, message, "EMAIL");
    }

    /**
     * ✅ NOUVELLE MÉTHODE - Notifier l'annulation d'une inscription
     */
    @Transactional
    public void notifierAnnulation(Inscription inscription) {
        String sujet = "❌ Annulation d'inscription";
        String message = String.format(
                "Bonjour %s %s,\n\n" +
                        "Nous vous confirmons l'annulation de l'inscription de votre enfant %s %s " +
                        "pour l'activité '%s' du %s.\n\n" +
                        "Détails de l'activité annulée :\n" +
                        "• Activité : %s\n" +
                        "• Date : %s\n" +
                        "• Horaire : %s - %s\n" +
                        "• Lieu : %s\n\n" +
                        "Si cette annulation est à votre initiative, aucune autre action n'est requise.\n" +
                        "Si cette annulation est de notre fait, nous nous excusons pour la gêne occasionnée.\n\n" +
                        "Pour toute question, n'hésitez pas à nous contacter.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Manara",
                inscription.getEnfant().getParent().getlNom(),
                inscription.getEnfant().getParent().getpPrenom(),
                inscription.getEnfant().getPrenom(),
                inscription.getEnfant().getNom(),
                inscription.getSession().getActivite().getNom(),
                formatDate(inscription.getSession().getDate()),
                inscription.getSession().getActivite().getNom(),
                formatDate(inscription.getSession().getDate()),
                formatHeure(inscription.getSession().getHeureDebut()),
                formatHeure(inscription.getSession().getHeureFin()),
                inscription.getSession().getLieu()
        );

        envoyerMessageAParent(inscription.getEnfant().getParent().getId(), sujet, message, "EMAIL");
    }

    @Transactional
    public void envoyerRappelSession(Session session, List<Parent> parents) {
        String sujet = "⏰ Rappel : Activité demain";
        String message = String.format(
                "Bonjour,\n\n" +
                        "Nous vous rappelons que l'activité '%s' aura lieu demain %s de %s à %s.\n\n" +
                        "Lieu : %s\n\n" +
                        "Pensez à prévoir :\n" +
                        "• Tenue adaptée\n" +
                        "• Bouteille d'eau\n" +
                        "• Arriver 10 minutes avant le début\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Manara",
                session.getActivite().getNom(),
                formatDate(session.getDate()),
                formatHeure(session.getHeureDebut()),
                formatHeure(session.getHeureFin()),
                session.getLieu()
        );

        for (Parent parent : parents) {
            envoyerMessageAParent(parent.getId(), sujet, message, "EMAIL");
        }
    }

    @Transactional
    public void envoyerNotificationAnnulationSession(Session session, List<Parent> parents) {
        String sujet = "⚠️ Annulation de session";
        String message = String.format(
                "Bonjour,\n\n" +
                        "Nous sommes au regret de vous informer que la session de '%s' prévue le %s " +
                        "est annulée pour des raisons indépendantes de notre volonté.\n\n" +
                        "Nous vous recontacterons rapidement pour vous proposer une alternative.\n\n" +
                        "Nous vous prions de nous excuser pour la gêne occasionnée.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Manara",
                session.getActivite().getNom(),
                formatDate(session.getDate())
        );

        for (Parent parent : parents) {
            envoyerMessageAParent(parent.getId(), sujet, message, "EMAIL");
        }
    }

    @Transactional
    public void envoyerFacture(Parent parent, byte[] facturePdf) {
        String sujet = "📄 Votre facture Manara";
        String message = String.format(
                "Bonjour %s %s,\n\n" +
                        "Votre facture est disponible en pièce jointe.\n\n" +
                        "Merci pour votre confiance.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Manara",
                parent.getlNom(),
                parent.getpPrenom()
        );

        // Ici, vous pourriez ajouter la logique pour joindre le PDF
        envoyerMessageAParent(parent.getId(), sujet, message, "EMAIL");
    }

    // ========== Gestion des messages ==========

    public List<Communication> getCommunicationsParent(Integer parentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé"));
        return communicationRepository.findByParent(parent);
    }

    public List<Communication> getCommunicationsAnimateur(Integer animateurId) {
        Animation animateur = animationRepository.findById(animateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Animateur non trouvé"));
        return communicationRepository.findByAnimation(animateur);
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
                .orElseThrow(() -> new ResourceNotFoundException("Communication non trouvée avec id: " + id));
    }

    @Transactional
    public void marquerCommeLu(Integer id) {
        Communication communication = getCommunicationById(id);
        communication.setLu(true);
        communicationRepository.save(communication);
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

    // ========== Recherche et statistiques ==========

    public List<Communication> rechercherParSujet(String sujet) {
        return communicationRepository.rechercherParSujet(sujet);
    }

    public List<Communication> getCommunicationsEntreDates(LocalDateTime debut, LocalDateTime fin) {
        return communicationRepository.findBetweenDates(debut, fin);
    }

    public List<Communication> getCommunicationsGenerales() {
        return communicationRepository.findCommunicationsGenerales();
    }

    public List<Object[]> getStatistiquesCommunicationsParMois() {
        return communicationRepository.getStatistiquesCommunicationsParMois();
    }

    // ========== Méthodes utilitaires ==========

    @Transactional
    public void envoyerRappelInscription(Parent parent) {
        String sujet = "📢 N'oubliez pas d'inscrire vos enfants";
        String message = String.format(
                "Bonjour %s %s,\n\n" +
                        "De nouvelles activités sont disponibles. N'oubliez pas d'inscrire vos enfants !\n\n" +
                        "Rendez-vous sur votre espace parent pour découvrir toutes nos activités gratuites.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Manara",
                parent.getlNom(),
                parent.getpPrenom()
        );

        envoyerMessageAParent(parent.getId(), sujet, message, "EMAIL");
    }

    @Transactional
    public void envoyerBienvenue(Parent parent) {
        String sujet = "🎉 Bienvenue sur le Portail Manara";
        String message = String.format(
                "Bonjour %s %s,\n\n" +
                        "Bienvenue sur le portail Manara ! Nous sommes ravis de vous compter parmi nous.\n\n" +
                        "Vous pouvez maintenant :\n" +
                        "✅ Ajouter vos enfants\n" +
                        "✅ Consulter les activités disponibles\n" +
                        "✅ Inscrire gratuitement vos enfants aux activités\n" +
                        "✅ Recevoir nos notifications\n\n" +
                        "Pour commencer, rendez-vous sur votre tableau de bord.\n\n" +
                        "À très bientôt,\n" +
                        "L'équipe Manara",
                parent.getlNom(),
                parent.getpPrenom()
        );

        envoyerMessageAParent(parent.getId(), sujet, message, "EMAIL");
    }

    @Transactional
    public void envoyerMessageBienvenueAnimateur(Animation animateur) {
        String sujet = "🎉 Bienvenue dans l'équipe Manara";
        String message = String.format(
                "Bonjour %s %s,\n\n" +
                        "Bienvenue dans l'équipe Manara ! Nous sommes ravis de vous accueillir.\n\n" +
                        "Vous pouvez maintenant :\n" +
                        "✅ Consulter vos sessions\n" +
                        "✅ Gérer les présences\n" +
                        "✅ Signaler des incidents\n" +
                        "✅ Communiquer avec les parents\n\n" +
                        "Votre espace personnel est accessible dès maintenant.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe Manara",
                animateur.getlNom(),
                animateur.getpPrenom()
        );

        envoyerMessageAAnimateur(animateur.getId(), sujet, message, "EMAIL");
    }

    // ========== Méthodes privées utilitaires ==========

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
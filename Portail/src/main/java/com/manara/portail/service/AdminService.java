package com.manara.portail.service;

import com.manara.portail.model.Enfant;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.Parent;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AdminService {
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private InscriptionEnfantRepository inscriptionEnfantRepository;

    @Autowired
    private EnfantRepository enfantRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public void reinitialiserPassword(Long userId, String newPassword) throws Exception {
        if (newPassword == null || newPassword.length() < 8) {
            throw new Exception("Le mot de passe doit contenir au moins 8 caractères.");
        }
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new Exception("Utilisateur introuvable"));
        if (user.getRole().getNom().equals("ADMIN")) {
            throw new Exception("Impossible de modifier un admin");
        }
        user.setMotdepasse(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(user);
    }

    public void supprimerUtilisateur(Long id) throws Exception {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new Exception("Utilisateur introuvable"));
        if (user.getRole().getNom().equals("ADMIN")) {
            throw new Exception("Impossible de supprimer un admin");
        }

        // Supprimer toutes les inscriptions liées à cet utilisateur
        inscriptionEnfantRepository.deleteByUtilisateurId(id);

        // Trouver le parent lié s'il existe
        Optional<Parent> parentOpt = parentRepository.findByUtilisateurId(id);
        if (parentOpt.isPresent()) {
            Parent parent = parentOpt.get();

            // Supprimer les inscriptions de chaque enfant
            List<Enfant> enfants = enfantRepository.findByParentId(parent.getId());
            for (Enfant enfant : enfants) {
                List<InscriptionEnfant> inscriptions = inscriptionEnfantRepository.findByEnfantId(enfant.getId());
                inscriptionEnfantRepository.deleteAll(inscriptions);
            }

            // Supprimer les enfants
            enfantRepository.deleteAll(enfants);

            // Supprimer le parent
            parentRepository.deleteByUtilisateurId(id);
        }

        // Supprimer l'utilisateur
        utilisateurRepository.deleteById(id);
    }

    public Map<String, Object> rechercheToutLeMonde(String keyword, String roleFilter) {
        Map<String, Object> result = new HashMap<>();

        List<Utilisateur> utilisateurs;

        if (keyword == null || keyword.trim().isEmpty()) {
            utilisateurs = utilisateurRepository.findAll();
        } else {
            if (roleFilter == null || roleFilter.isEmpty()) {
                utilisateurs = adminRepository.rechercheUtilisateur(keyword);
            } else {
                utilisateurs = adminRepository.rechercheUtilisateurByRole(keyword, roleFilter);
            }
        }

        List<Utilisateur> parents = utilisateurs.stream()
                .filter(u -> u.getRole().getNom().equalsIgnoreCase("PARENT"))
                .toList();

        List<Utilisateur> animateurs = utilisateurs.stream()
                .filter(u -> u.getRole().getNom().equalsIgnoreCase("ANIMATEUR"))
                .toList();

        List<Enfant> enfant;
        if (keyword == null || keyword.trim().isEmpty()) {
            enfant = adminRepository.findAllEnfant();
        } else {
            enfant = adminRepository.rechercheEnfant(keyword);
        }

        result.put("parents", parents);
        result.put("animateurs", animateurs);
        result.put("enfant", enfant);

        return result;
    }

    // Envoyer un email à un arent
    public void envoyerEmailParent(Long id, String subject, String message) throws Exception {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new Exception("Parent introuvable"));

        emailService.sendEmail(user.getEmail(), subject, message);
    }

    // Envoyer un email à un animateur
    public void envoyerEmailAnimateur(Long id, String subject, String message) throws Exception {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new Exception("Animateur introuvable"));

        emailService.sendEmail(user.getEmail(), subject, message);
    }

    // Envoyer un email à un tous les parents
    public void envoyerEmailTousParents(String subject, String message) {
        List<Utilisateur> parents = adminRepository.findByRole("PARENT");

        for (Utilisateur u : parents) {
            emailService.sendEmail(u.getEmail(), subject, message);
        }
    }

    // Envoyer un email à un tous les animateurs
    public void envoyerEmailTousAnimateurs(String subject, String message) {
        List<Utilisateur> animateurs = adminRepository.findByRole("ANIMATEUR");

        for (Utilisateur u : animateurs) {
            emailService.sendEmail(u.getEmail(), subject, message);
        }
    }

    // Envoyer un email à un tout le monde
    public void envoyerEmailToutLeMonde(String subject, String message) {
        List<Utilisateur> users = utilisateurRepository.findAll();

        for (Utilisateur u : users) {
            emailService.sendEmail(u.getEmail(), subject, message);
        }
    }
}
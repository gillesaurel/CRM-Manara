package com.manara.portail.service;

import com.manara.portail.model.PasswordResetToken;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.PasswordResetTokenRepository;
import com.manara.portail.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void demanderReset(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("Aucun compte associé à cet email.");

        // Nettoyer les anciens tokens expirés
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());

        // Générer token UUID sécurisé
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        // Envoyer l'email
        String lien = "http://localhost:4200/reset-password?token=" + token;
        String message = "Bonjour " + user.getPrenom() + ",\n\n"
                + "Vous avez demandé la réinitialisation de votre mot de passe.\n\n"
                + "Cliquez sur ce lien (valide 30 minutes) :\n"
                + lien + "\n\n"
                + "Si vous n'avez pas fait cette demande, ignorez cet email.\n\n"
                + "L'équipe Manara";

        emailService.sendEmail(email, "Réinitialisation de mot de passe - Manara", message);
    }

    @Transactional
    public void reinitialiserMotDePasse(String token, String nouveauMotDePasse) {
        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 8) {
            throw new RuntimeException("Le mot de passe doit contenir au moins 8 caractères.");
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide."));

        if (resetToken.isExpire()) {
            throw new RuntimeException("Ce lien a expiré. Veuillez refaire une demande.");
        }

        Utilisateur user = resetToken.getUtilisateur();
        user.setMotdepasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(user);

        // Marquer le token comme utilisé
        resetToken.setUtilise(true);
        tokenRepository.save(resetToken);
    }
}

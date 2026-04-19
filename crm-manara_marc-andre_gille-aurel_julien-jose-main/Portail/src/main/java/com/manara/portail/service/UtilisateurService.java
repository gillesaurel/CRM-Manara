package com.manara.portail.service;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.RoleRepository;
import com.manara.portail.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UtilisateurService {
    @Autowired
    UtilisateurRepository utilisateurRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Utilisateur> afficherUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur enregistrer(Utilisateur utilisateur) throws Exception {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) != null) {
            throw new Exception("Email déjà utilisé !");
        }

        if (utilisateur.getConfirmMotdepasse() != null
                && !utilisateur.getConfirmMotdepasse().isBlank()
                && !utilisateur.getMotdepasse().equals(utilisateur.getConfirmMotdepasse())) {
            throw new Exception("Les mots de passe ne correspondent pas !");
        }

        utilisateur.setMotdepasse(passwordEncoder.encode(utilisateur.getMotdepasse()));

        Role roleParDefaut = roleRepository.findByNom("PARENT");
        if (roleParDefaut == null) {
            throw new Exception("Erreur : Le rôle PARENT n'existe pas.");
        }

        utilisateur.setRole(roleParDefaut);
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur getUtilisateurByEmailAndMotDePasse(String email, String password) {
        return utilisateurRepository.getUtilisateurByEmailAndMotDePasse(email, password);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public Integer getIdByEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        return utilisateur != null && utilisateur.getId() != null
                ? utilisateur.getId().intValue()
                : null;
    }

    public Utilisateur enregistrerParAdmin(Utilisateur utilisateur) throws Exception {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) != null) {
            throw new Exception("Email déjà utilisé !");
        }

        if (utilisateur.getRole() != null && utilisateur.getRole().getId() != null) {
            Role roleSelectionne = roleRepository.findById(utilisateur.getRole().getId().intValue())
                    .orElseThrow(() -> new Exception("Rôle introuvable"));
            utilisateur.setRole(roleSelectionne);
        } else {
            Role roleParDefaut = roleRepository.findByNom("PARENT");
            if (roleParDefaut == null) {
                throw new Exception("Erreur : Le rôle PARENT n'existe pas.");
            }
            utilisateur.setRole(roleParDefaut);
        }

        utilisateur.setMotdepasse(passwordEncoder.encode(utilisateur.getMotdepasse()));
        return utilisateurRepository.save(utilisateur);
    }
}

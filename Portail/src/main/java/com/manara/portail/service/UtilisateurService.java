// fait par Manu Charland-Rivard

package com.manara.portail.service;

import com.manara.portail.model.Parent;
import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.ParentRepository;
import com.manara.portail.repository.RoleRepository;
import com.manara.portail.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ParentRepository parentRepository;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public List<Utilisateur> afficherUtilisateurs(){
        return utilisateurRepository.findAll();
    }

    public Utilisateur enregistrer(Utilisateur utilisateur) throws Exception {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) != null) {
            throw new Exception("Email déjà utilisé !");
        }

        // NE PAS réencoder le mot de passe (déjà fait dans AuthController)
        // utilisateur.setMotdepasse(passwordEncoder.encode(utilisateur.getMotdepasse()));

        Role roleParDefaut = roleRepository.findByNom("PARENT");
        if (roleParDefaut == null) {
            throw new Exception("Erreur : Le rôle PARENT n'existe pas.");
        }
        utilisateur.setRole(roleParDefaut);

        // 1. Sauvegarder l'utilisateur d'abord
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);

        // 2. Créer le parent si nécessaire
        if (savedUtilisateur.getRole().getNom().equals("PARENT")) {
            Parent parent = new Parent(savedUtilisateur);
            parentRepository.save(parent);
        }

        return savedUtilisateur;
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Transactional
    public Utilisateur enregistrerParAdmin(Utilisateur utilisateur) {
        Utilisateur existeDeja = utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (existeDeja != null && utilisateur.getId() != null && !existeDeja.getId().equals(utilisateur.getId())) {
            throw new RuntimeException("Email déjà utilisé !");
        }

        if (utilisateur.getRole() != null && utilisateur.getRole().getId() != null) {
            Role roleSelectionne = roleRepository.findById(utilisateur.getRole().getId().intValue())
                    .orElseThrow(() -> new RuntimeException("Rôle introuvable"));
            utilisateur.setRole(roleSelectionne);
        } else {
            Role roleParDefaut = roleRepository.findByNom("PARENT");
            utilisateur.setRole(roleParDefaut);
        }

        utilisateur.setMotdepasse(passwordEncoder.encode(utilisateur.getMotdepasse()));

        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);

        if (savedUtilisateur.getRole().getNom().equals("PARENT")) {
            Parent parent = new Parent(savedUtilisateur);
            parentRepository.save(parent);
        }

        return savedUtilisateur;
    }

    public Integer getIdByEmail(String email) {
        Utilisateur u = utilisateurRepository.findByEmail(email);
        return u != null ? u.getId().intValue() : null;
    }
}
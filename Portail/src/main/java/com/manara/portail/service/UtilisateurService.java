// fait par Manu Charland-Rivard

package com.manara.portail.service;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
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
    UtilisateurRepository utilisateurRepository;   //     UtilisateurRepository repos = new  UtilisateurRepository ()
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public List<Utilisateur> afficherUtilisateurs(){
        return utilisateurRepository.findAll();
    }

    public Utilisateur enregistrer(Utilisateur utilisateur)throws Exception {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) != null) {
            throw new Exception("Email déjà utilisé !");
        }

        if (!utilisateur.getMotdepasse().equals(utilisateur.getConfirmMotdepasse())) {
            throw new Exception("Les mots de passe ne correspondent pas !");
        }

        // Encode le mot de passe avant de sauvegarder
        utilisateur.setMotdepasse(passwordEncoder.encode(utilisateur.getMotdepasse()));

        Role roleParDefaut = roleRepository.findByNom("PARENT");

        if (roleParDefaut == null) {
            throw new Exception("Erreur : Le rôle PARENT n'existe pas.");
        }

        utilisateur.setRole(roleParDefaut);

        return utilisateurRepository.save(utilisateur);
    }

    /*public Utilisateur getUtilisateurByEmailAndMotDePasse(String email, String password){
        return utilisateurRepository.getUtilisateurByEmailAndMotDePasse(email,password);}
     */

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
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
            utilisateur.setRole(roleParDefaut);
        }

        utilisateur.setMotdepasse(passwordEncoder.encode(utilisateur.getMotdepasse()));

        return utilisateurRepository.save(utilisateur);
    }


}

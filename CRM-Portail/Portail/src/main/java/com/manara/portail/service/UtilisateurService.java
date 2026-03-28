// fait par Manu Charland-Rivard

package com.manara.portail.service;

import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UtilisateurService {
    @Autowired
    UtilisateurRepository utilisateurRepository;   //     UtilisateurRepository repos = new  UtilisateurRepository ()



    public List<Utilisateur> afficherUtilisateurs(){
        return ( List<Utilisateur> ) utilisateurRepository.findAll();
    }

    public Utilisateur enregistrer(Utilisateur utilisateur)throws Exception {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) != null) {
            throw new Exception("Email déjà utilisé !");
        }
        return utilisateurRepository.save(utilisateur);
    }
    public Utilisateur getUtilisateurByEmailAndMotDePasse(String email, String password){
        return utilisateurRepository.getUtilisateurByEmailAndMotDePasse(email,password);}




    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

}

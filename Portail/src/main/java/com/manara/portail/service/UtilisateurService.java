package com.manara.portail.service;

import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UtilisateurService {
    @Autowired
    UtilisateurRepository utilisateurRepository;   //     UtilisateurRepository repos = new  UtilisateurRepository ()


    public List<Utilisateur> afficherUtilisateurs(){
        return ( List<Utilisateur> ) utilisateurRepository.findAll();
    }

    public Utilisateur enregistrer(Utilisateur utilisateur){
        return utilisateurRepository.save(utilisateur);
    }



}

package com.manara.portail.service;

import com.manara.portail.model.Utilisateur;
import com.manara.portail.repos.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;


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

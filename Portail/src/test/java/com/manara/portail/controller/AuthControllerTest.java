package com.manara.portail.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;



@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;    
    @Autowired
    private UtilisateurService UtilService;
    @Autowired
    private RoleService RoleService;

    private Utilisateur utilisateur = new Utilisateur("Prenom", "Nom", "5555555555", "email@test.com", "motdepasse")


    @Test
    public void enregistrerUtilisateur() {
        utilisateur.setRole(RoleService.findByName("PARENT"));
        UtilService.enregistrer(utilisateur);

        return "redirect:/";
    }
}

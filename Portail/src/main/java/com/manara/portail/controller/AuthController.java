package com.manara.portail.controller;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.service.RoleService;
import com.manara.portail.service.UtilisateurNotFoundException;
import com.manara.portail.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.mvc.support.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class AuthController {

    @Autowired
    UtilisateurService UtilService;

    @Autowired
    private RoleService roleService;

    // PAGE INSCRIPTION UTILISATEUR
    @GetMapping("/register")
    public String pageInscription(Model model) {
        model.addAttribute("utilisateur",new Utilisateur());
        return "inscription";
    }

    // REDIRECTION INSCRIPTION UTILISATEUR
    @PostMapping("/register/save")
    public String enregistrerUtilisateur(
            @ModelAttribute("utilisateur") Utilisateur utilisateur,
            @RequestParam(value = "fileImage", required = false) MultipartFile fileImage,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        try {

            // Save user
            UtilService.enregistrer(utilisateur);

            if (utilisateur.getRole() == null) {
                // Example: role_id = 2 for "USER"
                Role roleUtilisateur = roleService.findByName("PARENT").orElse(null); // PARENT role must exist in DB
                utilisateur.setRole(roleUtilisateur);
            }

            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/register";
        }
    }


@GetMapping("/login")
    public String pageConnexion(){
        return "connexion";
    }
}

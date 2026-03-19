// FAIT PAR MANU, GILLES ET JOSE

package com.manara.portail.controller;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.service.RoleService;
import com.manara.portail.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.Cookie;

import java.io.IOException;


@Controller
public class AuthController {

    @Autowired
    UtilisateurService UtilService;

    @Autowired
    RoleService RoleService;

    // PAGE INSCRIPTION UTILISATEUR
    @GetMapping("/register")
    public String pageInscription(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
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

            utilisateur.setRole(RoleService.findByName("PARENT"));
            UtilService.enregistrer(utilisateur);

            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/register";
        }
    }

    // PAGE CONNEXION UTILISATEUR
    @GetMapping("/login")
    public String pageConnexion(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "connexion";
    }


    @PostMapping("/login/save")
    public String connecterUtilisateur(
            @ModelAttribute("utilisateur") Utilisateur utilisateur,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        try {

            Utilisateur tempUtilisateur = UtilService.connecter(
                    utilisateur.getEmail(),
                    utilisateur.getMotdepasse()
            );

            if (if tempUtilisateur != null) {

                response.createCookie("email",utilisateur.getEmail());
                response.createCookie("role", utilisateur.getRole());
                response.createCookie("prenom", utilisateur.getPrenom());
                response.createCookie("nom", utilisateur.getNom());

                return "redirect:/";
            }

            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/login";
        }
    }

    // creer un cookie utilisateur lorsque l'utilisateur se connecte
    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setHttpOnly(true);
        return cookie;
    }

   
}

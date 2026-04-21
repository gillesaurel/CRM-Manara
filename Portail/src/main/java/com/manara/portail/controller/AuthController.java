// FAIT PAR MANU, GILLES ET JOSE

package com.manara.portail.controller;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.service.RoleService;
import com.manara.portail.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@Controller
@CrossOrigin(
        origins = {"http://localhost:4200", "http://127.0.0.1:4200", "http://manara.histoiredurock.com", "https://manara.histoiredurock.com"},
        allowCredentials = "true"
)
public class AuthController {

    @Autowired
    RoleService RoleService;

    @Autowired
    private UtilisateurService utilisateurService;

    public AuthController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    ) {
        try {
            utilisateurService.enregistrer(utilisateur);
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

/*
    @PostMapping("/login/save")
    public String connecterUtilisateur(
            @ModelAttribute("utilisateur") Utilisateur utilisateur,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response
    ) throws IOException {

        Utilisateur tempUtilisateur = UtilService.getUtilisateurByEmailAndMotDePasse(
                utilisateur.getEmail(),
                utilisateur.getMotdepasse()
        );
        System.out.println("email : " + utilisateur.getEmail());
        System.out.println("mdp : " + utilisateur.getMotdepasse());

        try {


            System.out.println("tempUtilisateur : " + tempUtilisateur.getEmail());


            if (tempUtilisateur != null) {

                response.addCookie(createCookie("email",tempUtilisateur.getEmail()));
                response.addCookie(createCookie("role", tempUtilisateur.getRole().getNom()));
                response.addCookie(createCookie("prenom", tempUtilisateur.getPrenom()));
                response.addCookie(createCookie("nom", tempUtilisateur.getNom()));

                return "redirect:/";
            }

            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/login";
        }
    }*/

    @GetMapping("/deconnexion")
    public String pageConnexion(Model model, HttpServletResponse response) {
        model.addAttribute("utilisateur", new Utilisateur());

        response.addCookie(deleteCookie("email"));
        response.addCookie(deleteCookie("role"));
        response.addCookie(deleteCookie("prenom"));
        response.addCookie(deleteCookie("nom"));

        return "index";
    }

    // creer un cookie utilisateur lorsque l'utilisateur se connecte
    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        return cookie;
    }

    @GetMapping("/userConnecte")
    @ResponseBody
    public Utilisateur getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        String email = authentication.getName();
        return utilisateurService.findByEmail(email);
    }

}

package com.manara.portail.controller;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.service.RoleService;
import com.manara.portail.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // PAGE INSCRIPTION
    @GetMapping("/register")
    public String pageInscription(Model model,
<<<<<<< HEAD
                                  @CookieValue(name = "role", required = false) String role) {
=======
                                  @CookieValue(name = "role" , required = false) String role) {
>>>>>>> temporary-brench-manu
        model.addAttribute("role", role);
        model.addAttribute("utilisateur", new Utilisateur());
        return "inscription";
    }

    // TRAITEMENT INSCRIPTION
    @PostMapping("/register/save")
    public String enregistrerUtilisateur(
            @ModelAttribute("utilisateur") Utilisateur utilisateur,
            @RequestParam(value = "fileImage", required = false) MultipartFile fileImage,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        try {
            // Vérifier si l'email existe déjà
            if (utilisateurService.findByEmail(utilisateur.getEmail()) != null) {
                redirectAttributes.addFlashAttribute("message", "Cet email est déjà utilisé");
                return "redirect:/register";
            }

            // Encoder le mot de passe
            utilisateur.setMotdepasse(passwordEncoder.encode(utilisateur.getMotdepasse()));

            // Assigner le rôle PARENT par défaut
            Role roleParent = roleService.findByName("PARENT");
            if (roleParent == null) {
                throw new RuntimeException("Le rôle PARENT n'existe pas en base de données");
            }
            utilisateur.setRole(roleParent);

            // Sauvegarder l'utilisateur
            utilisateurService.enregistrer(utilisateur);

            redirectAttributes.addFlashAttribute("success", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erreur lors de l'inscription : " + e.getMessage());
            return "redirect:/register";
        }
    }

    // PAGE CONNEXION (Spring Security gère le POST)
    @GetMapping("/login")
    public String pageConnexion() {
        return "connexion";
    }

<<<<<<< HEAD
    // DÉCONNEXION
    @GetMapping("/deconnexion")
    public String deconnexion() {
        return "redirect:/login?logout=true";
    }
}
=======

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
                response.addCookie(createCookie("id", tempUtilisateur.getId().toString()));
                return "redirect:/";
            }

            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/deconnexion")
    public String pageConnexion(Model model, HttpServletResponse response) {
        model.addAttribute("utilisateur", new Utilisateur());

        response.addCookie(deleteCookie("email"));
        response.addCookie(deleteCookie("role"));
        response.addCookie(deleteCookie("prenom"));
        response.addCookie(deleteCookie("nom"));
        response.addCookie(deleteCookie("id"));

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
        cookie.setMaxAge(-1);
        cookie.setHttpOnly(true);
        return cookie;
    }
   
}
>>>>>>> temporary-brench-manu

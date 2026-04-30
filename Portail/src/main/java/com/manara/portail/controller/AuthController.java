// FAIT PAR MANU, GILLES ET JOSE

package com.manara.portail.controller;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.service.RoleService;
import com.manara.portail.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import java.util.HashMap;
import java.util.Map;

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

    // API INSCRIPTION UTILISATEUR (pour Angular)
    @PostMapping("/register/save")
    @ResponseBody
    public ResponseEntity<Map<String, String>> enregistrerUtilisateur(@RequestBody Map<String, String> userData) {
        Map<String, String> response = new HashMap<>();
        try {
            String email = userData.get("email");
            String password = userData.get("motdepasse");

            String confirmPassword = userData.get("confirmMotdepasse");

            if (password == null || !password.equals(confirmPassword)) {
                response.put("message", "Les mots de passe ne correspondent pas !");
                return ResponseEntity.badRequest().body(response);
            }

            System.out.println("=== TENTATIVE D'INSCRIPTION ===");
            System.out.println("Email: " + email);
            System.out.println("Données reçues: " + userData);

            // Vérifier si l'email existe déjà
            if (utilisateurService.findByEmail(email) != null) {
                response.put("message", "Cet email est déjà utilisé");
                return ResponseEntity.badRequest().body(response);
            }

            // Créer l'utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPrenom(userData.get("prenom"));
            utilisateur.setNom(userData.get("nom"));
            utilisateur.setTelephone(userData.get("telephone"));
            utilisateur.setEmail(email);
            utilisateur.setMotdepasse(passwordEncoder.encode(password));

            // Assigner le rôle PARENT par défaut
            Role roleParent = RoleService.findByName("PARENT");
            if (roleParent == null) {
                roleParent = new Role();
                roleParent.setNom("PARENT");
                RoleService.save(roleParent);
            }
            utilisateur.setRole(roleParent);

            utilisateurService.enregistrer(utilisateur);

            response.put("message", "Inscription réussie");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("=== ERREUR INSCRIPTION ===");
            e.printStackTrace();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // PAGE CONNEXION UTILISATEUR
    @GetMapping("/login")
    public String pageConnexion(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "connexion";
    }

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
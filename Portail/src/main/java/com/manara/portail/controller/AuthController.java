// FAIT PAR MANU, GILLES ET JOSE
// MODIFIÉ POUR INCLURE LES CHAMPS PARENT

package com.manara.portail.controller;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.service.RoleService;
import com.manara.portail.service.UtilisateurService;
import com.manara.portail.service.ParentService;
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
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Controller
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ParentService parentService;

    // PAGE INSCRIPTION UTILISATEUR
    @GetMapping("/register")
    public String pageInscription(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "inscription";
    }

    // TRAITEMENT INSCRIPTION UTILISATEUR
    @PostMapping("/register/save")
    public String enregistrerUtilisateur(
            @ModelAttribute("utilisateur") Utilisateur utilisateur,
            @RequestParam(value = "fileImage", required = false) MultipartFile fileImage,
            @RequestParam(value = "adresse", required = false) String adresse,
            @RequestParam(value = "ville", required = false) String ville,
            @RequestParam(value = "codePostal", required = false) String codePostal,
            @RequestParam(value = "typeLien", required = false) String typeLien,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        try {
            // Remplir les champs parent
            utilisateur.setAdresse(adresse);
            utilisateur.setVille(ville);
            utilisateur.setCodePostal(codePostal);
            utilisateur.setTypeLien(typeLien);

            // Définir le rôle PARENT
            Role parentRole = roleService.findByName("PARENT");
            if (parentRole == null) {
                throw new Exception("Le rôle PARENT n'existe pas. Veuillez contacter l'administrateur.");
            }
            utilisateur.setRole(parentRole);

            // Enregistrer l'utilisateur
            utilisateurService.enregistrer(utilisateur);

            // Créer également l'entité Parent associée (pour la partie parent)
            try {
                com.manara.portail.dto.ParentDTO parentDTO = new com.manara.portail.dto.ParentDTO();
                parentDTO.setNom(utilisateur.getNom());
                parentDTO.setPrenom(utilisateur.getPrenom());
                parentDTO.setEmail(utilisateur.getEmail());
                parentDTO.setTelephone(utilisateur.getTelephone());
                parentDTO.setMotDePasse(utilisateur.getMotdepasse());
                parentDTO.setAdresse(utilisateur.getAdresse());
                parentDTO.setVille(utilisateur.getVille());
                parentDTO.setCodePostal(utilisateur.getCodePostal());
                parentDTO.setTypeLien(utilisateur.getTypeLien());
                parentService.inscrireParent(parentDTO);
            } catch (Exception e) {
                // L'utilisateur est déjà créé, on continue
                System.err.println("Erreur création parent: " + e.getMessage());
            }

            redirectAttributes.addFlashAttribute("message", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "redirect:/login";

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

    // TRAITEMENT CONNEXION UTILISATEUR
    @PostMapping("/login/save")
    public String connecterUtilisateur(
            @ModelAttribute("utilisateur") Utilisateur utilisateur,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response
    ) throws IOException {
        try {
            Utilisateur tempUtilisateur = utilisateurService.getUtilisateurByEmailAndMotDePasse(
                    utilisateur.getEmail(),
                    utilisateur.getMotdepasse()
            );

            if (tempUtilisateur != null) {
                response.addCookie(createCookie("email", tempUtilisateur.getEmail()));
                response.addCookie(createCookie("role", tempUtilisateur.getRole().getNom()));
                response.addCookie(createCookie("prenom", tempUtilisateur.getPrenom()));
                response.addCookie(createCookie("nom", tempUtilisateur.getNom()));

                // Redirection selon le rôle
                if ("PARENT".equals(tempUtilisateur.getRole().getNom())) {
                    return "redirect:/parent/dashboard";
                } else if ("ADMIN".equals(tempUtilisateur.getRole().getNom())) {
                    return "redirect:/admin/administrateur";
                } else if ("ANIMATEUR".equals(tempUtilisateur.getRole().getNom())) {
                    return "redirect:/animateur";
                }
                return "redirect:/";
            }

            redirectAttributes.addFlashAttribute("message", "Email ou mot de passe incorrect");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/login";
        }
    }

    // Créer un cookie utilisateur
    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7); // 7 jours
        cookie.setHttpOnly(true);
        return cookie;
    }
}
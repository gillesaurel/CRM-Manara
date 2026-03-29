package com.manara.portail.controller;

import com.manara.portail.service.ActiviteParentService;
import com.manara.portail.service.InscriptionEnfantService;
import com.manara.portail.service.ParentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    // ========== AJOUT : Déclaration des services parent ==========
    private final ActiviteParentService activiteService;
    private final ParentService parentService;
    private final InscriptionEnfantService inscriptionService;

    // ========== AJOUT : Constructeur modifié ==========
    public HomeController(ActiviteParentService activiteService,
                          ParentService parentService,
                          InscriptionEnfantService inscriptionService) {
        this.activiteService = activiteService;
        this.parentService = parentService;
        this.inscriptionService = inscriptionService;
    }

    @GetMapping("/")
    public String pageAccueil(HttpServletRequest request, Model model) {

        getCookies(request, model, "role");

        // ========== AJOUT : Affichage des activités populaires ==========
        model.addAttribute("activitesPopulaires",
                activiteService.getAllActivitesActives().stream().limit(8).toList());

        // ========== AJOUT : Statistiques pour la page d'accueil ==========
        Map<String, Object> stats = new HashMap<>();
        stats.put("nbParents", parentService.getNombreParents());
        stats.put("nbEnfants", parentService.getAllEnfants().size());
        stats.put("nbActivites", activiteService.getAllActivitesActives().size());
        stats.put("nbSessions", activiteService.getAllSessions().size());
        model.addAttribute("stats", stats);

        return "index";
    }

    // ========== AJOUT : Liste des activités publiques ==========
    @GetMapping("/activites")
    public String listeActivites(Model model) {
        model.addAttribute("activites", activiteService.getAllActivitesActives());
        return "activites";
    }


    // ========== AJOUT : Pages statiques ==========
    @GetMapping("/apropos")
    public String apropos() {
        return "apropos";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/mentions-legales")
    public String mentionsLegales() {
        return "mentions-legales";
    }

    @GetMapping("/confidentialite")
    public String confidentialite() {
        return "confidentialite";
    }

    private void getCookies(HttpServletRequest request, Model model, String cookieName) {

        // Get cookies from the request
        Cookie[] cookies = request.getCookies();

        String cookieValue = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Example: Check for a cookie named "userSession"
                if (cookieName.equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;  // Exit once we find the cookie
                }
            }
        }
        System.out.println("cookieValue: " + cookieValue);
        // Add the cookie value to the model, so it can be used in Thymeleaf
        model.addAttribute("cookieValue", cookieValue);
    }
}
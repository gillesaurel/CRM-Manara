package com.manara.portail.controller;

import com.manara.portail.dto.ParentDTO;
import com.manara.portail.service.ActiviteParentService;
import com.manara.portail.service.ParentService;
import com.manara.portail.service.InscriptionParentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    private final ActiviteParentService activiteService;
    private final ParentService parentService;
    private final InscriptionParentService inscriptionService;

    public HomeController(ActiviteParentService activiteService,
                          ParentService parentService,
                          InscriptionParentService inscriptionService) {
        this.activiteService = activiteService;
        this.parentService = parentService;
        this.inscriptionService = inscriptionService;
    }

    // ========== ACCUEIL ==========
    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("activitesPopulaires",
                activiteService.getAllActivitesActives().stream().limit(8).toList());

        Map<String, Object> stats = new HashMap<>();
        stats.put("nbParents", parentService.getNombreParentsActifs());
        stats.put("nbEnfants", parentService.getAllEnfants().size());
        stats.put("nbActivites", activiteService.getAllActivitesActives().size());
        stats.put("nbSessions", activiteService.getAllSessions().size());
        model.addAttribute("stats", stats);

        return "index";
    }

    // ========== AUTHENTIFICATION PARENT ==========
    @GetMapping("/register")
    public String pageInscription(Model model) {
        model.addAttribute("parent", new ParentDTO());
        return "inscription";
    }

    @PostMapping("/register")
    public String traiterInscription(@Valid ParentDTO parentDTO,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "inscription";
        }

        try {
            parentService.inscrireParent(parentDTO);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String pageConnexion() {
        return "connexion";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    // ========== PAGES PUBLIQUES ==========
    @GetMapping("/activites")
    public String listeActivites(Model model) {
        model.addAttribute("activites", activiteService.getAllActivitesActives());
        return "activites";
    }

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
}
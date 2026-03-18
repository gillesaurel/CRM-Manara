package com.manara.portail.controller;

import com.manara.portail.service.ActiviteService;
import com.manara.portail.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    private final ActiviteService activiteService;
    private final AdminService adminService;

    public HomeController(ActiviteService activiteService, AdminService adminService) {
        this.activiteService = activiteService;
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("activitesPopulaires",
                activiteService.getAllActivitesActives().stream().limit(8).toList());

        Map<String, Object> stats = new HashMap<>();
        stats.put("nbParents", adminService.getNombreParentsActifs());
        stats.put("nbEnfants", adminService.getNombreEnfants());
        stats.put("nbActivites", activiteService.getAllActivitesActives().size());
        stats.put("nbSessions", adminService.getNombreSessionsFutures());
        model.addAttribute("stats", stats);

        return "index";
    }

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
// fait par Manu Charland-Rivard

package com.manara.portail.controller;

import com.manara.portail.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnimateurController {

    @Autowired
    UtilisateurService UtilService;

    @GetMapping("/animateur")
    public String pageAccueil(Model model){
        return "animateur";
    }

    @GetMapping("/animateur/inscriptions")
    public String pageInscription(Model model) {
        model.addAttribute("enfant", UtilService.afficherUtilisateurs());
        return "animateur_inscription";
    }

    @GetMapping("/animateur/presences")
    public String pagePresences() {
        return "animateur_presences";
    }

    @GetMapping("/animateur/horaire")
    public String pageHoraire() {
        return "animateur_horaire";
    }
}
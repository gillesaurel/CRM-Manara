// fait par Manu Charland-Rivard

package com.manara.portail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AnimateurController {
    @GetMapping("/animateur")
    public String pageAccueil(){
        return "animateur";
    }

    @GetMapping("/animateur/inscriptions")
    public String pageInscription() {
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

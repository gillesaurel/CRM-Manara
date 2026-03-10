package com.manara.portail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AnimateurController {
    @GetMapping("/animateur")
    public String pageAccueil(){
        return "animateur";
    }
}

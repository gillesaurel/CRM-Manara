package com.manara.portail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/register")
    public String pageInscription(){
        return "inscription";
    }

    @GetMapping("/login")
    public String pageConnexion(){
        return "connexion";
    }
}

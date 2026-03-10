package com.manara.portail.controller;

import com.manara.portail.model.Utilisateur;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.mvc.support.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class AuthController {

    // PAGE INSCRIPTION UTILISATEUR
    @GetMapping("/register")
    public String pageInscription(){
        return "inscription";
    }

    // REDIRECTION INSCRIPTION UTILISATEUR
    @PostMapping("/register/save")
    public String enregistrerUtilisateur(Utilisateur utilisateur, RedirectAttributes redirectAttributes,  @RequestParam("fileImage") MultipartFile fileImage, Model model
    ){

            return "redirect:/";

    }


    @GetMapping("/login")
    public String pageConnexion(){
        return "connexion";
    }
}

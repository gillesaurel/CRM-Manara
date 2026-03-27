// fait par Manu Charland-Rivard

package com.manara.portail.controller;

import com.manara.portail.service.UtilisateurService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class AnimateurController {

    @Autowired
    UtilisateurService UtilService;


    @GetMapping("/animateur")
    public String pageAccueil(HttpServletRequest request, Model model) {
        getCookies(request, model, "role");

        return "animateur";
    }

    @GetMapping("/animateur/inscriptions")
    public String pageInscription(HttpServletRequest request, Model model) {
        model.addAttribute("enfant", UtilService.afficherUtilisateurs());
        return "animateur_inscription";
    }

    @GetMapping("/animateur/presences")
    public String pagePresences(HttpServletRequest request, Model model) {
        return "animateur_presences";
    }

    @GetMapping("/animateur/horaire")
    public String pageHoraire(HttpServletRequest request, Model model) {
        return "animateur_horaire";
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

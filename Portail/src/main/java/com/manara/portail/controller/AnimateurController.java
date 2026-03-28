// fait par Manu Charland-Rivard

package com.manara.portail.controller;

import com.manara.portail.model.ActiviteParent;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.SessionParent;
import com.manara.portail.service.SessionParentService;
import com.manara.portail.service.UtilisateurService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AnimateurController {

    @Autowired
    UtilisateurService UtilService;

    @Autowired
    SessionParentService sessionParentService;

    @GetMapping("/animateur")
    public String pageAccueil(HttpServletRequest request, Model model) {
        getCookieValue(request, "role");
        System.out.println("aaa");

        return "animateur";
    }

    @GetMapping("/animateur/inscriptions")
    public String pageInscription(HttpServletRequest request, Model model) {
        String animateurIdStr = getCookieValue(request, "id");
        Integer animateurId = Integer.parseInt(animateurIdStr);
        System.out.println("animateurId from cookie: " + animateurId);

        List<SessionParent> sessions = sessionParentService
                .getSessionsAvecEnfantsByAnimateur(animateurId);

        System.out.println("=== SESSIONS (" + sessions.size() + ") ===");
        for (SessionParent sess : sessions) {
            System.out.println("  Session id=" + sess.getId()
                    + " | activite=" + (sess.getActivite() != null ? sess.getActivite().getNom() : "NULL")
                    + " | animateur_id=" + (sess.getAnimateur() != null ? sess.getAnimateur().getId() : "NULL"));

            System.out.println("  Inscriptions (" + sess.getInscriptions().size() + "):");
            for (InscriptionEnfant inscription : sess.getInscriptions()) {
                System.out.println("    -> inscription id=" + inscription.getId()
                        + " | nomEnfant=" + inscription.getNomEnfant()
                        + " | enAttente=" + inscription.isEnAttente()
                        + " | enfant=" + (inscription.getEnfant() != null
                        ? inscription.getEnfant().getNom() + " " + inscription.getEnfant().getPrenom()
                        : "NULL"));
            }
        }

        model.addAttribute("sessions", sessions);
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




    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

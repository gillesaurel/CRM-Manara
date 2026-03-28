// fait par Manu Charland-Rivard

package com.manara.portail.controller;

import com.manara.portail.model.ActiviteParent;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.SessionParent;
import com.manara.portail.repository.InscriptionEnfantRepository;
import com.manara.portail.service.InscriptionEnfantService;
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

    @Autowired
    InscriptionEnfantService inscriptionEnfantService;

    @GetMapping("/animateur")
    public String pageAccueil(HttpServletRequest request, Model model) {
        getCookieValue(request, "role");
        System.out.println("aaa");

        return "animateur";
    }

    @GetMapping("/animateur/inscriptions")
    public String pageInscription(HttpServletRequest request, Model model) {
        String animateurIdStr = getCookieValue(request, "id");
        if (animateurIdStr == null) return "redirect:/login";

        Integer animateurId = Integer.parseInt(animateurIdStr);
        List<SessionParent> sessions = sessionParentService
                .getSessionsAvecEnfantsByAnimateur(animateurId);

        // Group inscriptions by activite name
        Map<String, List<InscriptionEnfant>> inscriptionsParActivite = new LinkedHashMap<>();
        for (SessionParent sess : sessions) {
            String nomActivite = sess.getActivite() != null ? sess.getActivite().getNom() : "Activité inconnue";
            inscriptionsParActivite.computeIfAbsent(nomActivite, k -> new ArrayList<>());

            List<InscriptionEnfant> inscriptions = inscriptionEnfantService
                    .getInscriptionsBySession(sess.getId());

            for (InscriptionEnfant inscription : inscriptions) {
                // Avoid duplicates
                if (inscriptionsParActivite.get(nomActivite).stream()
                        .noneMatch(i -> i.getId().equals(inscription.getId()))) {
                    inscriptionsParActivite.get(nomActivite).add(inscription);
                }
            }
        }

        model.addAttribute("inscriptionsParActivite", inscriptionsParActivite);
        return "animateur_inscription";
    }

    @GetMapping("/animateur/presences")
    public String pagePresences(HttpServletRequest request, Model model) {
        return "animateur_presences";
    }

    @GetMapping("/animateur/horaire")
    public String pageHoraire(HttpServletRequest request, Model model) {
        String animateurIdStr = getCookieValue(request, "id");
        if (animateurIdStr == null) return "redirect:/login";

        Integer animateurId = Integer.parseInt(animateurIdStr);
        List<SessionParent> sessions = sessionParentService
                .getSessionsAvecEnfantsByAnimateur(animateurId);

        model.addAttribute("sessions", sessions);
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

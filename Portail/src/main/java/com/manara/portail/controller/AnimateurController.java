// fait par Manu Charland-Rivard

package com.manara.portail.controller;

import com.manara.portail.model.ActiviteParent;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.SessionParent;
<<<<<<< HEAD
=======
import com.manara.portail.repository.InscriptionEnfantRepository;
import com.manara.portail.service.InscriptionEnfantService;
>>>>>>> temporary-brench-manu
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

<<<<<<< HEAD
    @GetMapping("/animateur")
    public String pageAccueil(HttpServletRequest request, Model model) {
        getCookieValue(request, "role");
=======
    @Autowired
    InscriptionEnfantService inscriptionEnfantService;

    @GetMapping("/animateur")
    public String pageAccueil(HttpServletRequest request, Model model) {
        getCookieValue(request, "role");
        System.out.println("aaa");
>>>>>>> temporary-brench-manu

        return "animateur";
    }

    @GetMapping("/animateur/inscriptions")
    public String pageInscription(HttpServletRequest request, Model model) {
<<<<<<< HEAD
        // Get animateur id from cookie
        String animateurIdStr = getCookieValue(request, "id");

        if (animateurIdStr == null) {
            return "redirect:/login";
        }
=======
        String animateurIdStr = getCookieValue(request, "id");
        if (animateurIdStr == null) return "redirect:/login";
>>>>>>> temporary-brench-manu

        Integer animateurId = Integer.parseInt(animateurIdStr);
        List<SessionParent> sessions = sessionParentService
                .getSessionsAvecEnfantsByAnimateur(animateurId);

<<<<<<< HEAD
        // Group enfants by activite
        Map<ActiviteParent, List<Enfant>> activitesEnfants = new LinkedHashMap<>();
        for (SessionParent session : sessions) {
            ActiviteParent activite = session.getActivite();
            activitesEnfants.computeIfAbsent(activite, k -> new ArrayList<>());

            for (InscriptionEnfant inscription : session.getInscriptions()) {
                Enfant enfant = inscription.getEnfant();
                if (!activitesEnfants.get(activite).contains(enfant)) {
                    activitesEnfants.get(activite).add(enfant);
=======
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
>>>>>>> temporary-brench-manu
                }
            }
        }

<<<<<<< HEAD
        model.addAttribute("activitesEnfants", activitesEnfants);
=======
        model.addAttribute("inscriptionsParActivite", inscriptionsParActivite);
>>>>>>> temporary-brench-manu
        return "animateur_inscription";
    }

    @GetMapping("/animateur/presences")
    public String pagePresences(HttpServletRequest request, Model model) {
        return "animateur_presences";
    }

    @GetMapping("/animateur/horaire")
    public String pageHoraire(HttpServletRequest request, Model model) {
<<<<<<< HEAD
=======
        String animateurIdStr = getCookieValue(request, "id");
        if (animateurIdStr == null) return "redirect:/login";

        Integer animateurId = Integer.parseInt(animateurIdStr);
        List<SessionParent> sessions = sessionParentService
                .getSessionsAvecEnfantsByAnimateur(animateurId);

        model.addAttribute("sessions", sessions);
>>>>>>> temporary-brench-manu
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

// fait par Manu Charland-Rivard

package com.manara.portail.controller;

import com.manara.portail.model.Enfant;
import com.manara.portail.model.Incident;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.SessionParent;
import com.manara.portail.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AnimateurController {

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    SessionParentService sessionParentService;

    @Autowired
    InscriptionEnfantService inscriptionEnfantService;

    @Autowired
    IncidentService incidentService;

    @Autowired
    EnfantService enfantService;

    @GetMapping("/animateur")
    public String pageAccueil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        String email = auth.getName();
        model.addAttribute("email", email);

        return "animateur";
    }

    @GetMapping("/animateur/inscriptions")
    public String pageInscription(HttpServletRequest request, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return "redirect:/login";

        String email = auth.getName();
        Integer animateurId = utilisateurService.getIdByEmail(email);
        if (animateurId == null) return "redirect:/login";
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return "redirect:/login";

        String email = auth.getName();
        Integer animateurId = utilisateurService.getIdByEmail(email);
        if (animateurId == null) return "redirect:/login";
        List<SessionParent> sessions = sessionParentService
                .getSessionsAvecEnfantsByAnimateur(animateurId);

        // Map: activite name → (session label → list of confirmed enfants)
        Map<String, Map<String, List<Enfant>>> presencesParActivite = new LinkedHashMap<>();

        for (SessionParent sess : sessions) {
            String nomActivite = sess.getActivite() != null ? sess.getActivite().getNom() : "Activité inconnue";
            String sessionLabel = sess.getDate() + " | " + sess.getHeureDebut() + " – " + sess.getHeureFin()
                    + " | " + sess.getLieu();

            presencesParActivite.computeIfAbsent(nomActivite, k -> new LinkedHashMap<>());

            List<Enfant> enfants = inscriptionEnfantService.getEnfantsConfirmesBySession(sess.getId());
            presencesParActivite.get(nomActivite).put(sessionLabel, enfants);
        }

        model.addAttribute("presencesParActivite", presencesParActivite);
        return "animateur_presences";
    }

    @GetMapping("/animateur/horaire")
    public String pageHoraire(HttpServletRequest request, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return "redirect:/login";

        String email = auth.getName();
        Integer animateurId = utilisateurService.getIdByEmail(email);
        if (animateurId == null) return "redirect:/login";
        List<SessionParent> sessions = sessionParentService
                .getSessionsAvecEnfantsByAnimateur(animateurId);

        model.addAttribute("sessions", sessions);
        return "animateur_horaire";
    }


    @GetMapping("/animateur/incidents")
    public String pageIncidents(HttpServletRequest request, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return "redirect:/login";

        String email = auth.getName();
        Integer animateurId = utilisateurService.getIdByEmail(email);
        if (animateurId == null) return "redirect:/login";

        List<Incident> incidents = incidentService.getIncidentsByAnimateur(animateurId);
        List<Enfant> enfants = enfantService.getAllEnfants();

        model.addAttribute("incidents", incidents);
        model.addAttribute("enfants", enfants);
        return "animateur_incidents";
    }

    @PostMapping("/animateur/incidents/save")
    public String signalerIncident(
            HttpServletRequest request,
            @RequestParam("enfantId") Integer enfantId,
            @RequestParam("type") String type,
            @RequestParam("description") String description,
            @RequestParam("gravite") Integer gravite,
            @RequestParam(value = "actionsPrises", required = false) String actionsPrises,
            RedirectAttributes redirectAttributes
    ) {
        String animateurIdStr = getCookieValue(request, "id");
        if (animateurIdStr == null) return "redirect:/login";

        Integer animateurId = Integer.parseInt(animateurIdStr);

        try {
            incidentService.signalerIncident(enfantId, animateurId, type, description, gravite, actionsPrises);
            redirectAttributes.addFlashAttribute("message", "Incident signalé avec succès.");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erreur lors du signalement : " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/animateur/incidents";
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
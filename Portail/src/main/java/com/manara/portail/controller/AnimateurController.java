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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@CrossOrigin(
        origins = {"http://localhost:4200", "http://127.0.0.1:4200", "http://manara.histoiredurock.com", "https://manara.histoiredurock.com"},
        allowCredentials = "true"
)
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
    public ResponseEntity<?> pageInscription() {
        System.out.println("Requested enter!");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        String email = auth.getName();
        Integer animateurId = utilisateurService.getIdByEmail(email);

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
        Map<String, Object> data = new HashMap<>();
        data.put("inscriptions", inscriptionsParActivite);
        System.out.println("Response Data: " + data.toString());
        return ResponseEntity.ok(data);


    }

    @GetMapping("/animateur/presences")
    public ResponseEntity<?> pagePresences() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer animateurId = utilisateurService.getIdByEmail(email);

        List<SessionParent> sessions = sessionParentService
                .getSessionsAvecEnfantsByAnimateur(animateurId);

        Map<String, List<Map<String, Object>>> presencesParActivite = new LinkedHashMap<>();

        for (SessionParent sess : sessions) {
            String nomActivite = sess.getActivite() != null ? sess.getActivite().getNom() : "Activité inconnue";
            String sessionLabel = sess.getDate() + " | " + sess.getHeureDebut() + " – " + sess.getHeureFin()
                    + " | " + sess.getLieu();

            presencesParActivite.computeIfAbsent(nomActivite, k -> new ArrayList<>());

            List<Enfant> enfants = inscriptionEnfantService.getEnfantsConfirmesBySession(sess.getId());

            Map<String, Object> sessionData = new LinkedHashMap<>();
            sessionData.put("sessionLabel", sessionLabel);
            sessionData.put("enfants", enfants);

            presencesParActivite.get(nomActivite).add(sessionData);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("presences", presencesParActivite);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/animateur/horaire")
    public ResponseEntity<?> pageHoraire() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer animateurId = utilisateurService.getIdByEmail(email);

        List<SessionParent> sessions = sessionParentService
                .getSessionsAvecEnfantsByAnimateur(animateurId);

        Map<String, Object> data = new HashMap<>();
        data.put("sessions", sessions);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/animateur/incidents")
    public ResponseEntity<?> pageIncidents() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer animateurId = utilisateurService.getIdByEmail(email);

        List<Incident> incidents = incidentService.getIncidentsByAnimateur(animateurId);
        List<Enfant> enfants = enfantService.getAllEnfants();

        Map<String, Object> data = new HashMap<>();
        data.put("incidents", incidents);
        data.put("enfants", enfants);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/animateur/incidents/save")
    public ResponseEntity<?> signalerIncident(
            @RequestParam("enfantId") Integer enfantId,
            @RequestParam("type") String type,
            @RequestParam("description") String description,
            @RequestParam("gravite") Integer gravite,
            @RequestParam(value = "actionsPrises", required = false) String actionsPrises
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer animateurId = utilisateurService.getIdByEmail(email);

        try {
            incidentService.signalerIncident(enfantId, animateurId, type, description, gravite, actionsPrises);
            Map<String, Object> data = new HashMap<>();
            data.put("message", "Incident signalé avec succès.");
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            Map<String, Object> data = new HashMap<>();
            data.put("message", "Erreur lors du signalement : " + e.getMessage());
            return ResponseEntity.status(500).body(data);
        }
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
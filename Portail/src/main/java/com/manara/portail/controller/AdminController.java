package com.manara.portail.controller;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.AdminRepository;
import com.manara.portail.repository.RoleRepository;
import com.manara.portail.service.ActiviteService;
import com.manara.portail.service.AdminService;
import com.manara.portail.service.InscriptionEnfantService;
import com.manara.portail.service.SessionService;
import com.manara.portail.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@CrossOrigin(
        origins = {"http://localhost:4200", "http://127.0.0.1:4200", "http://manara.histoiredurock.com", "https://manara.histoiredurock.com"},
        allowCredentials = "true"
)
public class AdminController {
    @Autowired
    private ActiviteService activiteService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private InscriptionEnfantService inscriptionEnfantService;

    @GetMapping("/administrateur")
    public ResponseEntity<Map<String, Object>> pageAdministrateur() {
        List<ActiviteAdmin> activites = activiteService.getAllActivites();

        Map<String, Object> data = new HashMap<>();
        data.put("activites", activites);
        data.put("totalActivities", activites.size());
        data.put("pourcentageInscrit", activiteService.getPourcentageInscription());

        return ResponseEntity.ok(data);
    }

    @GetMapping("/adminOnly/activites")
    public ResponseEntity<Map<String, Object>> showActivites() {
        List<ActiviteAdmin> activites = activiteService.getAllActivites();

        Map<String, Object> data = new HashMap<>();
        data.put("activites", activites);
        data.put("totalActivities", activites.size());
        data.put("pourcentageInscrit", activiteService.getPourcentageInscription());

        return ResponseEntity.ok(data);
    }

    @PostMapping("/activities/save")
    public ResponseEntity<?> saveOrUpdateActivite(@Valid @RequestBody ActiviteAdmin activite) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activiteService.save(activite));
    }

    @GetMapping("/activities/edit/{id}")
    public ResponseEntity<?> modifyActivite(@PathVariable Long id) {
        ActiviteAdmin activite = activiteService.getById(id);
        if (activite == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activite);
    }

    @PutMapping("/activities/update/{id}")
    public ResponseEntity<?> updateActivite(@PathVariable Long id, @Valid @RequestBody ActiviteAdmin activite) {
        activite.setId(id);
        return ResponseEntity.ok(activiteService.save(activite));
    }

    @DeleteMapping("/activities/delete/{id}")
    public ResponseEntity<?> removeActivite(@PathVariable Long id) {
        try {
            activiteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/adminOnly/listeAttente")
    public ResponseEntity<Map<String, Object>> listeAttente() {
        return buildListeAttenteResponse();
    }

    @GetMapping("/adminOnly/listeAttentes")
    public ResponseEntity<Map<String, Object>> listeAttentes() {
        return buildListeAttenteResponse();
    }

    private ResponseEntity<Map<String, Object>> buildListeAttenteResponse() {
        List<InscriptionEnfant> inscriptions = inscriptionEnfantService.getListeAttente();

        Map<String, Long> stats = inscriptions.stream().collect(Collectors.groupingBy(
                this::inscriptionLabel,
                Collectors.counting()
        ));

        Map<String, List<InscriptionEnfant>> groupedInscriptions = inscriptions.stream()
                .collect(Collectors.groupingBy(this::inscriptionLabel));

        Map<String, Object> data = new HashMap<>();
        data.put("inscriptions", inscriptions);
        data.put("stats", stats);
        data.put("groupedInscriptions", groupedInscriptions);
        data.put("activites", activiteService.getAllActivites());

        return ResponseEntity.ok(data);
    }

    @PostMapping("/inscriptions/valider/{id}")
    public ResponseEntity<?> validerInscription(@PathVariable Long id) {
        try {
            inscriptionEnfantService.confirmerInscriptionAvecEmail(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Inscription validée"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/inscriptions/delete/{id}")
    public ResponseEntity<?> deleteInscription(@PathVariable Long id) {
        try {
            inscriptionEnfantService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("adminOnly/adminAddUser")
    public ResponseEntity<Map<String, Object>> pageAjouterUser() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setRole(new Role());
        return ResponseEntity.ok(Map.of(
                "utilisateur", utilisateur,
                "roles", roleRepository.findAll()
        ));
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUserByAdmin(@RequestBody Utilisateur utilisateur) {
        try {
            return ResponseEntity.ok(utilisateurService.enregistrerParAdmin(utilisateur));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    public ResponseEntity<?> updateUserByAdmin(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        try {
            return ResponseEntity.ok(utilisateurService.mettreAJourParAdmin(id, utilisateur));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/adminOnly/utilisateurs")
    public ResponseEntity<Map<String, Object>> listUsers(@RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) String roleFilter) {
        return ResponseEntity.ok(adminService.rechercheToutLeMonde(keyword, roleFilter));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam Long userId,
                                           @RequestParam(required = false) String password,
                                           @RequestParam(required = false) String newPassword) {
        try {
            String value = password != null ? password : newPassword;
            adminService.reinitialiserPassword(userId, value);
            return ResponseEntity.ok(Map.of("success", true, "message", "Mot de passe réinitialisé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/utilisateur/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            adminService.supprimerUtilisateur(id);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("adminOnly/email")
    public ResponseEntity<Map<String, Object>> emailPage() {
        return ResponseEntity.ok(Map.of(
                "parents", adminRepository.findByRole("PARENT"),
                "animateurs", adminRepository.findByRole("ANIMATEUR")
        ));
    }

    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, Object> payload) {
        try {
            String type = (String) payload.get("type");
            String subject = (String) payload.get("subject");
            String message = (String) payload.get("message");

            switch (type) {
                case "PARENT_ONE":
                    adminService.envoyerEmailParent(Long.valueOf(payload.get("parentId").toString()), subject, message);
                    break;
                case "PARENT_ALL":
                    adminService.envoyerEmailTousParents(subject, message);
                    break;
                case "ANIMATEUR_ONE":
                    adminService.envoyerEmailAnimateur(Long.valueOf(payload.get("animateurId").toString()), subject, message);
                    break;
                case "ANIMATEUR_ALL":
                    adminService.envoyerEmailTousAnimateurs(subject, message);
                    break;
                case "ALL":
                    adminService.envoyerEmailToutLeMonde(subject, message);
                    break;
                default:
                    throw new IllegalArgumentException("Type d'envoi inconnu");
            }

            return ResponseEntity.ok(Map.of("success", true, "message", "Email envoyé !"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    private String inscriptionLabel(InscriptionEnfant inscription) {
        if (inscription.getSession() != null && inscription.getSession().getActivite() != null) {
            return inscription.getSession().getActivite().getNom();
        }
        if (inscription.getActivite() != null) {
            return inscription.getActivite().getNom();
        }
        return "Sans activité";
    }
}

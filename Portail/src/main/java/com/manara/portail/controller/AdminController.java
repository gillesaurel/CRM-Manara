package com.manara.portail.controller;

import com.manara.portail.model.*;
import com.manara.portail.repository.AdminRepository;
import com.manara.portail.repository.ParentRepository;
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
    private ParentRepository parentRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private InscriptionEnfantService inscriptionEnfantService;

    // Retourne les statistiques principales de l'admin
    @GetMapping("/administrateur")
    public ResponseEntity<Map<String, Object>> pageAdministrateur() {
        List<ActiviteAdmin> activites = activiteService.getAllActivites();

        Map<String, Object> data = new HashMap<>();
        data.put("activites", activites);
        data.put("totalActivities", activites.size());
        data.put("pourcentageInscrit", activiteService.getPourcentageInscription());

        return ResponseEntity.ok(data);
    }

    // Retourne toutes les activités + taux global
    @GetMapping("/adminOnly/activites")
    public ResponseEntity<Map<String, Object>> showActivites() {
        List<ActiviteAdmin> activites = activiteService.getAllActivites();

        Map<String, Object> data = new HashMap<>();
        data.put("activites", activites);
        data.put("totalActivities", activites.size());
        data.put("pourcentageInscrit", activiteService.getPourcentageInscription());

        return ResponseEntity.ok(data);
    }

    // Crée ou modifie une activité
    @PostMapping("/activities/save")
    public ResponseEntity<?> saveOrUpdateActivite(@RequestBody ActiviteAdmin activite) {
        ActiviteAdmin saved = activiteService.save(activite);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Récupère une activité par ID
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

        Map<String, Long> stats = inscriptions.stream()
                .collect(Collectors.groupingBy(
                        i -> i.getSession().getActivite().getNom(),
                        Collectors.counting()
                ));

        Map<String, Object> data = new HashMap<>();
        data.put("inscriptions", inscriptions);
        data.put("stats", stats);

        //Map<String, Object> data = new HashMap<>();
        data.put("inscriptions", inscriptions);
        data.put("stats", stats);
        //data.put("groupedInscriptions", groupedInscriptions);
        data.put("activites", activiteService.getAllActivites());

        return ResponseEntity.ok(data);
    }

    // Confirme une inscription + envoie email
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

    // Crée un utilisateur (admin)
    @PostMapping("/save")
    public ResponseEntity<?> saveUserByAdmin(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur saved = utilisateurService.enregistrerParAdmin(utilisateur);

            return ResponseEntity.ok(Map.of(
                    "message", "success",
                    "id", saved.getId()
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }

    // Supprime un utilisateur
    @DeleteMapping("/utilisateur/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            adminService.supprimerUtilisateur(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Utilisateur supprimé"
            ));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Recherche utilisateurs
    @GetMapping("/adminOnly/utilisateurs")
    public ResponseEntity<?> listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String roleFilter) {

        return ResponseEntity.ok(
                adminService.rechercheToutLeMonde(keyword, roleFilter)
        );
    }

    // Réinitialise mot de passe utilisateur
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam Long userId,
                                           @RequestParam String newPassword) {
        try {
            adminService.reinitialiserPassword(userId, newPassword);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Mot de passe mis à jour"
            ));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Donne les utilisateurs pour l'interface email
    @GetMapping("/adminOnly/email")
    public ResponseEntity<?> emailPage() {

        Map<String, Object> data = new HashMap<>();
        data.put("parents", adminRepository.findByRole("PARENT"));
        data.put("animateurs", adminRepository.findByRole("ANIMATEUR"));

        return ResponseEntity.ok(data);
    }

    // Envoie email selon le type choisi
    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, Object> payload) {

        try {
            String type = (String) payload.get("type");
            String subject = (String) payload.get("subject");
            String message = (String) payload.get("message");

            switch (type) {

                case "PARENT_ONE":
                    if (payload.get("parentId") == null) {
                        throw new RuntimeException("parentId obligatoire");
                    }
                    adminService.envoyerEmailParent(
                            Long.valueOf(payload.get("parentId").toString()),
                            subject,
                            message
                    );
                    break;

                case "PARENT_ALL":
                    adminService.envoyerEmailTousParents(subject, message);
                    break;

                case "ANIMATEUR_ONE":
                    if (payload.get("animateurId") == null) {
                        throw new RuntimeException("animateurId obligatoire");
                    }

                    adminService.envoyerEmailAnimateur(
                            Long.valueOf(payload.get("animateurId").toString()),
                            subject,
                            message
                    );
                    break;

                case "ANIMATEUR_ALL":
                    adminService.envoyerEmailTousAnimateurs(subject, message);
                    break;

                case "ALL":
                    adminService.envoyerEmailToutLeMonde(subject, message);
                    break;

                default:
                    throw new RuntimeException("Type invalide: " + type);
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Email envoyé avec succès !"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
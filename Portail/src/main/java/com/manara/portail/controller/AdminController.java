package com.manara.portail.controller;

import com.manara.portail.model.*;
import com.manara.portail.repository.AdminRepository;
import com.manara.portail.repository.RoleRepository;
import com.manara.portail.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
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

    // Retourne les statistiques principales de l'admin
    @GetMapping("/administrateur")
    public ResponseEntity<?> getAdminData() {

        List<ActiviteAdmin> activites = activiteService.getAllActivites();

        Map<String, Object> data = new HashMap<>();
        data.put("activites", activites);
        data.put("totalActivities", activites.size());
        data.put("pourcentageInscrit", activiteService.getPourcentageInscription());

        return ResponseEntity.ok(data);
    }

    // Retourne toutes les activités + taux global
    @GetMapping("/adminOnly/activites")
    public ResponseEntity<?> showActivites() {

        List<ActiviteAdmin> activites = activiteService.getAllActivites();

        Map<String, Object> data = new HashMap<>();
        data.put("activites", activites);
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
        try {
            ActiviteAdmin activite = activiteService.getById(id);
            return ResponseEntity.ok(activite);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/activities/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ActiviteAdmin activite) {
        activite.setId(id);
        return ResponseEntity.ok(activiteService.save(activite));
    }

    // Supprime une activité
    @DeleteMapping("/activities/delete/{id}")
    public ResponseEntity<?> removeActivite(@PathVariable Long id) {
        activiteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Retourne les inscriptions en attente + statistiques
    @GetMapping("/adminOnly/listeAttentes")
    public ResponseEntity<?> listeAttente() {

        List<InscriptionEnfant> inscriptions = inscriptionEnfantService.getListeAttente();

        Map<String, Long> stats = inscriptions.stream()
                .collect(Collectors.groupingBy(
                        i -> i.getSession().getActivite().getNom(),
                        Collectors.counting()
                ));

        Map<String, Object> data = new HashMap<>();
        data.put("inscriptions", inscriptions);
        data.put("stats", stats);

        return ResponseEntity.ok(data);
    }

    // Confirme une inscription + envoie email
    @PostMapping("/inscriptions/valider/{id}")
    public ResponseEntity<?> validerInscription(@PathVariable Long id) {
        inscriptionEnfantService.confirmerInscriptionAvecEmail(id);
        return ResponseEntity.ok("validated");
    }

    // Supprime une inscription
    @DeleteMapping("/inscriptions/delete/{id}")
    public ResponseEntity<?> deleteInscription(@PathVariable Long id) {
        inscriptionEnfantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Crée un utilisateur (admin)
    @PostMapping("/save")
    public ResponseEntity<?> saveUserByAdmin(@RequestBody Utilisateur utilisateur) {
        try {
            utilisateurService.enregistrerParAdmin(utilisateur);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Supprime un utilisateur
    @DeleteMapping("/utilisateur/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            adminService.supprimerUtilisateur(id);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
                                           @RequestParam String password) {
        try {
            adminService.reinitialiserPassword(userId, password);
            return ResponseEntity.ok("password updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
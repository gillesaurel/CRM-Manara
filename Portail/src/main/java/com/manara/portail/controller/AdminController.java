package com.manara.portail.controller;

import com.manara.portail.model.*;
import com.manara.portail.repository.AdminRepository;
import com.manara.portail.repository.ParentRepository;
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
    private ParentRepository parentRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private InscriptionEnfantService inscriptionEnfantService;

    // ===== HELPERS =====

    private Map<String, Object> activiteToMap(ActiviteAdmin a) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", a.getId());
        map.put("nom", a.getNom());
        map.put("description", a.getDescription());
        map.put("capacite", a.getCapacite());
        map.put("dateDebut", a.getDateDebut());
        map.put("dateCreation", a.getDateCreation());
        map.put("nbInscrits", a.getNbInscrits());
        map.put("nbEnAttente", a.getNbEnAttente());
        map.put("complet", a.isComplet());
        map.put("tauxRemplissage", a.getTauxRemplissage());
        map.put("prix", a.getPrix());

        // Animateur simplifié
        if (a.getAnimateur() != null) {
            Map<String, Object> anim = new HashMap<>();
            anim.put("id", a.getAnimateur().getId());
            anim.put("nom", a.getAnimateur().getNom());
            anim.put("prenom", a.getAnimateur().getPrenom());
            map.put("animateur", anim);
        }

        // Séances
        if (a.getSeances() != null) {
            List<Map<String, Object>> seances = a.getSeances().stream().map(s -> {
                Map<String, Object> sm = new HashMap<>();
                sm.put("id", s.getId());
                sm.put("dateHeure", s.getDateHeure());
                return sm;
            }).collect(Collectors.toList());
            map.put("seances", seances);
        }

        return map;
    }

    private Map<String, Object> utilisateurToMap(Utilisateur u) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", u.getId());
        map.put("nom", u.getNom());
        map.put("prenom", u.getPrenom());
        map.put("email", u.getEmail());
        map.put("telephone", u.getTelephone());
        map.put("role", u.getRole() != null ? u.getRole().getNom() : null);
        return map;
    }

    // ===== ACTIVITÉS =====

    @GetMapping("/administrateur")
    public ResponseEntity<?> getAdminData() {
        List<ActiviteAdmin> activites = activiteService.getAllActivites();
        List<Map<String, Object>> activitesSimples = activites.stream()
                .map(this::activiteToMap).collect(Collectors.toList());
        Map<String, Object> data = new HashMap<>();
        data.put("activites", activitesSimples);
        data.put("totalActivities", activites.size());
        data.put("pourcentageInscrit", activiteService.getPourcentageInscription());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/adminOnly/activites")
    public ResponseEntity<?> showActivites() {
        List<ActiviteAdmin> activites = activiteService.getAllActivites();
        List<Map<String, Object>> activitesSimples = activites.stream()
                .map(this::activiteToMap).collect(Collectors.toList());
        Map<String, Object> data = new HashMap<>();
        data.put("activites", activitesSimples);
        data.put("pourcentageInscrit", activiteService.getPourcentageInscription());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/activities/save")
    public ResponseEntity<?> saveOrUpdateActivite(@RequestBody Map<String, Object> payload) {
        try {
            ActiviteAdmin activite = activiteService.fromPayload(payload);
            ActiviteAdmin saved = activiteService.save(activite);
            return ResponseEntity.status(HttpStatus.CREATED).body(activiteToMap(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/activities/edit/{id}")
    public ResponseEntity<?> getActiviteById(@PathVariable Long id) {
        try {
            ActiviteAdmin activite = activiteService.getById(id);
            return ResponseEntity.ok(activiteToMap(activite));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/activities/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            ActiviteAdmin activite = activiteService.fromPayload(payload);
            activite.setId(id);
            return ResponseEntity.ok(activiteToMap(activiteService.save(activite)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/activities/delete/{id}")
    public ResponseEntity<?> removeActivite(@PathVariable Long id) {
        activiteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ===== ANIMATEURS pour le formulaire =====

    @GetMapping("/adminOnly/animateurs")
    public ResponseEntity<?> getAnimateurs() {
        List<Utilisateur> animateurs = adminRepository.findByRole("ANIMATEUR");
        List<Map<String, Object>> result = animateurs.stream()
                .map(this::utilisateurToMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ===== INSCRIPTIONS =====

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

    @PostMapping("/inscriptions/valider/{id}")
    public ResponseEntity<?> validerInscription(@PathVariable Long id) {
        inscriptionEnfantService.confirmerInscriptionAvecEmail(id);
        return ResponseEntity.ok("validated");
    }

    @DeleteMapping("/inscriptions/delete/{id}")
    public ResponseEntity<?> deleteInscription(@PathVariable Long id) {
        inscriptionEnfantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ===== UTILISATEURS =====

    @GetMapping("/adminOnly/utilisateurs")
    public ResponseEntity<?> listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String roleFilter) {

        Object result = adminService.rechercheToutLeMonde(keyword, roleFilter);
        Map<String, Object> raw = (Map<String, Object>) result;

        List<Utilisateur> parents = (List<Utilisateur>) raw.getOrDefault("parents", new ArrayList<>());
        List<Utilisateur> animateurs = (List<Utilisateur>) raw.getOrDefault("animateurs", new ArrayList<>());
        List<Utilisateur> enfants = (List<Utilisateur>) raw.getOrDefault("enfants", new ArrayList<>());

        Map<String, Object> data = new HashMap<>();
        data.put("parents", parents.stream().map(this::utilisateurToMap).collect(Collectors.toList()));
        data.put("animateurs", animateurs.stream().map(this::utilisateurToMap).collect(Collectors.toList()));
        data.put("enfants", enfants.stream().map(this::utilisateurToMap).collect(Collectors.toList()));

        return ResponseEntity.ok(data);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUserByAdmin(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur saved = utilisateurService.enregistrerParAdmin(utilisateur);
            return ResponseEntity.ok(Map.of("message", "success", "id", saved.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/utilisateur/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            adminService.supprimerUtilisateur(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Utilisateur supprimé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam Long userId,
                                           @RequestParam String newPassword) {
        try {
            adminService.reinitialiserPassword(userId, newPassword);
            return ResponseEntity.ok(Map.of("success", true, "message", "Mot de passe mis à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ===== EMAIL =====

    @GetMapping("/adminOnly/email")
    public ResponseEntity<?> emailPage() {
        Map<String, Object> data = new HashMap<>();
        data.put("parents", adminRepository.findByRole("PARENT"));
        data.put("animateurs", adminRepository.findByRole("ANIMATEUR"));
        return ResponseEntity.ok(data);
    }

    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, Object> payload) {
        try {
            String type = (String) payload.get("type");
            String subject = (String) payload.get("subject");
            String message = (String) payload.get("message");

            switch (type) {
                case "PARENT_ONE":
                    if (payload.get("parentId") == null) throw new RuntimeException("parentId obligatoire");
                    adminService.envoyerEmailParent(Long.valueOf(payload.get("parentId").toString()), subject, message);
                    break;
                case "PARENT_ALL":
                    adminService.envoyerEmailTousParents(subject, message);
                    break;
                case "ANIMATEUR_ONE":
                    if (payload.get("animateurId") == null) throw new RuntimeException("animateurId obligatoire");
                    adminService.envoyerEmailAnimateur(Long.valueOf(payload.get("animateurId").toString()), subject, message);
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

            return ResponseEntity.ok(Map.of("success", true, "message", "Email envoyé avec succès !"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

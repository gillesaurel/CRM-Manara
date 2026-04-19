package com.manara.portail.controller;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.AdminRepository;
import com.manara.portail.repository.RoleRepository;
import com.manara.portail.repository.UtilisateurRepository;
import com.manara.portail.service.ActiviteService;
import com.manara.portail.service.AdminService;
import com.manara.portail.service.InscriptionEnfantService;
import com.manara.portail.service.SessionService;
import com.manara.portail.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
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
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private InscriptionEnfantService inscriptionEnfantService;

    @GetMapping("/administrateur")
    public String pageAdministrateur(Model model) {
        List<ActiviteAdmin> activites = activiteService.getAllActivites();
        model.addAttribute("activites", activites);
        model.addAttribute("totalActivities", activites.size());
        model.addAttribute("PourcentageInscrit", activiteService.getPourcentageInscription());
        model.addAttribute("activite", new ActiviteAdmin());
        return "administrateur";
    }

    @GetMapping("/adminOnly/activites")
    public String showActivites(Model model) {
        List<ActiviteAdmin> activites = activiteService.getAllActivites();
        model.addAttribute("activites", activites);
        model.addAttribute("tauxGlobal", activiteService.getPourcentageInscription());
        model.addAttribute("activite", new ActiviteAdmin());
        return "/adminOnly/activites";
    }

    @PostMapping("/activities/save")
    public String saveOrUpdateActivite(@Valid @ModelAttribute ActiviteAdmin activite,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("activites", activiteService.getAllActivites());
            return "administrateur";
        }

        activiteService.save(activite);
        redirectAttributes.addFlashAttribute("message", "Activité enregistrée avec succès.");
        return "redirect:/admin/administrateur";
    }

    @GetMapping("/activities/edit/{id}")
    public String modifyActivite(@PathVariable Long id, Model model) {
        ActiviteAdmin activite = activiteService.getById(id);
        model.addAttribute("activite", activite);
        model.addAttribute("activites", activiteService.getAllActivites());
        return "administrateur";
    }

    @GetMapping("/activities/delete/{id}")
    public String removeActivite(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            activiteService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Activité supprimée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression !");
        }
        return "redirect:/admin/adminOnly/activites";
    }

    @GetMapping("/adminOnly/listeAttente")
    public String listeAttente(Model model) {
        List<InscriptionEnfant> inscriptions = inscriptionEnfantService.getListeAttente();

        Map<String, Long> stats = inscriptions.stream().collect(Collectors.groupingBy(
                this::inscriptionLabel,
                Collectors.counting()
        ));

        Map<String, List<InscriptionEnfant>> groupedInscriptions = inscriptions.stream()
                .collect(Collectors.groupingBy(this::inscriptionLabel));

        model.addAttribute("activites", activiteService.getAllActivites());
        model.addAttribute("stats", stats);
        model.addAttribute("groupedInscriptions", groupedInscriptions);
        return "adminOnly/listeAttente";
    }

    @PostMapping("/inscriptions/valider/{id}")
    public String validerInscription(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inscriptionEnfantService.confirmerInscriptionAvecEmail(id);
            redirectAttributes.addFlashAttribute("message", "Inscription validée !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Activité complète !");
        }
        return "redirect:/admin/adminOnly/listeAttente";
    }

    @GetMapping("/inscriptions/delete/{id}")
    public String deleteInscription(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inscriptionEnfantService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Inscription supprimée");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/adminOnly/listeAttente";
    }

    @GetMapping("adminOnly/adminAddUser")
    public String pageAjouterUser(Model model) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setRole(new Role());
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("roles", roleRepository.findAll());
        return "adminOnly/adminAddUser";
    }

    @PostMapping("/save")
    public String saveUserByAdmin(@ModelAttribute Utilisateur utilisateur, Model model) {
        try {
            utilisateurService.enregistrerParAdmin(utilisateur);
            return "redirect:/admin/adminOnly/adminAddUser";
        } catch (Exception e) {
            model.addAttribute("Erreur : ", e.getMessage());
            model.addAttribute("roles", roleRepository.findAll());
            return "adminOnly/adminAddUser";
        }
    }

    @PostMapping("utilisateur/delete/{id}")
    public String deleteUserForm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminService.supprimerUtilisateur(id);
            redirectAttributes.addFlashAttribute("message", "Utilisateur supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/adminOnly/utilisateurs";
    }

    @GetMapping("/adminOnly/utilisateurs")
    public String listUsers(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String roleFilter,
                            Model model) {
        Map<String, Object> result = adminService.rechercheToutLeMonde(keyword, roleFilter);
        model.addAttribute("parents", result.get("parents"));
        model.addAttribute("animateurs", result.get("animateurs"));
        model.addAttribute("enfants", result.get("enfants"));
        model.addAttribute("keyword", keyword);
        model.addAttribute("roleFilter", roleFilter);
        return "/adminOnly/utilisateurs";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam Long userId,
                                @RequestParam String password,
                                RedirectAttributes redirectAttributes) {
        try {
            adminService.reinitialiserPassword(userId, password);
            redirectAttributes.addFlashAttribute("message", "Mot de passe réinitialisé avec succès ");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/adminOnly/utilisateurs";
    }

    @DeleteMapping("/utilisateur/delete/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable Long id) {
        try {
            adminService.supprimerUtilisateur(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("adminOnly/email")
    public String emailPage(Model model) {
        model.addAttribute("parents", adminRepository.findByRole("PARENT"));
        model.addAttribute("animateurs", adminRepository.findByRole("ANIMATEUR"));
        return "adminOnly/email";
    }

    @PostMapping("/email/send")
    public String sendEmail(@RequestParam String type,
                            @RequestParam(required = false) Long parentId,
                            @RequestParam(required = false) Long animateurId,
                            @RequestParam String subject,
                            @RequestParam String message,
                            RedirectAttributes redirectAttributes) {
        try {
            switch (type) {
                case "PARENT_ONE":
                    adminService.envoyerEmailParent(parentId, subject, message);
                    break;
                case "PARENT_ALL":
                    adminService.envoyerEmailTousParents(subject, message);
                    break;
                case "ANIMATEUR_ONE":
                    adminService.envoyerEmailAnimateur(animateurId, subject, message);
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
            redirectAttributes.addFlashAttribute("message", "Email envoyé !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/admin/adminOnly/email";
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

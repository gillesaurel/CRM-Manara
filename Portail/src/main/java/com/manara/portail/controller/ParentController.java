package com.manara.portail.controller;

import com.manara.portail.dto.EnfantDTO;
import com.manara.portail.dto.ParentDTO;
import com.manara.portail.model.Parent;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.model.Communication;
import com.manara.portail.model.SessionParent;
import com.manara.portail.service.ParentService;
import com.manara.portail.service.InscriptionEnfantService;
import com.manara.portail.service.ActiviteParentService;
import com.manara.portail.service.CommunicationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/parent")
public class ParentController {

    private final ParentService parentService;
    private final InscriptionEnfantService inscriptionService;
    private final ActiviteParentService activiteService;
    private final CommunicationService communicationService;

    public ParentController(ParentService parentService,
                            InscriptionEnfantService inscriptionService,
                            ActiviteParentService activiteService,
                            CommunicationService communicationService) {
        this.parentService = parentService;
        this.inscriptionService = inscriptionService;
        this.activiteService = activiteService;
        this.communicationService = communicationService;
    }

    // Méthode utilitaire pour récupérer le parent connecté
    private Parent getParentConnecte(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String email = authentication.getName();
        return parentService.getParentParEmail(email);
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        // Récupérer toutes les données
        List<Enfant> enfants = parentService.getEnfantsDuParent(parent.getId());
        List<InscriptionEnfant> inscriptionsFutures = inscriptionService.getInscriptionsFuturesParent(parent.getId());
        long messagesNonLus = communicationService.getNombreMessagesNonLusParent(parent.getId());
        List<Communication> communications = communicationService.getCommunicationsParent(parent.getId());

        // Récupérer les prochaines activités
        List<SessionParent> prochainesActivites = new ArrayList<>();
        try {
            prochainesActivites = activiteService.getProchainesSessions(5);
        } catch (Exception e) {
            prochainesActivites = new ArrayList<>();
        }

        // Ajouter toutes les variables au modèle
        model.addAttribute("parent", parent);
        model.addAttribute("enfants", enfants != null ? enfants : new ArrayList<>());
        model.addAttribute("inscriptions", inscriptionsFutures != null ? inscriptionsFutures : new ArrayList<>());
        model.addAttribute("messagesNonLus", messagesNonLus);
        model.addAttribute("prochainesActivites", prochainesActivites != null ? prochainesActivites : new ArrayList<>());
        model.addAttribute("communications", communications != null ? communications : new ArrayList<>());

        return "parent/dashboard";
    }

    @GetMapping("/profil")
    public String profil(Authentication authentication, Model model) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        ParentDTO parentDTO = new ParentDTO();
        parentDTO.setNom(parent.getUtilisateur().getNom());
        parentDTO.setPrenom(parent.getUtilisateur().getPrenom());
        parentDTO.setEmail(parent.getUtilisateur().getEmail());
        parentDTO.setTelephone(parent.getUtilisateur().getTelephone());

        model.addAttribute("parent", parentDTO);
        return "parent/profil";
    }

    @PostMapping("/profil/update")
    public String updateProfile(Authentication authentication,
                                @Valid @ModelAttribute("parent") ParentDTO parentDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "parent/profil";
        }

        try {
            parentService.mettreAJourParent(parent.getId(), parentDTO);
            redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour: " + e.getMessage());
        }

        return "redirect:/parent/profil";
    }

    @GetMapping("/enfants")
    public String listeEnfants(Authentication authentication, Model model) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        List<Enfant> enfants = parentService.getEnfantsDuParent(parent.getId());
        model.addAttribute("enfants", enfants != null ? enfants : new ArrayList<>());
        return "parent/enfants";
    }

    @GetMapping("/enfants/ajouter")
    public String ajouterEnfantForm(Model model) {
        model.addAttribute("enfant", new EnfantDTO());
        return "parent/ajouter-enfant";
    }

    @PostMapping("/enfants/ajouter")
    public String ajouterEnfant(Authentication authentication,
                                @Valid @ModelAttribute("enfant") EnfantDTO enfantDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "parent/ajouter-enfant";
        }

        try {
            parentService.ajouterEnfant(parent.getId(), enfantDTO);
            redirectAttributes.addFlashAttribute("success", "Enfant ajouté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout: " + e.getMessage());
        }

        return "redirect:/parent/enfants";
    }

    @GetMapping("/enfants/modifier/{id}")
    public String modifierEnfantForm(@PathVariable Integer id, Model model) {
        Enfant enfant = parentService.getEnfantParId(id);

        EnfantDTO enfantDTO = new EnfantDTO();
        enfantDTO.setId(enfant.getId());
        enfantDTO.setNom(enfant.getNom());
        enfantDTO.setPrenom(enfant.getPrenom());
        enfantDTO.setDateNaissance(enfant.getDateNaissance());
        enfantDTO.setAllergies(enfant.getAllergies());
        enfantDTO.setNotesMedicales(enfant.getNotesMedicales());

        model.addAttribute("enfant", enfantDTO);
        return "parent/modifier-enfant";
    }

    @PostMapping("/enfants/modifier/{id}")
    public String modifierEnfant(@PathVariable Integer id,
                                 @Valid @ModelAttribute("enfant") EnfantDTO enfantDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "parent/modifier-enfant";
        }

        try {
            parentService.modifierEnfant(id, enfantDTO);
            redirectAttributes.addFlashAttribute("success", "Enfant modifié avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
        }

        return "redirect:/parent/enfants";
    }

    @GetMapping("/enfants/{id}")
    public String detailsEnfant(@PathVariable Integer id, Model model) {
        Enfant enfant = parentService.getEnfantParId(id);
        model.addAttribute("enfant", enfant);
        return "parent/details-enfant";
    }

    @GetMapping("/enfants/supprimer/{id}")
    public String supprimerEnfant(@PathVariable Integer id,
                                  RedirectAttributes redirectAttributes) {
        try {
            parentService.supprimerEnfant(id);
            redirectAttributes.addFlashAttribute("success", "Enfant supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }

        return "redirect:/parent/enfants";
    }

    @GetMapping("/activites")
    public String listeActivites(Model model) {
        List<?> activites = activiteService.getAllActivitesActives();
        model.addAttribute("activites", activites != null ? activites : new ArrayList<>());
        return "parent/activites";
    }

    @GetMapping("/activites/{id}/sessions")
    public String voirSessions(@PathVariable Integer id, Model model) {
        model.addAttribute("activite", activiteService.getActiviteById(id));
        List<SessionParent> sessions = activiteService.getSessionsFuturesByActivite(id);
        model.addAttribute("sessions", sessions != null ? sessions : new ArrayList<>());
        return "parent/sessions";
    }

    @GetMapping("/inscription-enfant")
    public String formulaireInscriptionEnfant(Authentication authentication, Model model) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        List<Enfant> enfants = parentService.getEnfantsDuParent(parent.getId());
        List<SessionParent> sessions = activiteService.getSessionsFuturesSessions();

        model.addAttribute("enfants", enfants != null ? enfants : new ArrayList<>());
        model.addAttribute("sessions", sessions != null ? sessions : new ArrayList<>());

        return "inscription-enfant";
    }

    @GetMapping("/inscriptions")
    public String mesInscriptions(Authentication authentication, Model model) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        List<InscriptionEnfant> inscriptions = inscriptionService.getInscriptionsFuturesParent(parent.getId());
        List<InscriptionEnfant> inscriptionsPassees = inscriptionService.getInscriptionsPasseesParent(parent.getId());

        model.addAttribute("inscriptions", inscriptions != null ? inscriptions : new ArrayList<>());
        model.addAttribute("inscriptionsPassees", inscriptionsPassees != null ? inscriptionsPassees : new ArrayList<>());
        return "parent/inscriptions";
    }

    @PostMapping("/inscription")
    public String inscrireEnfant(Authentication authentication,
                                 @RequestParam Integer enfantId,
                                 @RequestParam Integer sessionId,
                                 @RequestParam(required = false) String commentaire,
                                 RedirectAttributes redirectAttributes) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        try {
            inscriptionService.inscrireEnfant(enfantId, sessionId, commentaire);
            redirectAttributes.addFlashAttribute("success", "Inscription réussie");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'inscription: " + e.getMessage());
        }

        return "redirect:/parent/inscriptions";
    }

    @PostMapping("/inscription/{id}/annuler")
    public String annulerInscription(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        try {
            inscriptionService.annulerInscription(id);
            redirectAttributes.addFlashAttribute("success", "Inscription annulée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'annulation: " + e.getMessage());
        }

        return "redirect:/parent/inscriptions";
    }

    @GetMapping("/planning")
    public String planning(Authentication authentication, Model model) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        List<InscriptionEnfant> inscriptions = inscriptionService.getInscriptionsFuturesParent(parent.getId());
        model.addAttribute("inscriptions", inscriptions != null ? inscriptions : new ArrayList<>());
        return "parent/planning";
    }

    @GetMapping("/notifications")
    public String notifications(Authentication authentication, Model model) {
        Parent parent = getParentConnecte(authentication);
        if (parent == null) {
            return "redirect:/login";
        }

        List<Communication> communications = communicationService.getCommunicationsParent(parent.getId());
        model.addAttribute("communications", communications != null ? communications : new ArrayList<>());

        communicationService.marquerToutesCommeLues(parent.getId());

        return "parent/notifications";
    }
}
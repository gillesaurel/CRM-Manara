package com.manara.portail.controller;

import com.manara.portail.dto.EnfantDTO;
import com.manara.portail.dto.ParentDTO;
import com.manara.portail.model.*;
import com.manara.portail.security.UserPrincipal;
import com.manara.portail.service.ParentService;
import com.manara.portail.service.InscriptionService;
import com.manara.portail.service.ActiviteService;
import com.manara.portail.service.CommunicationService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/parent")
public class ParentController {

    private final ParentService parentService;
    private final InscriptionService inscriptionService;
    private final ActiviteService activiteService;
    private final CommunicationService communicationService;

    public ParentController(ParentService parentService,
                            InscriptionService inscriptionService,
                            ActiviteService activiteService,
                            CommunicationService communicationService) {
        this.parentService = parentService;
        this.inscriptionService = inscriptionService;
        this.activiteService = activiteService;
        this.communicationService = communicationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        Parent parent = parentService.getParentParId(userPrincipal.getId());
        List<Enfant> enfants = parentService.getEnfantsDuParent(parent.getId());
        List<Inscription> inscriptionsFutures = inscriptionService.getInscriptionsFuturesParent(parent.getId());
        long messagesNonLus = communicationService.getNombreMessagesNonLusParent(parent.getId());

        model.addAttribute("parent", parent);
        model.addAttribute("enfants", enfants);
        model.addAttribute("inscriptions", inscriptionsFutures);
        model.addAttribute("messagesNonLus", messagesNonLus);
        model.addAttribute("prochainesActivites", activiteService.getProchainesSessions(5));

        return "parent/dashboard";
    }

    @GetMapping("/profil")
    public String profil(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        Parent parent = parentService.getParentParId(userPrincipal.getId());

        ParentDTO parentDTO = new ParentDTO();
        parentDTO.setNom(parent.getlNom());
        parentDTO.setPrenom(parent.getpPrenom());
        parentDTO.setEmail(parent.getEmail());
        parentDTO.setTelephone(parent.getTelephone());
        parentDTO.setAdresse(parent.getAdresse());
        parentDTO.setVille(parent.getVille());
        parentDTO.setCodePostal(parent.getCodePostale());
        parentDTO.setTypeLien(parent.getTypeLien());

        model.addAttribute("parent", parentDTO);
        return "parent/profil";
    }

    @PostMapping("/profil/update")
    public String updateProfile(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                @Valid @ModelAttribute("parent") ParentDTO parentDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "parent/profil";
        }

        try {
            parentService.mettreAJourParent(userPrincipal.getId(), parentDTO);
            redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour: " + e.getMessage());
        }

        return "redirect:/parent/profil";
    }

    @GetMapping("/enfants")
    public String listeEnfants(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        List<Enfant> enfants = parentService.getEnfantsDuParent(userPrincipal.getId());
        model.addAttribute("enfants", enfants);
        return "parent/enfants";
    }

    @GetMapping("/enfants/ajouter")
    public String ajouterEnfantForm(Model model) {
        model.addAttribute("enfant", new EnfantDTO());
        return "parent/ajouter-enfant";
    }

    @PostMapping("/enfants/ajouter")
    public String ajouterEnfant(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                @Valid @ModelAttribute("enfant") EnfantDTO enfantDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "parent/ajouter-enfant";
        }

        try {
            parentService.ajouterEnfant(userPrincipal.getId(), enfantDTO);
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
        model.addAttribute("activites", activiteService.getAllActivitesActives());
        return "parent/activites";
    }

    @GetMapping("/activites/{id}/sessions")
    public String voirSessions(@PathVariable Integer id, Model model) {
        model.addAttribute("activite", activiteService.getActiviteById(id));
        model.addAttribute("sessions", activiteService.getSessionsFuturesByActivite(id));
        return "parent/sessions";
    }

    @GetMapping("/inscriptions")
    public String mesInscriptions(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        List<Inscription> inscriptions = inscriptionService.getInscriptionsFuturesParent(userPrincipal.getId());
        model.addAttribute("inscriptions", inscriptions);
        return "parent/inscriptions";
    }

    @PostMapping("/inscription")
    public String inscrireEnfant(@RequestParam Integer enfantId,
                                 @RequestParam Integer sessionId,
                                 @RequestParam(required = false) String commentaire,
                                 RedirectAttributes redirectAttributes) {
        try {
            inscriptionService.inscrireEnfant(enfantId, sessionId, commentaire);
            redirectAttributes.addFlashAttribute("success", "Inscription réussie");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'inscription: " + e.getMessage());
        }

        return "redirect:/parent/inscriptions";
    }

    @PostMapping("/inscription/{id}/annuler")
    public String annulerInscription(@PathVariable Integer id,
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
    public String planning(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        List<Inscription> inscriptions = inscriptionService.getInscriptionsFuturesParent(userPrincipal.getId());
        model.addAttribute("inscriptions", inscriptions);
        return "parent/planning";
    }

    @GetMapping("/notifications")
    public String notifications(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        List<Communication> communications = communicationService.getCommunicationsParent(userPrincipal.getId());
        model.addAttribute("communications", communications);

        // Marquer comme lues
        communicationService.marquerToutesCommeLues(userPrincipal.getId());

        return "parent/notifications";
    }
}
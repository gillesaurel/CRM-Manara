// fait par Gilles Aurel

package com.manara.portail.controller;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.Inscription;
import com.manara.portail.service.ActiviteNotFoundException;
import com.manara.portail.service.ActiviteService;
import com.manara.portail.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ActiviteService activiteService;
    @Autowired
    private SessionService sessionService;

    @GetMapping("/administrateur")
    public String pageAdministrateur(Model model) {
        List<ActiviteAdmin> activites = activiteService.getAllActivites();
        model.addAttribute("activites", activites);
        model.addAttribute("totalActivities", activites.size());
        model.addAttribute("PourcentageInscrit", activiteService.getPourcentageInscription());
        model.addAttribute("activite", new ActiviteAdmin());
        return "administrateur";
    }

    @PostMapping("/activities/save")
    public String saveOrUpdateActivite(@ModelAttribute ActiviteAdmin activite, RedirectAttributes redirectAttributes) {
        activiteService.save(activite);
        redirectAttributes.addFlashAttribute("message",
                "Activité enregistrée avec succès.");

        return "redirect:/admin/administrateur";
    }

    @GetMapping("/activities/edit/{id}")
    public String modifyActivite(@PathVariable Long id, Model model) {
        ActiviteAdmin activite = this.activiteService.getById(id);
        model.addAttribute("activite", activite);
        model.addAttribute("activites", activiteService.getAllActivites());
        return "administrateur";
    }


    @GetMapping("/activities/delete/{id}")
    public String removeActivite(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            activiteService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Activité supprimé avec succès.");
        } catch (ActiviteNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", "Activité introuvable.");
        }
        return "redirect:/admin/administrateur";
    }
}

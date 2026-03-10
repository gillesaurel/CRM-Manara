package com.manara.portail.controller;

import com.manara.portail.model.Activite;
import com.manara.portail.model.Session;
import com.manara.portail.service.ActiviteNotFoundException;
import com.manara.portail.service.ActiviteService;
import com.manara.portail.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ActiviteService activiteService;
    @Autowired
    private SessionService sessionService;

    @GetMapping("/administrateur")
    public String pageAdministrateur(Model model) {
        model.addAttribute("activite", new Activite());
        model.addAttribute("activites", activiteService.getAllActivites());
        return "administrateur";
    }

    @PostMapping("/activities/save")
    public String addActivite(@ModelAttribute Activite activite, RedirectAttributes redirectAttributes) {
        activiteService.save(activite);
        redirectAttributes.addFlashAttribute("message",
                "Activité enregistrée avec succès.");

        return "redirect:/admin/administrateur";
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

package com.manara.portail.controller;

import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.ActiviteRepository;
import com.manara.portail.repository.UtilisateurRepository;
import com.manara.portail.service.InscriptionEnfantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InscriptionEnfantController {
    @Autowired
    private ActiviteRepository activiteRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private InscriptionEnfantService inscriptionEnfantService;

    @GetMapping("inscriptionEnfant")
    public String afficherInscriptionEnfant(Model model) {
        model.addAttribute("activites", this.activiteRepository.findAll());
        return "inscriptionEnfant";
    }

    @PostMapping("/inscriptionEnfant/save")
    public String saveInscriptionEnfant(@RequestParam String nomEnfant,
                                        @RequestParam Long activite_id,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String email = auth.getName();
        Utilisateur parent = utilisateurRepository.findByEmail(email);
        inscriptionEnfantService.InscriptionEnfant(nomEnfant, activite_id, parent);
        redirectAttributes.addFlashAttribute("message", "Inscription confirmée !");
        return "redirect:/index";
    }
}

package com.manara.portail.controller;

import com.manara.portail.dto.ParentDTO;
import com.manara.portail.service.ParentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final ParentService parentService;

    public AuthController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping("/register")
    public String pageInscription(Model model) {
        model.addAttribute("parent", new ParentDTO());
        return "inscription";
    }

    @PostMapping("/register")
    public String traiterInscription(@Valid ParentDTO parentDTO,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "inscription";
        }

        try {
            parentService.inscrireParent(parentDTO);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String pageConnexion() {
        return "connexion";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }
}
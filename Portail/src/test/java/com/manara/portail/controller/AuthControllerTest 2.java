package com.manara.portail.controller;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.service.RoleService;
import com.manara.portail.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;



@WebMvcTest(AuthController.class)

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurService utilService;

    @MockBean
    private RoleService roleService;

    @Test
    void testPageInscription() throws Exception {
        mockMvc.perform(get("/register")).andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testPageInscription() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}

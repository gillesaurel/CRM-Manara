package com.manara.portail.controller;

import com.manara.portail.service.RoleService;
import com.manara.portail.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;


import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;



@WebMvcTest(AuthParentController.class)
public class AuthParentControllerTest {

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

package com.manara.portail.controller;

import com.manara.portail.service.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPageInscription() throws Exception {
        // Test that GET request to "/inscription" returns the correct view
        mockMvc.perform(get("/inscription"))
                .andExpect(status().isOk())  // Expect HTTP status 200 OK
                .andExpect(view().name("inscription"));  // Expect the view name to be "inscription"
    }
}
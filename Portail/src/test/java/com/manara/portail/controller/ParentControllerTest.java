package com.manara.portail.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ParentController.class)
class ParentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPageDashboard() throws Exception {
        mockMvc.perform(get("/parent/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("parent/dashboard"));
    }

    @Test
    void testPageEnfants() throws Exception {
        mockMvc.perform(get("/parent/enfants"))
                .andExpect(status().isOk())
                .andExpect(view().name("parent/enfants"));
    }

    @Test
    void testPageActivites() throws Exception {
        mockMvc.perform(get("/parent/activites"))
                .andExpect(status().isOk())
                .andExpect(view().name("parent/activites"));
    }
}
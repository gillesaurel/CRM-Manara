package com.manara.portail.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminController.class)
class adminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPageAdministrateur() throws Exception {
        mockMvc.perform(get("/administrateur")).andExpect(status().isOk())
                .andExpect(view().name("administrateur"));
    }


}



package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.entity.Utilisateur;
import com.dogtrack.dogtrack_backend.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthControllerIT {

    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private UtilisateurRepository utilisateurRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void login_avecBonsIdentifiants_renvoieUnToken() throws Exception {
        utilisateurRepo.save(Utilisateur.builder()
                .login("testuser")
                .password(passwordEncoder.encode("motdepasse"))
                .nom("Test User")
                .role(Utilisateur.Role.VENDEUR)
                .actif(true)
                .build());

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"login\":\"testuser\",\"password\":\"motdepasse\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("VENDEUR"));
    }

    @Test
    void login_avecMauvaisMotDePasse_renvoie401() throws Exception {
        utilisateurRepo.save(Utilisateur.builder()
                .login("testuser2")
                .password(passwordEncoder.encode("bonmotdepasse"))
                .nom("Test")
                .role(Utilisateur.Role.VENDEUR)
                .actif(true)
                .build());

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"login\":\"testuser2\",\"password\":\"mauvais\"}"))
                .andExpect(status().isUnauthorized());
    }
}
package com.dogtrack.dogtrack_backend.config;

import com.dogtrack.dogtrack_backend.entity.Utilisateur;
import com.dogtrack.dogtrack_backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!utilisateurRepo.existsByLogin("admin")) {
            Utilisateur admin = Utilisateur.builder()
                    .login("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .nom("Administrateur")
                    .role(Utilisateur.Role.ADMIN)
                    .actif(true)
                    .build();
            utilisateurRepo.save(admin);
            System.out.println(">>> Compte admin créé (admin / admin123) — à changer après premier login.");
        }
    }
}
package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.dto.LoginRequest;
import com.dogtrack.dogtrack_backend.dto.LoginResponse;
import com.dogtrack.dogtrack_backend.entity.Utilisateur;
import com.dogtrack.dogtrack_backend.repository.UtilisateurRepository;
import com.dogtrack.dogtrack_backend.security.TokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UtilisateurRepository utilisateurRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest requete) {
        var utilisateurOpt = utilisateurRepo.findByLogin(requete.login());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Identifiant ou mot de passe incorrect");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        if (!Boolean.TRUE.equals(utilisateur.getActif())) {
            return ResponseEntity.status(403).body("Compte désactivé");
        }

        if (!passwordEncoder.matches(requete.password(), utilisateur.getPassword())) {
            return ResponseEntity.status(401).body("Identifiant ou mot de passe incorrect");
        }

        String token = tokenStore.creerToken(utilisateur);

        return ResponseEntity.ok(new LoginResponse(
                token, utilisateur.getLogin(), utilisateur.getNom(), utilisateur.getRole().name()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String header) {
        if (header != null && header.startsWith("Bearer ")) {
            tokenStore.invalider(header.substring(7));
        }
        return ResponseEntity.ok().build();
    }
}
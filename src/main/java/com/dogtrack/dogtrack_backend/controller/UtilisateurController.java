package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.entity.Utilisateur;
import com.dogtrack.dogtrack_backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepo;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAll() {
        // on ne renvoie jamais le hash du mot de passe au client
        utilisateurRepo.findAll().forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(utilisateurRepo.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Utilisateur utilisateur) {
        if (utilisateurRepo.existsByLogin(utilisateur.getLogin())) {
            return ResponseEntity.status(409).body("Ce login existe déjà");
        }
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        utilisateur.setActif(true);
        Utilisateur saved = utilisateurRepo.save(utilisateur);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/desactiver")
    public ResponseEntity<Utilisateur> desactiver(@PathVariable Long id) {
        return utilisateurRepo.findById(id).map(u -> {
            u.setActif(false);
            return ResponseEntity.ok(utilisateurRepo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/reactiver")
    public ResponseEntity<Utilisateur> reactiver(@PathVariable Long id) {
        return utilisateurRepo.findById(id).map(u -> {
            u.setActif(true);
            return ResponseEntity.ok(utilisateurRepo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }
}
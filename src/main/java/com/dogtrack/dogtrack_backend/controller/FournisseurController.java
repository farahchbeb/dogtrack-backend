package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.entity.Fournisseur;
import com.dogtrack.dogtrack_backend.repository.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FournisseurController {

    private final FournisseurRepository fournisseurRepo;

    @GetMapping
    public ResponseEntity<List<Fournisseur>> getAll() {
        return ResponseEntity.ok(fournisseurRepo.findAll());
    }

    @PostMapping
    public ResponseEntity<Fournisseur> create(@RequestBody Fournisseur fournisseur) {
        return ResponseEntity.ok(fournisseurRepo.save(fournisseur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fournisseur> update(@PathVariable Long id, @RequestBody Fournisseur fournisseur) {
        return fournisseurRepo.findById(id).map(existing -> {
            existing.setNom(fournisseur.getNom());
            existing.setContact(fournisseur.getContact());
            existing.setAdresse(fournisseur.getAdresse());
            return ResponseEntity.ok(fournisseurRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fournisseurRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
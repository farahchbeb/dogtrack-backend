package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.dto.FactureRequest;
import com.dogtrack.dogtrack_backend.entity.Facture;
import com.dogtrack.dogtrack_backend.service.FactureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FactureController {

    private final FactureService factureService;

    @GetMapping
    public ResponseEntity<List<Facture>> getAllFactures() {
        return ResponseEntity.ok(factureService.getAllFactures());
    }

    @PostMapping
    public ResponseEntity<Facture> createFacture(@RequestBody FactureRequest req) {
        return ResponseEntity.ok(factureService.createFacture(req));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<Facture> updateStatut(
            @PathVariable Long id,
            @RequestParam Facture.StatutPaiement statut) {
        return ResponseEntity.ok(factureService.updateStatutPaiement(id, statut));
    }

    @GetMapping("/chiffre-affaires")
    public ResponseEntity<Double> getCA() {
        return ResponseEntity.ok(factureService.getChiffreAffaires());
    }
}
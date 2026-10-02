package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.dto.BonLivraisonRequest;
import com.dogtrack.dogtrack_backend.entity.BonLivraison;
import com.dogtrack.dogtrack_backend.entity.Facture;
import com.dogtrack.dogtrack_backend.service.BLService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bl")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BLController {

    private final BLService blService;

    @GetMapping
    public ResponseEntity<List<BonLivraison>> getAll() {
        return ResponseEntity.ok(blService.getAllBL());
    }

    @PostMapping
    public ResponseEntity<BonLivraison> create(@RequestBody BonLivraisonRequest req) {
        return ResponseEntity.ok(blService.createBL(req));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<BonLivraison> updateStatut(
            @PathVariable Long id,
            @RequestParam BonLivraison.StatutBL statut) {
        return ResponseEntity.ok(blService.updateStatut(id, statut));
    }

    @PostMapping("/{id}/convertir-facture")
    public ResponseEntity<Facture> convertir(
            @PathVariable Long id,
            @RequestParam(required = false) Long utilisateurId) {
        return ResponseEntity.ok(blService.convertirEnFacture(id, utilisateurId));
    }
}
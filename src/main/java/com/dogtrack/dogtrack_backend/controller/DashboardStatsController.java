package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.dto.*;
import com.dogtrack.dogtrack_backend.repository.FactureRepository;
import com.dogtrack.dogtrack_backend.repository.LigneFactureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardStatsController {

    private final FactureRepository factureRepo;
    private final LigneFactureRepository ligneFactureRepo;

    @GetMapping("/ca-par-mois")
    public ResponseEntity<List<StatMoisDTO>> caParMois() {
        LocalDateTime sixMoisAvant = LocalDateTime.now().minusMonths(6);
        List<Object[]> resultats = factureRepo.chiffreAffairesParMois(sixMoisAvant);

        List<StatMoisDTO> stats = resultats.stream()
                .map(r -> new StatMoisDTO((String) r[0], ((Number) r[1]).doubleValue()))
                .toList();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/top-produits")
    public ResponseEntity<List<StatProduitDTO>> topProduits() {
        List<Object[]> resultats = ligneFactureRepo.topProduitsVendus(PageRequest.of(0, 5));

        List<StatProduitDTO> stats = resultats.stream()
                .map(r -> new StatProduitDTO((String) r[0], ((Number) r[1]).longValue()))
                .toList();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/repartition-categories")
    public ResponseEntity<List<StatCategorieDTO>> repartitionCategories() {
        List<Object[]> resultats = ligneFactureRepo.repartitionParCategorie();

        List<StatCategorieDTO> stats = resultats.stream()
                .map(r -> new StatCategorieDTO((String) r[0], ((Number) r[1]).doubleValue()))
                .toList();

        return ResponseEntity.ok(stats);
    }
}
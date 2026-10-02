package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.dto.*;
import com.dogtrack.dogtrack_backend.entity.*;
import com.dogtrack.dogtrack_backend.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;

    @GetMapping("/produits")
    public ResponseEntity<List<Produit>> getAllProduits() {
        return ResponseEntity.ok(stockService.getAllProduits());
    }

    @GetMapping("/produits/alertes")
    public ResponseEntity<List<Produit>> getProduitsEnAlerte() {
        return ResponseEntity.ok(stockService.getProduitsEnAlerte());
    }

    @PostMapping("/produits")
    public ResponseEntity<Produit> createProduit(@RequestBody ProduitDTO dto) {
        return ResponseEntity.ok(stockService.createProduit(dto));
    }

    @PostMapping("/mouvement")
    public ResponseEntity<Stock> effectuerMouvement(@RequestBody StockMouvementRequest req) {
        return ResponseEntity.ok(stockService.effectuerMouvement(req));
    }

    @GetMapping("/historique/{produitId}")
    public ResponseEntity<List<MouvementStock>> getHistorique(@PathVariable Long produitId) {
        return ResponseEntity.ok(stockService.getHistoriqueProduit(produitId));
    }
}
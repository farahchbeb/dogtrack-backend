package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.entity.Categorie;
import com.dogtrack.dogtrack_backend.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategorieController {

    private final CategorieRepository categorieRepo;

    @GetMapping
    public ResponseEntity<List<Categorie>> getAll() {
        return ResponseEntity.ok(categorieRepo.findAll());
    }

    @PostMapping
    public ResponseEntity<Categorie> create(@RequestBody Categorie categorie) {
        return ResponseEntity.ok(categorieRepo.save(categorie));
    }
}
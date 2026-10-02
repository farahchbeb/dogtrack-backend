package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.entity.Client;
import com.dogtrack.dogtrack_backend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientRepository clientRepo;

    @GetMapping
    public ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok(clientRepo.findAll());
    }

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody Client client) {
        return ResponseEntity.ok(clientRepo.save(client));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Client>> search(@RequestParam String nom) {
        return ResponseEntity.ok(clientRepo.findByNomContainingIgnoreCase(nom));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client) {
        return clientRepo.findById(id).map(existing -> {
            existing.setNom(client.getNom());
            existing.setContact(client.getContact());
            existing.setEmail(client.getEmail());
            existing.setAdresse(client.getAdresse());
            return ResponseEntity.ok(clientRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
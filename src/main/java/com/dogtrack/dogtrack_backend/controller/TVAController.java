package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.dto.TvaSuggestionDTO;
import com.dogtrack.dogtrack_backend.service.TVAEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tva")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TVAController {

    private final TVAEngineService tvaEngineService;

    @GetMapping("/suggestion")
    public ResponseEntity<TvaSuggestionDTO> suggerer(@RequestParam String designation) {
        TvaSuggestionDTO suggestion = tvaEngineService.suggererTva(designation);
        return suggestion != null
                ? ResponseEntity.ok(suggestion)
                : ResponseEntity.noContent().build();
    }

    @PostMapping("/feedback")
    public ResponseEntity<Void> feedback(@RequestBody java.util.Map<String, Object> body) {
        tvaEngineService.envoyerFeedback(
                (String) body.get("designation"),
                Double.valueOf(body.get("tauxPropose").toString()),
                Double.valueOf(body.get("tauxCorrige").toString())
        );
        return ResponseEntity.ok().build();
    }
}
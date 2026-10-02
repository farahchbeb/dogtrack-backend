package com.dogtrack.dogtrack_backend.service;

import com.dogtrack.dogtrack_backend.dto.TvaSuggestionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TVAEngineService {

    private final RestClient restClient;

    public TVAEngineService(@Value("${tva.engine.url:http://127.0.0.1:8000}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public TvaSuggestionDTO suggererTva(String designation) {
        try {
            return restClient.post()
                    .uri("/predict-tva")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(java.util.Map.of("designation", designation))
                    .retrieve()
                    .body(TvaSuggestionDTO.class);
        } catch (Exception e) {
            // Le microservice IA est optionnel : si indisponible, le backend continue
            // de fonctionner normalement (pas de suggestion, pas de blocage)
            return null;
        }
    }

    public void envoyerFeedback(String designation, Double tauxPropose, Double tauxCorrige) {
        try {
            restClient.post()
                    .uri("/feedback")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(java.util.Map.of(
                            "designation", designation,
                            "taux_propose", tauxPropose,
                            "taux_corrige", tauxCorrige
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ignored) {
            // même logique : le feedback est un bonus, pas un point bloquant
        }
    }
}
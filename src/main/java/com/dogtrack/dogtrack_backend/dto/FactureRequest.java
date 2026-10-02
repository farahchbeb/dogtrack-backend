package com.dogtrack.dogtrack_backend.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureRequest {
    private Long clientId;
    private Long utilisateurId;
    private List<LigneFactureDTO> lignes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LigneFactureDTO {
        private Long produitId;
        private Integer quantite;
    }
}
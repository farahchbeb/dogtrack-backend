package com.dogtrack.dogtrack_backend.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonLivraisonRequest {
    private Long clientId;
    private List<LigneBLDTO> lignes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LigneBLDTO {
        private Long produitId;
        private Integer quantite;
    }
}
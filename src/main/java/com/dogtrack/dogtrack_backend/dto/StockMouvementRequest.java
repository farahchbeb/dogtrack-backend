package com.dogtrack.dogtrack_backend.dto;

import com.dogtrack.dogtrack_backend.entity.MouvementStock;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMouvementRequest {
    private Long produitId;
    private MouvementStock.TypeMouvement type;
    private Integer quantite;
    private String motif;
}
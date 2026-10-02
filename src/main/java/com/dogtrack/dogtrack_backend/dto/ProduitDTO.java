package com.dogtrack.dogtrack_backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitDTO {
    private Long id;
    private String designation;
    private Long categorieId;
    private String categorieNom;
    private Long fournisseurId;
    private String fournisseurNom;
    private Double prixHt;
    private Double tauxTva;
    private Double prixTtc;
    private Integer seuilAlerte;
    private Integer quantiteStock;
    private Boolean actif;
}
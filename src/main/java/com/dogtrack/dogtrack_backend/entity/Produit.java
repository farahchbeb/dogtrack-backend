package com.dogtrack.dogtrack_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String designation;

    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @Column(name = "prix_ht", nullable = false)
    private Double prixHt;

    @Column(name = "taux_tva", nullable = false)
    private Double tauxTva;

    @Column(name = "seuil_alerte", nullable = false)
    private Integer seuilAlerte;

    @Column(nullable = false)
    private Boolean actif = true;
}
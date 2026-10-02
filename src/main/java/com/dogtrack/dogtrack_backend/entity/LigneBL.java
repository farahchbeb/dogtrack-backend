package com.dogtrack.dogtrack_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes_bl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneBL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bl_id", nullable = false)
    @JsonIgnore
    private BonLivraison bonLivraison;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite;
}
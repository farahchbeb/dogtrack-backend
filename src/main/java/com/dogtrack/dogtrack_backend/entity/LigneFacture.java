package com.dogtrack.dogtrack_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes_facture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "facture_id", nullable = false)
    @JsonIgnore
    private Facture facture;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "taux_tva_applique", nullable = false)
    private Double tauxTvaApplique;

    @Column(name = "montant_ligne")
    private Double montantLigne;

    @PrePersist
    @PreUpdate
    public void calculMontant() {
        this.montantLigne = this.prixUnitaire * this.quantite * (1 + this.tauxTvaApplique / 100);
    }
}
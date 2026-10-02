package com.dogtrack.dogtrack_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "factures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    public enum StatutPaiement { EN_ATTENTE, PARTIEL, PAYE, ANNULE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "date_facture")
    private LocalDateTime dateFacture;

    @Column(name = "montant_ht")
    private Double montantHt;

    @Column(name = "montant_tva")
    private Double montantTva;

    @Column(name = "montant_ttc")
    private Double montantTtc;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_paiement")
    private StatutPaiement statutPaiement = StatutPaiement.EN_ATTENTE;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL)
    private List<LigneFacture> lignes;

    @PrePersist
    public void setDate() {
        this.dateFacture = LocalDateTime.now();
    }
}
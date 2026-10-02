package com.dogtrack.dogtrack_backend.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bons_livraison")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BonLivraison {

    public enum StatutBL { PREPARE, LIVRE, SIGNE, ANNULE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture;

    @Column(name = "date_bl")
    private LocalDateTime dateBl;

    @Enumerated(EnumType.STRING)
    private StatutBL statut = StatutBL.PREPARE;

    @OneToMany(mappedBy = "bonLivraison", cascade = CascadeType.ALL)
    private List<LigneBL> lignes;

    @PrePersist
    public void setDate() {
        this.dateBl = LocalDateTime.now();
    }
}
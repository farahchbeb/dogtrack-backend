package com.dogtrack.dogtrack_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvements_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MouvementStock {

    public enum TypeMouvement { ENTREE, SORTIE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvement type;

    @Column(nullable = false)
    private Integer quantite;

    private String motif;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @PrePersist
    public void setDate() {
        this.dateMouvement = LocalDateTime.now();
    }
}
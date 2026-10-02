package com.dogtrack.dogtrack_backend.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "produit_id", nullable = false, unique = true)
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "derniere_maj")
    private LocalDateTime derniereMaj;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.derniereMaj = LocalDateTime.now();
    }
}
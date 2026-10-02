package com.dogtrack.dogtrack_backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fournisseurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String contact;
    private String adresse;
}
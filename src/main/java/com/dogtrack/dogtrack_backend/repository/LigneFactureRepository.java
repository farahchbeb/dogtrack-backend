package com.dogtrack.dogtrack_backend.repository;

import com.dogtrack.dogtrack_backend.entity.LigneFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LigneFactureRepository extends JpaRepository<LigneFacture, Long> {

    @Query("""
           SELECT lf.produit.designation AS designation, SUM(lf.quantite) AS totalVendu
           FROM LigneFacture lf
           GROUP BY lf.produit.designation
           ORDER BY totalVendu DESC
           """)
    List<Object[]> topProduitsVendus(Pageable pageable);

    @Query("""
           SELECT lf.produit.categorie.nom AS categorie, SUM(lf.montantLigne) AS total
           FROM LigneFacture lf
           GROUP BY lf.produit.categorie.nom
           ORDER BY total DESC
           """)
    List<Object[]> repartitionParCategorie();
}
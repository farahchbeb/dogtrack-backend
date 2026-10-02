package com.dogtrack.dogtrack_backend.repository;

import com.dogtrack.dogtrack_backend.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByActifTrue();
    List<Produit> findByCategorieId(Long categorieId);

    @Query("""
        SELECT p FROM Produit p
        JOIN Stock s ON s.produit.id = p.id
        WHERE s.quantite <= p.seuilAlerte AND p.actif = true
    """)
    List<Produit> findProduitsEnAlerte();
}
package com.dogtrack.dogtrack_backend.repository;

import com.dogtrack.dogtrack_backend.entity.MouvementStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {
    List<MouvementStock> findByProduitIdOrderByDateMouvementDesc(Long produitId);
}
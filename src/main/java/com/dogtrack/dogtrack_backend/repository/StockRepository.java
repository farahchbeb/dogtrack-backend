package com.dogtrack.dogtrack_backend.repository;

import com.dogtrack.dogtrack_backend.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProduitId(Long produitId);
}
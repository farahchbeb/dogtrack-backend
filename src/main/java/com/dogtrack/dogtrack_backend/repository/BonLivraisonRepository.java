package com.dogtrack.dogtrack_backend.repository;

import com.dogtrack.dogtrack_backend.entity.BonLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BonLivraisonRepository extends JpaRepository<BonLivraison, Long> {
    List<BonLivraison> findByClientId(Long clientId);
    List<BonLivraison> findByStatut(BonLivraison.StatutBL statut);

    @Query("""
           SELECT b FROM BonLivraison b
           WHERE (:clientNom IS NULL OR LOWER(b.client.nom) LIKE LOWER(CONCAT('%', :clientNom, '%')))
           AND (:dateDebut IS NULL OR b.dateBl >= :dateDebut)
           AND (:dateFin IS NULL OR b.dateBl <= :dateFin)
           AND (:statut IS NULL OR b.statut = :statut)
           ORDER BY b.dateBl DESC
           """)
    List<BonLivraison> rechercheArchive(@Param("clientNom") String clientNom,
                                        @Param("dateDebut") LocalDateTime dateDebut,
                                        @Param("dateFin") LocalDateTime dateFin,
                                        @Param("statut") BonLivraison.StatutBL statut);
}
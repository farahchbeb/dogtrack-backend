package com.dogtrack.dogtrack_backend.repository;

import com.dogtrack.dogtrack_backend.entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FactureRepository extends JpaRepository<Facture, Long> {
    List<Facture> findByClientId(Long clientId);
    List<Facture> findByStatutPaiement(Facture.StatutPaiement statut);

    @Query("SELECT SUM(f.montantTtc) FROM Facture f WHERE f.statutPaiement = 'PAYE'")
    Double sumChiffreAffaires();

    @Query("""
           SELECT f FROM Facture f
           WHERE (:clientNom IS NULL OR LOWER(f.client.nom) LIKE LOWER(CONCAT('%', :clientNom, '%')))
           AND (:dateDebut IS NULL OR f.dateFacture >= :dateDebut)
           AND (:dateFin IS NULL OR f.dateFacture <= :dateFin)
           AND (:statut IS NULL OR f.statutPaiement = :statut)
           ORDER BY f.dateFacture DESC
           """)
    List<Facture> rechercheArchive(@Param("clientNom") String clientNom,
                                   @Param("dateDebut") LocalDateTime dateDebut,
                                   @Param("dateFin") LocalDateTime dateFin,
                                   @Param("statut") Facture.StatutPaiement statut);

    @Query(value = """
           SELECT DATE_FORMAT(f.date_facture, '%Y-%m') AS mois, SUM(f.montant_ttc) AS total
           FROM factures f
           WHERE f.statut_paiement = 'PAYE' AND f.date_facture >= :dateDebut
           GROUP BY DATE_FORMAT(f.date_facture, '%Y-%m')
           ORDER BY mois
           """, nativeQuery = true)
    List<Object[]> chiffreAffairesParMois(@Param("dateDebut") LocalDateTime dateDebut);
}
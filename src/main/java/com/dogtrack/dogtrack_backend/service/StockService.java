package com.dogtrack.dogtrack_backend.service;

import com.dogtrack.dogtrack_backend.dto.*;
import com.dogtrack.dogtrack_backend.entity.*;
import com.dogtrack.dogtrack_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProduitRepository produitRepo;
    private final StockRepository stockRepo;
    private final MouvementStockRepository mouvementRepo;
    private final CategorieRepository categorieRepo;
    private final FournisseurRepository fournisseurRepo;

    public List<Produit> getAllProduits() {
        return produitRepo.findByActifTrue();
    }

    public List<Produit> getProduitsEnAlerte() {
        return produitRepo.findProduitsEnAlerte();
    }

    @Transactional
    public Produit createProduit(ProduitDTO dto) {
        Categorie cat = categorieRepo.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        Produit produit = Produit.builder()
                .designation(dto.getDesignation())
                .categorie(cat)
                .prixHt(dto.getPrixHt())
                .tauxTva(dto.getTauxTva() != null ? dto.getTauxTva() : cat.getTauxTvaDefaut())
                .seuilAlerte(dto.getSeuilAlerte())
                .actif(true)
                .build();

        if (dto.getFournisseurId() != null) {
            produit.setFournisseur(fournisseurRepo.findById(dto.getFournisseurId()).orElse(null));
        }

        Produit saved = produitRepo.save(produit);

        Stock stock = Stock.builder()
                .produit(saved)
                .quantite(0)
                .build();
        stockRepo.save(stock);

        return saved;
    }

    @Transactional
    public Stock effectuerMouvement(StockMouvementRequest req) {
        Produit produit = produitRepo.findById(req.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        Stock stock = stockRepo.findByProduitId(req.getProduitId())
                .orElseThrow(() -> new RuntimeException("Stock introuvable"));

        if (req.getType() == MouvementStock.TypeMouvement.ENTREE) {
            stock.setQuantite(stock.getQuantite() + req.getQuantite());
        } else {
            if (stock.getQuantite() < req.getQuantite()) {
                throw new RuntimeException("Stock insuffisant !");
            }
            stock.setQuantite(stock.getQuantite() - req.getQuantite());
        }

        stockRepo.save(stock);

        MouvementStock mouvement = MouvementStock.builder()
                .produit(produit)
                .type(req.getType())
                .quantite(req.getQuantite())
                .motif(req.getMotif())
                .build();
        mouvementRepo.save(mouvement);

        return stock;
    }

    public List<MouvementStock> getHistoriqueProduit(Long produitId) {
        return mouvementRepo.findByProduitIdOrderByDateMouvementDesc(produitId);
    }
}
package com.dogtrack.dogtrack_backend.service;

import com.dogtrack.dogtrack_backend.dto.StockMouvementRequest;
import com.dogtrack.dogtrack_backend.entity.*;
import com.dogtrack.dogtrack_backend.repository.MouvementStockRepository;
import com.dogtrack.dogtrack_backend.repository.ProduitRepository;
import com.dogtrack.dogtrack_backend.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock private ProduitRepository produitRepo;
    @Mock private StockRepository stockRepo;
    @Mock private MouvementStockRepository mouvementRepo;   // 👈 LIGNE AJOUTÉE

    @InjectMocks
    private StockService stockService;

    private Produit produit;
    private Stock stock;

    @BeforeEach
    void setUp() {
        produit = Produit.builder().id(1L).designation("Laisse cuir").build();
        stock = Stock.builder().id(1L).produit(produit).quantite(10).build();
    }

    @Test
    void entree_stock_augmenteLaQuantite() {
        when(produitRepo.findById(1L)).thenReturn(Optional.of(produit));
        when(stockRepo.findByProduitId(1L)).thenReturn(Optional.of(stock));
        when(stockRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        StockMouvementRequest req = new StockMouvementRequest(1L, MouvementStock.TypeMouvement.ENTREE, 5, "Réappro");
        Stock resultat = stockService.effectuerMouvement(req);

        assertEquals(15, resultat.getQuantite());
        verify(mouvementRepo, times(1)).save(any());   // bonus : vérifie que l'historique est bien enregistré
    }

    @Test
    void sortie_stock_suffisant_diminueLaQuantite() {
        when(produitRepo.findById(1L)).thenReturn(Optional.of(produit));
        when(stockRepo.findByProduitId(1L)).thenReturn(Optional.of(stock));
        when(stockRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        StockMouvementRequest req = new StockMouvementRequest(1L, MouvementStock.TypeMouvement.SORTIE, 4, "Vente");
        Stock resultat = stockService.effectuerMouvement(req);

        assertEquals(6, resultat.getQuantite());
    }

    @Test
    void sortie_stock_insuffisant_leveUneException() {
        when(produitRepo.findById(1L)).thenReturn(Optional.of(produit));
        when(stockRepo.findByProduitId(1L)).thenReturn(Optional.of(stock));

        StockMouvementRequest req = new StockMouvementRequest(1L, MouvementStock.TypeMouvement.SORTIE, 999, "Vente");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> stockService.effectuerMouvement(req));
        assertEquals("Stock insuffisant !", ex.getMessage());
    }
}
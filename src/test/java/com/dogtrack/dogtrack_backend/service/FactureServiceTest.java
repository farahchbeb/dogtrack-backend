package com.dogtrack.dogtrack_backend.service;

import com.dogtrack.dogtrack_backend.dto.FactureRequest;
import com.dogtrack.dogtrack_backend.entity.Client;
import com.dogtrack.dogtrack_backend.entity.Facture;
import com.dogtrack.dogtrack_backend.entity.Produit;
import com.dogtrack.dogtrack_backend.repository.ClientRepository;
import com.dogtrack.dogtrack_backend.repository.ProduitRepository;
import com.dogtrack.dogtrack_backend.repository.FactureRepository;
import com.dogtrack.dogtrack_backend.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactureServiceTest {

    @Mock private FactureRepository factureRepo;
    @Mock private ClientRepository clientRepo;
    @Mock private ProduitRepository produitRepo;
    @Mock private StockService stockService;
    @Mock private UtilisateurRepository utilisateurRepo;

    @InjectMocks
    private FactureService factureService;

    private Client client;
    private Produit produit;

    @BeforeEach
    void setUp() {
        client = Client.builder().id(1L).nom("Client Test").build();

        produit = Produit.builder()
                .id(1L)
                .designation("Collier de dressage électronique")
                .prixHt(50.0)
                .tauxTva(13.0)
                .build();
    }

    @Test
    void createFacture_calculeCorrectementHtTvaTtc() {
        // Arrange : une facture avec une ligne de 3 unités à 50 TND HT, TVA 13%
        FactureRequest.LigneFactureDTO ligneDto = new FactureRequest.LigneFactureDTO();
        ligneDto.setProduitId(1L);
        ligneDto.setQuantite(3);

        FactureRequest req = new FactureRequest();
        req.setClientId(1L);
        req.setLignes(List.of(ligneDto));

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(produitRepo.findById(1L)).thenReturn(Optional.of(produit));
        when(factureRepo.save(any(Facture.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Facture resultat = factureService.createFacture(req);

        // Assert : HT = 50 * 3 = 150 | TVA = 150 * 13% = 19.5 | TTC = 169.5
        assertEquals(150.0, resultat.getMontantHt(), 0.001);
        assertEquals(19.5, resultat.getMontantTva(), 0.001);
        assertEquals(169.5, resultat.getMontantTtc(), 0.001);

        // Vérifie que le mouvement de stock (sortie) a bien été déclenché
        verify(stockService, times(1)).effectuerMouvement(any());
    }

    @Test
    void createFacture_clientInexistant_leveUneException() {
        FactureRequest req = new FactureRequest();
        req.setClientId(999L);
        req.setLignes(List.of());

        when(clientRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> factureService.createFacture(req));
    }
}
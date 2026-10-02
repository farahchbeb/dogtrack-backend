package com.dogtrack.dogtrack_backend.service;

import com.dogtrack.dogtrack_backend.dto.BonLivraisonRequest;
import com.dogtrack.dogtrack_backend.dto.FactureRequest;
import com.dogtrack.dogtrack_backend.entity.*;
import com.dogtrack.dogtrack_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BLService {

    private final BonLivraisonRepository blRepo;
    private final ClientRepository clientRepo;
    private final ProduitRepository produitRepo;
    private final FactureService factureService;

    @Transactional
    public BonLivraison createBL(BonLivraisonRequest req) {
        Client client = clientRepo.findById(req.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        BonLivraison bl = BonLivraison.builder()
                .client(client)
                .statut(BonLivraison.StatutBL.PREPARE)
                .lignes(new ArrayList<>())
                .build();

        for (BonLivraisonRequest.LigneBLDTO ligneDto : req.getLignes()) {
            Produit produit = produitRepo.findById(ligneDto.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable : " + ligneDto.getProduitId()));

            LigneBL ligne = LigneBL.builder()
                    .bonLivraison(bl)
                    .produit(produit)
                    .quantite(ligneDto.getQuantite())
                    .build();

            bl.getLignes().add(ligne);
        }

        return blRepo.save(bl);
    }

    public List<BonLivraison> getAllBL() {
        return blRepo.findAll();
    }

    public List<BonLivraison> getByClient(Long clientId) {
        return blRepo.findByClientId(clientId);
    }

    @Transactional
    public BonLivraison updateStatut(Long id, BonLivraison.StatutBL statut) {
        BonLivraison bl = blRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("BL introuvable"));
        bl.setStatut(statut);
        return blRepo.save(bl);
    }

    @Transactional
    public Facture convertirEnFacture(Long blId, Long utilisateurId) {
        BonLivraison bl = blRepo.findById(blId)
                .orElseThrow(() -> new RuntimeException("BL introuvable"));

        if (bl.getFacture() != null) {
            throw new RuntimeException("Ce BL a déjà été converti en facture");
        }

        FactureRequest factureReq = new FactureRequest();
        factureReq.setClientId(bl.getClient().getId());
        factureReq.setUtilisateurId(utilisateurId);
        factureReq.setLignes(
                bl.getLignes().stream()
                        .map(l -> new FactureRequest.LigneFactureDTO(l.getProduit().getId(), l.getQuantite()))
                        .collect(Collectors.toList())
        );

        Facture facture = factureService.createFacture(factureReq);

        bl.setFacture(facture);
        bl.setStatut(BonLivraison.StatutBL.LIVRE);
        blRepo.save(bl);

        return facture;
    }
}
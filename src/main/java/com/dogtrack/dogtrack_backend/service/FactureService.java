package com.dogtrack.dogtrack_backend.service;

import com.dogtrack.dogtrack_backend.dto.*;
import com.dogtrack.dogtrack_backend.entity.*;
import com.dogtrack.dogtrack_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepo;
    private final ClientRepository clientRepo;
    private final ProduitRepository produitRepo;
    private final StockService stockService;
    private final UtilisateurRepository utilisateurRepo;

    @Transactional
    public Facture createFacture(FactureRequest req) {
        Client client = clientRepo.findById(req.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        Facture facture = Facture.builder()
                .client(client)
                .statutPaiement(Facture.StatutPaiement.EN_ATTENTE)
                .lignes(new ArrayList<>())
                .build();

        if (req.getUtilisateurId() != null) {
            utilisateurRepo.findById(req.getUtilisateurId())
                    .ifPresent(facture::setUtilisateur);
        }

        double totalHt = 0, totalTva = 0;

        for (FactureRequest.LigneFactureDTO ligneDto : req.getLignes()) {
            Produit produit = produitRepo.findById(ligneDto.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable : " + ligneDto.getProduitId()));

            double montantHtLigne = produit.getPrixHt() * ligneDto.getQuantite();
            double montantTvaLigne = montantHtLigne * produit.getTauxTva() / 100;

            LigneFacture ligne = LigneFacture.builder()
                    .facture(facture)
                    .produit(produit)
                    .quantite(ligneDto.getQuantite())
                    .prixUnitaire(produit.getPrixHt())
                    .tauxTvaApplique(produit.getTauxTva())
                    .build();

            facture.getLignes().add(ligne);
            totalHt += montantHtLigne;
            totalTva += montantTvaLigne;

            StockMouvementRequest mvt = new StockMouvementRequest(
                    produit.getId(),
                    MouvementStock.TypeMouvement.SORTIE,
                    ligneDto.getQuantite(),
                    "Vente facture"
            );
            stockService.effectuerMouvement(mvt);
        }

        facture.setMontantHt(totalHt);
        facture.setMontantTva(totalTva);
        facture.setMontantTtc(totalHt + totalTva);

        return factureRepo.save(facture);
    }

    public List<Facture> getAllFactures() {
        return factureRepo.findAll();
    }

    @Transactional
    public Facture updateStatutPaiement(Long id, Facture.StatutPaiement statut) {
        Facture facture = factureRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));
        facture.setStatutPaiement(statut);
        return factureRepo.save(facture);
    }

    public Double getChiffreAffaires() {
        Double ca = factureRepo.sumChiffreAffaires();
        return ca != null ? ca : 0.0;
    }
}
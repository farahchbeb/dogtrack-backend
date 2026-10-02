package com.dogtrack.dogtrack_backend.controller;

import com.dogtrack.dogtrack_backend.entity.BonLivraison;
import com.dogtrack.dogtrack_backend.entity.Facture;
import com.dogtrack.dogtrack_backend.repository.BonLivraisonRepository;
import com.dogtrack.dogtrack_backend.repository.FactureRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/archive")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ArchiveController {

    private final FactureRepository factureRepo;
    private final BonLivraisonRepository blRepo;

    // =========================================================
    // RECHERCHE DES FACTURES
    // =========================================================

    @GetMapping("/factures")
    public ResponseEntity<List<Facture>> rechercherFactures(
            @RequestParam(required = false) String client,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateDebut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFin,
            @RequestParam(required = false)
            Facture.StatutPaiement statut) {

        return ResponseEntity.ok(
                factureRepo.rechercheArchive(
                        client,
                        dateDebut != null
                                ? dateDebut.atStartOfDay()
                                : null,
                        dateFin != null
                                ? dateFin.atTime(23, 59, 59)
                                : null,
                        statut
                )
        );
    }

    // =========================================================
    // RECHERCHE DES BONS DE LIVRAISON
    // =========================================================

    @GetMapping("/bl")
    public ResponseEntity<List<BonLivraison>> rechercherBL(
            @RequestParam(required = false) String client,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateDebut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFin,
            @RequestParam(required = false)
            BonLivraison.StatutBL statut) {

        return ResponseEntity.ok(
                blRepo.rechercheArchive(
                        client,
                        dateDebut != null
                                ? dateDebut.atStartOfDay()
                                : null,
                        dateFin != null
                                ? dateFin.atTime(23, 59, 59)
                                : null,
                        statut
                )
        );
    }

    // =========================================================
    // EXPORT CSV DES FACTURES
    // =========================================================

    @GetMapping("/factures/export")
    public void exporterFactures(
            @RequestParam(required = false) String client,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateDebut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFin,
            @RequestParam(required = false)
            Facture.StatutPaiement statut,
            HttpServletResponse response) throws IOException {

        // Recherche des factures avec les mêmes filtres
        List<Facture> factures = factureRepo.rechercheArchive(
                client,
                dateDebut != null
                        ? dateDebut.atStartOfDay()
                        : null,
                dateFin != null
                        ? dateFin.atTime(23, 59, 59)
                        : null,
                statut
        );

        // Configuration de la réponse CSV
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=factures_archive.csv"
        );

        PrintWriter writer = response.getWriter();

        // BOM UTF-8 pour une bonne reconnaissance par Excel
        writer.write('\uFEFF');

        // En-tête du fichier CSV
        writer.println(
                "ID;Client;Date;Montant HT;Montant TVA;Montant TTC;Statut"
        );

        // Contenu du fichier
        for (Facture f : factures) {

            writer.printf(
                    "%d;%s;%s;%.3f;%.3f;%.3f;%s%n",
                    f.getId(),
                    f.getClient().getNom(),
                    f.getDateFacture(),
                    f.getMontantHt(),
                    f.getMontantTva(),
                    f.getMontantTtc(),
                    f.getStatutPaiement()
            );
        }

        writer.flush();
    }

    // =========================================================
    // EXPORT CSV DES BONS DE LIVRAISON
    // =========================================================

    @GetMapping("/bl/export")
    public void exporterBL(
            @RequestParam(required = false) String client,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateDebut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFin,
            @RequestParam(required = false)
            BonLivraison.StatutBL statut,
            HttpServletResponse response) throws IOException {

        // Recherche des BL avec les mêmes filtres
        List<BonLivraison> bls = blRepo.rechercheArchive(
                client,
                dateDebut != null
                        ? dateDebut.atStartOfDay()
                        : null,
                dateFin != null
                        ? dateFin.atTime(23, 59, 59)
                        : null,
                statut
        );

        // Configuration de la réponse CSV
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=bl_archive.csv"
        );

        PrintWriter writer = response.getWriter();

        // BOM UTF-8 pour Excel
        writer.write('\uFEFF');

        // En-tête du fichier CSV
        writer.println("ID;Client;Date;Statut");

        // Contenu du fichier
        for (BonLivraison b : bls) {

            writer.printf(
                    "%d;%s;%s;%s%n",
                    b.getId(),
                    b.getClient().getNom(),
                    b.getDateBl(),
                    b.getStatut()
            );
        }

        writer.flush();
    }
}
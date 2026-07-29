package com.pharmastock.pharmastock.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Service
public class MedicamentLookupService {

    private final RestClient restClient;

    public MedicamentLookupService() {
        this.restClient = RestClient.builder().build();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> chercherProduitParCodeBarre(String codeBarre) {
        try {
            // URL ciblée sur Open Beauty Facts
            String url = "https://world.openbeautyfacts.org/api/v0/product/3282779003131.json";

            Map<String, Object> reponse = restClient.get()
                    .uri(url)
                    .header("User-Agent", "PharmaStock - Java Application - Version L3")
                    .retrieve()
                    .body(Map.class);

            // Log d'inspection crucial pour le debug sur Render
            System.out.println("=== DEBUG PHARMASTOCK - REPONSE REÇUE ===");
            System.out.println(reponse);

            if (reponse != null) {
                // Gestion souple et unique du status (qu'il soit Integer ou String)
                Object statusObj = reponse.get("status");
                String status = statusObj != null ? String.valueOf(statusObj) : "";

                if ("1".equals(status) || "1.0".equals(status)) {
                    Map<String, Object> productData = (Map<String, Object>) reponse.get("product");

                    if (productData != null) {
                        return Map.of(
                                "statut", "trouve",
                                "nom", productData.getOrDefault("product_name", "Produit inconnu"),
                                "marque", productData.getOrDefault("brands", "Marque inconnue"),
                                "image", productData.getOrDefault("image_url", ""));
                    }
                }
            }

        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("=== PRODUIT INCONNU OU INVALIDE CORRESPONDANT AU CODE : " + codeBarre + " ===");
            System.out.println(e.getResponseBodyAsString());

            return Map.of(
                    "statut", "introuvable",
                    "message", "Produit non référencé ou mauvais type (ex: alimentaire dans Open Food Facts)");

        } catch (Exception e) {
            System.out.println("=== ERREUR TECHNIQUE PHARMASTOCK API LOOKUP ===");
            e.printStackTrace();
            System.out.println("=====================================");

            return Map.of("statut", "erreur", "message", "Impossible de joindre l'API externe");
        }

        return Map.of("statut", "introuvable", "message", "Code-barres non validé par l'API (Statut incorrect)");
    }
}
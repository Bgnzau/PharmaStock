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
            String url = "https://world.openbeautyfacts.org/api/v0/product/" + codeBarre + ".json";

            Map<String, Object> reponse = restClient.get()
                    .uri(url)
                    .header("User-Agent", "PharmaStock - Java Application - Version L3")
                    .retrieve()
                    .body(Map.class);

            if (reponse != null) {
                // Conversion sécurisée du status en String ("1" pour trouvé)
                String status = String.valueOf(reponse.get("status"));

                if ("1".equals(status)) {
                    Map<String, Object> productData = (Map<String, Object>) reponse.get("product");

                    return Map.of(
                            "statut", "trouve",
                            "nom", productData.getOrDefault("product_name", "Produit inconnu"),
                            "marque", productData.getOrDefault("brands", "Marque inconnue"),
                            "image", productData.getOrDefault("image_url", ""));
                }
            }

        } catch (HttpClientErrorException.NotFound e) {
            // Si l'API répond 404 (ex: produit alimentaire ou inexistant)
            System.out.println("=== PRODUIT INCONNU OU INVALIDE CORRESPONDANT AU CODE : " + codeBarre + " ===");

            // Si l'API donne un message spécifique (comme pour la nourriture), on l'affiche
            // dans Render
            System.out.println(e.getResponseBodyAsString());

            return Map.of("statut", "introuvable", "message",
                    "Produit non référencé ou mauvais type (ex: alimentaire)");

        } catch (Exception e) {
            // Pour toute autre erreur technique (réseau, coupure API...)
            System.out.println("=== ERREUR TECHNIQUE PHARMASTOCK API LOOKUP ===");
            e.printStackTrace();
            System.out.println("=====================================");

            return Map.of("statut", "erreur", "message", "Impossible de joindre l'API externe");
        }

        return Map.of("statut", "introuvable", "message", "Produit non référencé");
    }
}
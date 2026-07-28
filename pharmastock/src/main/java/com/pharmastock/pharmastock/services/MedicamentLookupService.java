package com.pharmastock.pharmastock.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Service
public class MedicamentLookupService {

    // 1. On initialise le client HTTP moderne de Spring Boot 3
    private final RestClient restClient;

    public MedicamentLookupService() {
        this.restClient = RestClient.builder().build();
    }

    /**
     * Cette méthode appelle l'API externe avec un code-barres
     * et renvoie les informations du produit sous forme de Map (Clé/Value).
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> chercherProduitParCodeBarre(String codeBarre) {
        try {
            // 2. On construit l'URL de l'API externe avec le code-barres reçu
            String url = "https://world.openbeautyfacts.org/api/v0/product/" + codeBarre + ".json";

            // 3. On envoie la requête GET avec un User-Agent (requis par Open Beauty Facts)
            Map<String, Object> reponse = restClient.get()
                    .uri(url)
                    .header("User-Agent", "PharmaStock - Java Application - Version L3") // Identifie proprement
                                                                                         // l'application
                    .retrieve()
                    .body(Map.class);

            // 4. On extrait les données utiles si le produit existe
            if (reponse != null && Integer.valueOf(1).equals(reponse.get("status"))) {
                Map<String, Object> productData = (Map<String, Object>) reponse.get("product");

                // On crée une réponse propre et simplifiée pour notre application
                return Map.of(
                        "statut", "trouve",
                        "nom", productData.getOrDefault("product_name", "Produit inconnu"),
                        "marque", productData.getOrDefault("brands", "Marque inconnue"),
                        "image", productData.getOrDefault("image_url", ""));
            }

        } catch (Exception e) {
            // AJOUT DE SÉCURITÉ POUR LE DEBUGGING : Affiche l'erreur complète dans les logs
            // Render !
            System.out.println("=== ERREUR PHARMASTOCK API LOOKUP ===");
            e.printStackTrace();
            System.out.println("=====================================");

            // En cas de problème réseau, on renvoie une map d'erreur propre
            return Map.of("statut", "erreur", "message", "Impossible de joindre l'API externe");
        }

        return Map.of("statut", "introuvable", "message", "Produit non référencé");
    }
}
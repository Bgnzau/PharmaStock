package com.pharmastock.pharmastock.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ApiExterneService {

    private final RestTemplate restTemplate;

    public ApiExterneService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Cette méthode va consommer une API RESTful externe
    public String recupererDescriptionAutomatique(String nomMedicament) {
        try {
            // On appelle une API de test gratuite qui renvoie du JSON descriptif
            String url = "https://jsonplaceholder.typicode.com/posts/1"; // Exemple d'API REST

            // RestTemplate consomme l'API et convertit le JSON reçu en Map Java
            Map<String, Object> reponse = restTemplate.getForObject(url, Map.class);

            if (reponse != null && reponse.containsKey("body")) {
                // On récupère le texte du JSON
                return "Notice auto pour " + nomMedicament + " : " + reponse.get("body").toString();
            }
        } catch (Exception e) {
            return "Impossible de charger la notice automatique pour " + nomMedicament;
        }
        return "Aucune information trouvée.";
    }
}
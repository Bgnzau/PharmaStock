package com.pharmastock.pharmastock.controllers;

import com.pharmastock.pharmastock.models.Medicament;
import com.pharmastock.pharmastock.repositories.MedicamentRepository;
import com.pharmastock.pharmastock.services.MedicamentLookupService; // 1. On importe le service d'API externe
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // 2. On importe PathVariable pour le code-barres
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map; // 3. On importe Map

@RestController
@RequestMapping("/api/medicaments")
public class MedicamentRestController {

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private MedicamentLookupService medicamentLookupService; // 4. On injecte proprement le service créé ensemble

    // Ton API existante pour les alertes de stock
    @GetMapping("/alerte-stock")
    public List<Medicament> getProduitsEnAlerte() {
        return medicamentRepository.findByQuantiteStockLessThan(5);
    }

    // 5. On ajoute notre nouvelle API REST qui consomme l'API externe !
    // Accessible sur : GET http://localhost:8085/api/medicaments/lookup/{codeBarre}
    @GetMapping("/lookup/{codeBarre}")
    public Map<String, Object> lookupMedicament(@PathVariable String codeBarre) {
        return medicamentLookupService.chercherProduitParCodeBarre(codeBarre);
    }
}
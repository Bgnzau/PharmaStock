package com.pharmastock.pharmastock.controllers;

import com.pharmastock.pharmastock.models.Medicament;
import com.pharmastock.pharmastock.repositories.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/medicaments")
public class MedicamentRestController {

    @Autowired
    private MedicamentRepository medicamentRepository;

    // Cette API sera accessible sur
    // http://localhost:8080/api/medicaments/alerte-stock
    @GetMapping("/alerte-stock")
    public List<Medicament> getProduitsEnAlerte() {
        // Remplace "findByQuantiteLessThan(5)" par une méthode qui existe déjà dans ton
        // Repository,
        // ou ajoute-la simplement dans ton MedicamentRepository pour que ça marche !
        return medicamentRepository.findByQuantiteStockLessThan(5);
    }
}
package com.pharmastock.pharmastock.services;

import com.pharmastock.pharmastock.models.Medicament;
import com.pharmastock.pharmastock.models.Vente;
import com.pharmastock.pharmastock.repositories.MedicamentRepository;
import com.pharmastock.pharmastock.repositories.VenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VenteService {

    private final VenteRepository venteRepository;
    private final MedicamentRepository medicamentRepository;

    public VenteService(VenteRepository venteRepository, MedicamentRepository medicamentRepository) {
        this.venteRepository = venteRepository;
        this.medicamentRepository = medicamentRepository;
    }

    public List<Vente> getAllVentes() {
        return venteRepository.findAll();
    }

    @Transactional // Permet d'annuler l'opération si un bug survient au milieu (Rollback)
    public void enregistrerVente(Long medicamentId, Integer quantiteVendue) throws Exception {
        // 1. Récupérer le médicament
        Medicament medicament = medicamentRepository.findById(medicamentId)
                .orElseThrow(() -> new Exception("Médicament introuvable"));

        // 2. Vérifier si le stock est suffisant
        if (medicament.getQuantiteStock() < quantiteVendue) {
            throw new Exception("Stock insuffisant pour ce médicament !");
        }

        // 3. Mettre à jour le stock du médicament
        medicament.setQuantiteStock(medicament.getQuantiteStock() - quantiteVendue);
        medicamentRepository.save(medicament);

        // 4. Créer et sauvegarder la vente
        Vente vente = new Vente(medicament, quantiteVendue);
        venteRepository.save(vente);
    }
}
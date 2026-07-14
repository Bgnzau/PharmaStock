package com.pharmastock.pharmastock.services;

import com.pharmastock.pharmastock.models.Medicament;
import com.pharmastock.pharmastock.repositories.MedicamentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final ApiExterneService apiExterneService;

    // Le constructeur magique avec les deux injections
    public MedicamentService(MedicamentRepository medicamentRepository, ApiExterneService apiExterneService) {
        this.medicamentRepository = medicamentRepository;
        this.apiExterneService = apiExterneService;
    }

    // 1. Récupérer tous les médicaments (Ce qui manquait au contrôleur !)
    public List<Medicament> getAllMedicaments() {
        return medicamentRepository.findAll();
    }

    // 2. Trouver un médicament par son ID (Ce qui manquait aussi !)
    public Medicament getMedicamentById(Long id) {
        return medicamentRepository.findById(id).orElse(null);
    }

    // 3. Supprimer un médicament (Ce qui manquait aussi !)
    public void deleteMedicament(Long id) {
        medicamentRepository.deleteById(id);
    }

    // 4. Ta méthode de sauvegarde boostée à l'API externe !
    public Medicament saveMedicament(Medicament medicament) {
        // On consomme l'API externe juste avant la sauvegarde
        String noticeAuto = apiExterneService.recupererDescriptionAutomatique(medicament.getNom());

        // On l'affiche dans le terminal
        System.out.println("==================================================");
        System.out.println("[API EXTERNE] Données JSON consommées avec succès !");
        System.out.println(noticeAuto);
        System.out.println("==================================================");

        // Sauvegarde finale en BDD
        return medicamentRepository.save(medicament);
    }
}
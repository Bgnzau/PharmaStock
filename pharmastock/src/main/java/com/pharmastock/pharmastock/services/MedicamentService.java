package com.pharmastock.pharmastock.services;

import com.pharmastock.pharmastock.models.Medicament;
import com.pharmastock.pharmastock.repositories.MedicamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Ajout de l'import pour les transactions
import java.util.List;

@Service
public class MedicamentService {

    private final MedicamentRepository repository;

    public MedicamentService(MedicamentRepository repository) {
        this.repository = repository;
    }

    public List<Medicament> getAllMedicaments() {
        return repository.findAll();
    }

    @Transactional // Garantit qu'en cas de problème MySQL pendant la sauvegarde, l'opération
                   // s'annule proprement
    public void saveMedicament(Medicament medicament) {
        repository.save(medicament);
    }

    public Medicament getMedicamentById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional // Sécurise la suppression pour éviter les corruptions de données orphelines
    public void deleteMedicament(Long id) {
        repository.deleteById(id);
    }
}
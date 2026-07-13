package com.pharmastock.pharmastock.repositories;

import com.pharmastock.pharmastock.models.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    // Cette interface gérera toutes les requêtes pour la table medicaments
    
    List<Medicament> findByQuantiteStockLessThan(int quantiteStock);
}

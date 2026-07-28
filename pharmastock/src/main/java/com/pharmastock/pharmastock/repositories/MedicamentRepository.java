package com.pharmastock.pharmastock.repositories;

import com.pharmastock.pharmastock.models.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

    // 1. Recherche un médicament par son nom (ex: "Para" pour Paracétamol)
    List<Medicament> findByNomContainingIgnoreCase(String nom);

    // 2. Filtre les médicaments par le nom de leur catégorie
    List<Medicament> findByCategorieNomContainingIgnoreCase(String nomCategorie);

    // 3. Récupère les stocks critiques (ex: inférieur à 5) pour les alertes
    List<Medicament> findByQuantiteStockLessThan(int quantite);

    // 4. NOUVELLE MÉTHODE : Récupère les médicaments périmés ou qui expirent
    // aujourd'hui
    // Utilisée par le service d'alerte automatique (@Scheduled)
    List<Medicament> findByDatePeremptionLessThanEqual(LocalDate date);
}
package com.pharmastock.pharmastock.repositories;

import com.pharmastock.pharmastock.models.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {
    // Spring Data JPA va générer automatiquement toutes les requêtes SQL (save,
    // findAll, etc.)
}
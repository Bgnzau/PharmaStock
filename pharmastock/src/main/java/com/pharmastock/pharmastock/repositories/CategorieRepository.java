package com.pharmastock.pharmastock.repositories;

import com.pharmastock.pharmastock.models.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // Dit à Spring qu'il s'agit d'un composant de gestion de données
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    // La magie continue ! Spring Data JPA va créer la requête SQL de recherche
    // automatiquement rien qu'avec le nom de cette méthode.
    List<Categorie> findByNomContainingIgnoreCase(String nom);
}
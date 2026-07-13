package com.pharmastock.pharmastock.repositories;

import com.pharmastock.pharmastock.models.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Dit à Spring qu'il s'agit d'un composant de gestion de données
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    // Rien d'autre à écrire ! JpaRepository nous donne déjà accès à :
    // findAll(), findById(), save(), deleteById()... c'est magique !
}
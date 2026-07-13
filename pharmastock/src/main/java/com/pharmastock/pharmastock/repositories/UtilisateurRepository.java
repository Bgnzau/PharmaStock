package com.pharmastock.pharmastock.repositories;

import com.pharmastock.pharmastock.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Cette méthode permettra à Spring Security de chercher un user par son
    // identifiant
    Optional<Utilisateur> findByUsername(String username);
}
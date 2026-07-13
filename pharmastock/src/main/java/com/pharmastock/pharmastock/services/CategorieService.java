package com.pharmastock.pharmastock.services;

import com.pharmastock.pharmastock.models.Categorie;
import com.pharmastock.pharmastock.repositories.CategorieRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Dit à Spring que c'est ici qu'on gère la logique métier
public class CategorieService {

    private final CategorieRepository repository;

    // Injection du repository par constructeur (recommandé)
    public CategorieService(CategorieRepository repository) {
        this.repository = repository;
    }

    // Récupérer toutes les catégories
    public List<Categorie> getAllCategories() {
        return repository.findAll();
    }

    // Sauvegarder une catégorie (Ajout ou Modification)
    public void saveCategorie(Categorie categorie) {
        repository.save(categorie);
    }

    // Récupérer une catégorie par son ID
    public Categorie getCategorieById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Supprimer une catégorie
    public void deleteCategorie(Long id) {
        repository.deleteById(id);
    }
}
package com.pharmastock.pharmastock.services;

import com.pharmastock.pharmastock.models.Categorie;
import com.pharmastock.pharmastock.repositories.CategorieRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository repository;

    public CategorieService(CategorieRepository repository) {
        this.repository = repository;
    }

    public List<Categorie> getAllCategories() {
        return repository.findAll();
    }

    public void saveCategorie(Categorie categorie) {
        repository.save(categorie);
    }

    public void deleteCategorie(Long id) {
        repository.deleteById(id);
    }

    public Categorie getCategorieById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // 🔍 LA MÉTHODE MANQUANTE À AJOUTER :
    public List<Categorie> searchCategoriesParNom(String keyword) {
        return repository.findByNomContainingIgnoreCase(keyword);
    }
}
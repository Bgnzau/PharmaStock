package com.pharmastock.pharmastock.controllers;

import com.pharmastock.pharmastock.models.Categorie;
import com.pharmastock.pharmastock.services.CategorieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories") // Toutes les routes de ce contrôleur commenceront par /categories
public class CategorieController {

    private final CategorieService service;

    public CategorieController(CategorieService service) {
        this.service = service;
    }

    // Afficher la liste des catégories et le formulaire d'ajout (2 en 1 !)
    @GetMapping
    public String listeCategories(Model model) {
        model.addAttribute("categories", service.getAllCategories());
        model.addAttribute("nouvelleCategorie", new Categorie()); // Pour l'objet du formulaire
        return "categories/liste"; // Va chercher templates/categories/liste.html
    }

    // Traiter le formulaire d'ajout d'une catégorie
    @PostMapping("/ajouter")
    public String ajouterCategorie(@ModelAttribute("nouvelleCategorie") Categorie categorie) {
        service.saveCategorie(categorie);
        return "redirect:/categories"; // Redirige vers la liste après l'ajout
    }

    // Supprimer une catégorie
    @GetMapping("/supprimer/{id}")
    public String supprimerCategorie(@PathVariable Long id) {
        service.deleteCategorie(id);
        return "redirect:/categories";
    }

    // Charger la catégorie dans le formulaire pour modification
    @GetMapping("/modifier/{id}")
    public String modifierCategorieForm(@PathVariable Long id, Model model) {
        Categorie catAModifier = service.getCategorieById(id);
        
        // Si la catégorie n'existe pas, on retourne à la liste
        if (catAModifier == null) {
            return "redirect:/categories";
        }
        
        model.addAttribute("categories", service.getAllCategories());
        // Au lieu d'un objet vide, on passe l'objet existant au formulaire !
        model.addAttribute("nouvelleCategorie", catAModifier); 
        return "categories/liste";
    }
}

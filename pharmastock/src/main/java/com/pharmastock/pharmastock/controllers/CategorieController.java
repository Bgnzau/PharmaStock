package com.pharmastock.pharmastock.controllers;

import com.pharmastock.pharmastock.models.Categorie;
import com.pharmastock.pharmastock.services.CategorieService;
import org.springframework.dao.DataIntegrityViolationException; // <-- AJOUTÉ
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // <-- AJOUTÉ

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategorieController {

    private final CategorieService service;

    public CategorieController(CategorieService service) {
        this.service = service;
    }

    // Afficher la liste des catégories (avec filtrage par recherche)
    @GetMapping
    public String listeCategories(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        List<Categorie> liste;

        if (keyword != null && !keyword.trim().isEmpty()) {
            liste = service.searchCategoriesParNom(keyword);
        } else {
            liste = service.getAllCategories();
        }

        model.addAttribute("categories", liste);
        model.addAttribute("nouvelleCategorie", new Categorie());
        return "categories/liste";
    }

    // Enregistrer ou modifier une catégorie
    @PostMapping("/ajouter")
    public String ajouterCategorie(@ModelAttribute("nouvelleCategorie") Categorie categorie,
            RedirectAttributes redirectAttributes) {
        service.saveCategorie(categorie);
        redirectAttributes.addFlashAttribute("success", "Catégorie enregistrée avec succès !");
        return "redirect:/categories";
    }

    // Supprimer une catégorie sécurisée
    @GetMapping("/supprimer/{id}")
    public String supprimerCategorie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.deleteCategorie(id);
            redirectAttributes.addFlashAttribute("success", "Catégorie supprimée avec succès !");
        } catch (DataIntegrityViolationException e) {
            // Capturé si MySQL refuse la suppression à cause des médicaments liés
            redirectAttributes.addFlashAttribute("error",
                    "Impossible de supprimer cette catégorie car des médicaments y sont encore liés.");
        } catch (Exception e) {
            // Sécurité globale pour d'autres erreurs inconnues
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de la suppression.");
        }
        return "redirect:/categories";
    }

    // Charger la catégorie sélectionnée pour modification
    @GetMapping("/modifier/{id}")
    public String modifierCategorieForm(@PathVariable Long id, Model model) {
        Categorie catAModifier = service.getCategorieById(id);

        if (catAModifier == null) {
            return "redirect:/categories";
        }

        model.addAttribute("categories", service.getAllCategories());
        model.addAttribute("nouvelleCategorie", catAModifier);
        return "categories/liste";
    }
}
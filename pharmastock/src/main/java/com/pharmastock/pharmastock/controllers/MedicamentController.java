package com.pharmastock.pharmastock.controllers;

import com.pharmastock.pharmastock.models.Medicament;
import com.pharmastock.pharmastock.services.MedicamentService;
import com.pharmastock.pharmastock.services.CategorieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicaments")
public class MedicamentController {

    private final MedicamentService medicamentService;
    private final CategorieService categorieService;

    // On injecte les deux services nécessaires
    public MedicamentController(MedicamentService medicamentService, CategorieService categorieService) {
        this.medicamentService = medicamentService;
        this.categorieService = categorieService;
    }

    // Afficher la liste et le formulaire
    @GetMapping
    public String listeMedicaments(Model model) {
        model.addAttribute("medicaments", medicamentService.getAllMedicaments());
        model.addAttribute("categories", categorieService.getAllCategories()); // Requis pour le <select>
        model.addAttribute("nouveauMedicament", new Medicament());
        return "medicaments/liste";
    }

    // Traiter l'ajout
    @PostMapping("/ajouter")
    public String ajouterMedicament(@ModelAttribute("nouveauMedicament") Medicament medicament) {
        medicamentService.saveMedicament(medicament);
        return "redirect:/medicaments";
    }

    // Supprimer un médicament
    @GetMapping("/supprimer/{id}")
    public String supprimerMedicament(@PathVariable Long id) {
        medicamentService.deleteMedicament(id);
        return "redirect:/medicaments";
    }

    // Charger un médicament dans le formulaire pour modification
    @GetMapping("/modifier/{id}")
    public String modifierMedicamentForm(@PathVariable Long id, Model model) {
        Medicament medAModifier = medicamentService.getMedicamentById(id);
        
        if (medAModifier == null) {
            return "redirect:/medicaments";
        }
        
        model.addAttribute("medicaments", medicamentService.getAllMedicaments());
        model.addAttribute("categories", categorieService.getAllCategories()); // Requis pour remplir le select
        model.addAttribute("nouveauMedicament", medAModifier); // On passe l'objet existant au lieu d'un vide
        return "medicaments/liste";
    }
}
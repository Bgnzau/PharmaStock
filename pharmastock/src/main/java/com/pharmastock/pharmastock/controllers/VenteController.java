package com.pharmastock.pharmastock.controllers;

import com.pharmastock.pharmastock.services.MedicamentService;
import com.pharmastock.pharmastock.services.VenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VenteController {

    private final VenteService venteService;
    private final MedicamentService medicamentService;

    public VenteController(VenteService venteService, MedicamentService medicamentService) {
        this.venteService = venteService;
        this.medicamentService = medicamentService;
    }

    // Afficher la page des ventes
    @GetMapping("/ventes")
    public String listeVentes(Model model) {
        model.addAttribute("ventes", venteService.getAllVentes());
        model.addAttribute("medicaments", medicamentService.getAllMedicaments());
        return "ventes"; // pointera vers src/main/resources/templates/ventes.html
    }

    // Traiter l'enregistrement d'une vente
    @PostMapping("/ventes/enregistrer")
    public String effectuerVente(@RequestParam("medicamentId") Long medicamentId,
            @RequestParam("quantite") Integer quantite,
            RedirectAttributes redirectAttributes) {
        try {
            venteService.enregistrerVente(medicamentId, quantite);
            redirectAttributes.addFlashAttribute("success", "Vente enregistrée avec succès ! Stock mis à jour.");
        } catch (Exception e) {
            // En cas de stock insuffisant ou autre problème, on renvoie l'erreur
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ventes";
    }
}
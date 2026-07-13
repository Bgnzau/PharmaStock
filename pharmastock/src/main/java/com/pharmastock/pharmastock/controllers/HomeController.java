package com.pharmastock.pharmastock.controllers;

import com.pharmastock.pharmastock.models.Medicament;
import com.pharmastock.pharmastock.services.MedicamentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final MedicamentService medicamentService;

    public HomeController(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    @GetMapping("/")
    public String accueil(Model model, Principal principal) {
        List<Medicament> tousLesMedicaments = medicamentService.getAllMedicaments();

        // 1. Filtrer les médicaments périmés
        List<Medicament> perimes = tousLesMedicaments.stream()
                .filter(Medicament::isPerime)
                .collect(Collectors.toList());

        // 2. Filtrer les médicaments presque périmés (- de 3 mois)
        List<Medicament> presquePerimes = tousLesMedicaments.stream()
                .filter(Medicament::isPresquePerime)
                .collect(Collectors.toList());

        // 3. Filtrer les ruptures de stock (quantité <= 5)
        List<Medicament> enRupture = tousLesMedicaments.stream()
                .filter(med -> med.getQuantiteStock() <= 5)
                .collect(Collectors.toList());

        // On envoie les compteurs à la page d'accueil
        model.addAttribute("nbPerimes", perimes.size());
        model.addAttribute("nbPresquePerimes", presquePerimes.size());
        model.addAttribute("nbRupture", enRupture.size());

        // On envoie les listes pour les afficher
        model.addAttribute("listePerimes", perimes);
        model.addAttribute("listePresquePerimes", presquePerimes);

        // On récupère le nom de l'utilisateur connecté
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("username", "Utilisateur");
        }

        return "accueil";
    }

    // LA ROUTE DU LOGIN
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // NOUVELLE ROUTE POUR FOURNIR LES DONNÉES DU GRAPHIQUE
    @org.springframework.web.bind.annotation.ResponseBody
    @GetMapping("/api/stocks-donnees")
    public List<Medicament> getDonneesStocks() {
        // On récupère uniquement les médicaments dont la quantité est critique (<= 5)
        return medicamentService.getAllMedicaments().stream()
                .filter(med -> med.getQuantiteStock() <= 5)
                .collect(Collectors.toList());
    }
}
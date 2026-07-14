package com.pharmastock.pharmastock.controllers;

import com.pharmastock.pharmastock.models.Utilisateur;
import com.pharmastock.pharmastock.repositories.UtilisateurRepository;
import com.pharmastock.pharmastock.services.Code2faService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class Login2faController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private Code2faService code2faService;

    // 1. Affiche la page et envoie le code par mail
    @GetMapping("/verifier-2fa")
    public String afficherPage2fa(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Optional<Utilisateur> optUser = utilisateurRepository.findByUsername(userDetails.getUsername());
        if (optUser.isPresent()) {
            Utilisateur utilisateur = optUser.get();

            // Génération du code à 6 chiffres
            String code = code2faService.genererCode();
            utilisateur.setCode2fa(code);
            utilisateur.setCode2faExpiration(LocalDateTime.now().plusMinutes(5)); // Expire dans 5 min
            utilisateurRepository.save(utilisateur);

            // Envoi de l'e-mail via ton service Mailtrap réorienté
            // Note : Comme tu n'as pas de champ email, on envoie au username ou une adresse
            // de test standard
            String destinationEmail = utilisateur.getUsername().contains("@") ? utilisateur.getUsername()
                    : "test-pharmastock@mailtrap.io";

            try {
                code2faService.envoyerEmailCode(destinationEmail, code);
                model.addAttribute("message", "Un code de sécurité a été envoyé sur votre boîte mail.");
            } catch (Exception e) {
                model.addAttribute("error", "Erreur lors de l'envoi du mail, mais le code généré est : " + code);
            }
        }

        return "verifier-2fa"; // Va chercher le template html
    }

    // 2. Vérifie le code tapé par l'utilisateur
    @PostMapping("/verifier-2fa")
    public String soumettreCode2fa(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("code") String codeTape,
            Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Optional<Utilisateur> optUser = utilisateurRepository.findByUsername(userDetails.getUsername());
        if (optUser.isPresent()) {
            Utilisateur utilisateur = optUser.get();

            // Vérification
            if (utilisateur.getCode2fa() != null && utilisateur.getCode2fa().equals(codeTape)) {
                if (utilisateur.getCode2faExpiration().isAfter(LocalDateTime.now())) {
                    // Code valide et non expiré ! On nettoie et on redirige vers l'accueil
                    utilisateur.setCode2fa(null);
                    utilisateur.setCode2faExpiration(null);
                    utilisateurRepository.save(utilisateur);
                    return "redirect:/";
                } else {
                    model.addAttribute("error", "Le code a expiré. Veuillez rafraîchir la page.");
                }
            } else {
                model.addAttribute("error", "Code incorrect. Veuillez réessayer.");
            }
        }

        return "verifier-2fa";
    }
}
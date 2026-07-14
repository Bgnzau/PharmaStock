package com.pharmastock.pharmastock.controllers;

import com.pharmastock.pharmastock.services.Code2faService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestMailController {

    @Autowired
    private Code2faService code2faService;

    @GetMapping("/test-2fa")
    public String testerEnvoiMail() {
        try {
            // 1. On génère un faux code à 6 chiffres
            String fauxCode = code2faService.genererCode();

            // 2. On tente de l'envoyer sur l'adresse de ton choix (Mailtrap va
            // l'intercepter de toute façon)
            code2faService.envoyerEmailCode("testeur@pharmastock.com", fauxCode);

            return "🔥 Succès ! Le code " + fauxCode + " a été envoyé. Vérifie ton compte Mailtrap !";
        } catch (Exception e) {
            return "❌ Erreur lors de l'envoi : " + e.getMessage();
        }
    }
}
package com.pharmastock.pharmastock.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class Code2faService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Génère un code aléatoire de 6 chiffres sous forme de chaîne (ex: "054321")
     */
    public String genererCode() {
        Random random = new Random();
        int nombre = random.nextInt(1000000);
        return String.format("%06d", nombre);
    }

    /**
     * Envoie le code de sécurité 2FA par email à l'utilisateur
     */
    public void envoyerEmailCode(String emailDestinataire, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDestinataire);
        message.setSubject("PharmaStock - Votre code de sécurité de double facteur");
        message.setText("Bonjour,\n\n"
                + "Pour finaliser votre connexion à PharmaStock, veuillez utiliser le code de vérification suivant : "
                + code + "\n\n"
                + "Ce code est confidentiel et expirera dans 5 minutes.\n\n"
                + "Cordialement,\nL'équipe de sécurité PharmaStock.");

        mailSender.send(message);
    }
}
package com.pharmastock.pharmastock.services;

import com.pharmastock.pharmastock.models.Medicament;
import com.pharmastock.pharmastock.repositories.MedicamentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AlertePeremptionService {

    private final MedicamentRepository medicamentRepository;
    private final JavaMailSender mailSender;

    @Value("${pharmastock.admin.email:admin@pharmastock.com}")
    private String adminEmail;

    public AlertePeremptionService(MedicamentRepository medicamentRepository, JavaMailSender mailSender) {
        this.medicamentRepository = medicamentRepository;
        this.mailSender = mailSender;
    }

    // S'exécute toutes les minutes pour tes tests (fixedDelay = 60000)
    // Pour la production ou la soutenance, tu pourras remettre le cron :
    // @Scheduled(cron = "0 0 8 * * *")
    @Scheduled(fixedDelay = 60000)
    public void verifierMedicamentsPerimes() {
        LocalDate aujourdHui = LocalDate.now();
        List<Medicament> medPerimes = medicamentRepository.findByDatePeremptionLessThanEqual(aujourdHui);

        if (!medPerimes.isEmpty()) {
            envoyerEmailAlerte(medPerimes);
        }
    }

    private void envoyerEmailAlerte(List<Medicament> medicaments) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmail);
        message.setSubject("⚠️ ALERTE : Produits périmés détectés dans PharmaStock !");

        StringBuilder corpsMessage = new StringBuilder();
        corpsMessage.append("Bonjour Administrateur,\n\n");
        corpsMessage.append("Les médicaments suivants ont atteint ou dépassé leur date de péremption :\n\n");

        for (Medicament med : medicaments) {
            corpsMessage.append("- ")
                    .append(med.getNom())
                    .append(" (Date limite : ")
                    .append(med.getDatePeremption())
                    .append(")\n");
        }

        corpsMessage.append("\nMerci de les retirer du stock immédiatement.\nCordialement,\nL'équipe PharmaStock.");

        message.setText(corpsMessage.toString());
        mailSender.send(message);
        System.out.println("[ALERTE] E-mail envoyé avec succès à " + adminEmail + " via Mailtrap.");
    }
}
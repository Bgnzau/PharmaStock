package com.pharmastock.pharmastock.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventes")
public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "prix_total", nullable = false)
    private Double prixTotal;

    @Column(name = "date_vente", nullable = false)
    private LocalDateTime dateVente;

    // Une vente concerne UN médicament spécifique
    @ManyToOne
    @JoinColumn(name = "medicament_id", nullable = false)
    private Medicament medicament;

    // --- CONSTRUCTEURS ---
    public Vente() {
        this.dateVente = LocalDateTime.now(); // Date automatique à la création
    }

    public Vente(Medicament medicament, Integer quantite) {
        this.medicament = medicament;
        this.quantite = quantite;
        this.dateVente = LocalDateTime.now();
        // Calcul automatique du prix total lors de l'instanciation
        if (medicament != null && medicament.getPrixUnitaire() != null) {
            this.prixTotal = medicament.getPrixUnitaire() * quantite;
        }
    }

    // --- GETTERS ET SETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public LocalDateTime getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDateTime dateVente) {
        this.dateVente = dateVente;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }
}
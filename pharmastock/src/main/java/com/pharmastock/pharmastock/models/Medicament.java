package com.pharmastock.pharmastock.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "medicaments")
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(name = "code_barre", unique = true, length = 50)
    private String codeBarre;

    @Column(nullable = false)
    private Double prixUnitaire;

    @Column(nullable = false)
    private Integer quantiteStock;

    @Column(name = "date_peremption")
    private LocalDate datePeremption;

    // LA RELATION : Plusieurs médicaments pour une seule catégorie
    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false) // Crée la clé étrangère dans MySQL
    private Categorie categorie;

    // --- CONSTRUCTEURS ---
    public Medicament() {
    }

    public Medicament(String nom, String codeBarre, Double prixUnitaire, Integer quantiteStock, LocalDate datePeremption, Categorie categorie) {
        this.nom = nom;
        this.codeBarre = codeBarre;
        this.prixUnitaire = prixUnitaire;
        this.quantiteStock = quantiteStock;
        this.datePeremption = datePeremption;
        this.categorie = categorie;
    }

    // --- LOGIQUE MÉTIER ALERTE PÉREMPTION (ZÉRO ERREUR) ---

    // Indique si le produit est déjà périmé
    public boolean isPerime() {
        if (this.datePeremption == null) return false;
        return this.datePeremption.isBefore(LocalDate.now());
    }

    // Indique si le produit va périmer dans les 90 prochains jours (3 mois)
    public boolean isPresquePerime() {
        if (this.datePeremption == null) return false;
        LocalDate aujourdhui = LocalDate.now();
        
        // S'il est déjà périmé, ce n'est plus "presque" périmé
        if (this.isPerime()) return false; 
        
        long joursRestants = ChronoUnit.DAYS.between(aujourdhui, this.datePeremption);
        return joursRestants >= 0 && joursRestants <= 90;
    }

    // --- GETTERS ET SETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Integer quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}
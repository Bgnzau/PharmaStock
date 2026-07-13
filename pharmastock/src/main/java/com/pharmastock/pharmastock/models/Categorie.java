package com.pharmastock.pharmastock.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity 
@Table(name = "categories") 
public class Categorie {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(nullable = false, length = 100) 
    private String nom;

    @Column(columnDefinition = "TEXT") 
    private String description;

    // --- CONSTRUCTEURS ---
    public Categorie() {
    }

    // Constructeur pratique pour créer des catégories plus tard
    public Categorie(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // --- GETTERS ET SETTERS ---
    // Indispensables pour que Spring puisse lire et modifier les données

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
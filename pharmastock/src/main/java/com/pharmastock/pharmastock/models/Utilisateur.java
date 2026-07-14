package com.pharmastock.pharmastock.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role; // Exemple : ROLE_USER, ROLE_ADMIN

    @Column(name = "code_2fa")
    private String code2fa;

    @Column(name = "code_2fa_expiration")
    private LocalDateTime code2faExpiration;

    // --- CONSTRUCTEURS ---
    public Utilisateur() {
    }

    public Utilisateur(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Constructeur complet (optionnel, utile pour le développement)
    public Utilisateur(String username, String password, String role, String code2fa, LocalDateTime code2faExpiration) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.code2fa = code2fa;
        this.code2faExpiration = code2faExpiration;
    }

    // --- GETTERS ET SETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCode2fa() {
        return code2fa;
    }

    public void setCode2fa(String code2fa) {
        this.code2fa = code2fa;
    }

    public LocalDateTime getCode2faExpiration() {
        return code2faExpiration;
    }

    public void setCode2faExpiration(LocalDateTime code2faExpiration) {
        this.code2faExpiration = code2faExpiration;
    }
}
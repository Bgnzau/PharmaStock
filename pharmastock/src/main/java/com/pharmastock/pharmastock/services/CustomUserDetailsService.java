package com.pharmastock.pharmastock.services;

import com.pharmastock.pharmastock.models.Utilisateur;
import com.pharmastock.pharmastock.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. On cherche l'utilisateur en base de données
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Utilisateur non trouvé avec le pseudo : " + username));

        // 2. On transforme notre modèle "Utilisateur" en un objet que Spring Security
        // comprend
        return User.builder()
                .username(utilisateur.getUsername())
                .password(utilisateur.getPassword()) // Le mot de passe devra être haché (sécurisé)
                .roles(utilisateur.getRole().replace("ROLE_", "")) // Spring Security gère les préfixes automatiquement
                .build();
    }
}
package com.pharmastock.pharmastock.controllers.config;

import com.pharmastock.pharmastock.models.Utilisateur;
import com.pharmastock.pharmastock.repositories.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                // 1. Autorisations publiques (ON A RAJOUTÉ LA RECHERCHE PAR
                                                // CODE-BARRES)
                                                .requestMatchers("/login", "/verifier-2fa", "/css/**", "/js/**",
                                                                "/api/stocks-donnees", "/api/medicaments/lookup/**")
                                                .permitAll()

                                                // 2. BLINDAGE TOTAL POUR L'ADMIN (Médicaments)
                                                .requestMatchers("/medicaments/modifier", "/medicaments/modifier/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/medicaments/ajouter", "/medicaments/ajouter/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/medicaments/supprimer", "/medicaments/supprimer/**")
                                                .hasRole("ADMIN")

                                                // 3. BLINDAGE TOTAL POUR L'ADMIN (Catégories)
                                                .requestMatchers("/categories/modifier", "/categories/modifier/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/categories/ajouter", "/categories/ajouter/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/categories/supprimer", "/categories/supprimer/**")
                                                .hasRole("ADMIN")

                                                // 4. Tout le reste est accessible aux utilisateurs connectés
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/verifier-2fa", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())

                                // 5. Gestion de la redirection en cas de triche du vendeur
                                .exceptionHandling(exception -> exception
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        String uri = request.getRequestURI();
                                                        if (uri.contains("categories")) {
                                                                response.sendRedirect("/categories?interdit=true");
                                                        } else {
                                                                response.sendRedirect("/medicaments?interdit=true");
                                                        }
                                                }));

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public CommandLineRunner initDatabase(UtilisateurRepository utilisateurRepository,
                        PasswordEncoder passwordEncoder) {
                return args -> {
                        // 1. Création automatique du compte ADMIN (Le Gérant)
                        if (utilisateurRepository.findByUsername("admin").isEmpty()) {
                                Utilisateur admin = new Utilisateur();
                                admin.setUsername("admin");
                                admin.setPassword(passwordEncoder.encode("admin123"));
                                admin.setRole("ROLE_ADMIN"); // Spring Security attend le préfixe ROLE_

                                utilisateurRepository.save(admin);
                                System.out.println(
                                                ">> [SUCCESS] Compte ADMIN créé automatiquement ! pseudo: admin / mdp: admin123");
                        }

                        // 2. NOUVEAU : Création automatique du compte USER (Le Vendeur)
                        if (utilisateurRepository.findByUsername("vendeur").isEmpty()) {
                                Utilisateur vendeur = new Utilisateur();
                                vendeur.setUsername("vendeur");
                                vendeur.setPassword(passwordEncoder.encode("vendeur123"));
                                vendeur.setRole("ROLE_USER"); // Rôle utilisateur classique aux accès limités

                                utilisateurRepository.save(vendeur);
                                System.out.println(
                                                ">> [SUCCESS] Compte VENDEUR créé automatiquement ! pseudo: vendeur / mdp: vendeur123");
                        }
                };
        }
}
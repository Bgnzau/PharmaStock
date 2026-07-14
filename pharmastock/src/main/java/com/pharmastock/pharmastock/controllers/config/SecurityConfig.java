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
                                                // 1. Autorisations publiques
                                                .requestMatchers("/login", "/verifier-2fa", "/css/**", "/js/**",
                                                                "/api/stocks-donnees")
                                                .permitAll()

                                                // 2. BLINDAGE TOTAL POUR L'ADMIN (Médicaments)
                                                // Bloque la modification (GET et POST)
                                                .requestMatchers("/medicaments/modifier", "/medicaments/modifier/**")
                                                .hasRole("ADMIN")
                                                // Bloque l'ajout/création (POST)
                                                .requestMatchers("/medicaments/ajouter", "/medicaments/ajouter/**")
                                                .hasRole("ADMIN")
                                                // Bloque la suppression
                                                .requestMatchers("/medicaments/supprimer", "/medicaments/supprimer/**")
                                                .hasRole("ADMIN")

                                                // 3. BLINDAGE TOTAL POUR L'ADMIN (Catégories)
                                                // Bloque la modification (GET et POST)
                                                .requestMatchers("/categories/modifier", "/categories/modifier/**")
                                                .hasRole("ADMIN")
                                                // Bloque l'ajout/création (POST)
                                                .requestMatchers("/categories/ajouter", "/categories/ajouter/**")
                                                .hasRole("ADMIN")
                                                // Bloque la suppression
                                                .requestMatchers("/categories/supprimer", "/categories/supprimer/**")
                                                .hasRole("ADMIN")

                                                // 4. Tout le reste est accessible aux utilisateurs connectés (Vendeur
                                                // et Admin)
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
                                                        // Si l'action concerne les catégories, on le laisse sur la page
                                                        // catégories avec l'alerte
                                                        if (uri.contains("categories")) {
                                                                response.sendRedirect("/categories?interdit=true");
                                                        } else {
                                                                // Sinon, direction la page des médicaments avec
                                                                // l'alerte
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
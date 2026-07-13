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
                                                .requestMatchers("/login", "/css/**", "/js/**", "/api/stocks-donnees")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll());

                return http.build();
        }

        // 1. On définit le moteur de hachage des mots de passe (BCrypt)
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // 2. Ce code s'exécute au démarrage et crée l'admin s'il n'existe pas
        @Bean
        public CommandLineRunner initDatabase(UtilisateurRepository utilisateurRepository,
                        PasswordEncoder passwordEncoder) {
                return args -> {
                        if (utilisateurRepository.findByUsername("admin").isEmpty()) {
                                Utilisateur admin = new Utilisateur();
                                admin.setUsername("admin");
                                // On crypte le mot de passe "admin123" avant de le stocker !
                                admin.setPassword(passwordEncoder.encode("admin123"));
                                admin.setRole("ROLE_ADMIN");

                                utilisateurRepository.save(admin);
                                System.out.println(
                                                ">> [SUCCESS] Compte admin créé automatiquement ! pseudo: admin / mdp: admin123");
                        }
                };
        }
}
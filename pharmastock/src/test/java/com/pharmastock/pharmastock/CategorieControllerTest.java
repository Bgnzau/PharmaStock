package com.pharmastock.pharmastock;

import com.pharmastock.pharmastock.services.CategorieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class CategorieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategorieService service;

    // PREMIER TEST : Tout se passe bien (Succès)
    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void testSupprimerCategorieSucces() throws Exception {
        // Simulation du comportement du service
        Mockito.doNothing().when(service).deleteCategorie(1L);

        // Exécution de la requête sur le bon chemin complet avec CSRF
        mockMvc.perform(get("/categories/supprimer/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"))
                .andExpect(flash().attribute("success", "Catégorie supprimée avec succès !"));
    }
}
package site.easy.to.build.crm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Appliquer CORS à tous les endpoints
                .allowedOrigins("http://localhost:8080", "http://localhost:5174") // Autoriser les requêtes depuis ce domaine
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP autorisées
                .allowedHeaders("*") // Autoriser tous les en-têtes
                .allowCredentials(true) // Autoriser les cookies et les en-têtes d'authentification
                .maxAge(3600); // Durée de vie de la configuration CORS en cache (en secondes)
    }
}
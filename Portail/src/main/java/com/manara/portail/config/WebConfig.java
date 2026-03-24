package com.manara.portail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Chemin de base pour les uploads (défini dans application.properties)
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseLocation = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        // Photos de profil des parents
        registry.addResourceHandler("/uploads/parents/**")
                .addResourceLocations(baseLocation + "parents/");

        // Photos de profil des animateurs
        registry.addResourceHandler("/uploads/animateurs/**")
                .addResourceLocations(baseLocation + "animateurs/");

        // Photos des enfants
        registry.addResourceHandler("/uploads/enfants/**")
                .addResourceLocations(baseLocation + "enfants/");

        // Images des activités
        registry.addResourceHandler("/uploads/activites/**")
                .addResourceLocations(baseLocation + "activites/");

        // Documents divers (factures, rapports, etc.)
        registry.addResourceHandler("/uploads/documents/**")
                .addResourceLocations(baseLocation + "documents/");

        // Pour les fichiers temporaires
        registry.addResourceHandler("/uploads/temp/**")
                .addResourceLocations(baseLocation + "temp/");
    }
}
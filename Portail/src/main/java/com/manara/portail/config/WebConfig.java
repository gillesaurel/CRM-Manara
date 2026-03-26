package com.manara.portail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseLocation = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry.addResourceHandler("/uploads/parents/**")
                .addResourceLocations(baseLocation + "parents/");

        registry.addResourceHandler("/uploads/enfants/**")
                .addResourceLocations(baseLocation + "enfants/");

        registry.addResourceHandler("/uploads/activites/**")
                .addResourceLocations(baseLocation + "activites/");

        registry.addResourceHandler("/uploads/temp/**")
                .addResourceLocations(baseLocation + "temp/");
    }
}
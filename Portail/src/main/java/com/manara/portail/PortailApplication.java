package com.manara.portail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PortailApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(PortailApplication.class, args);

        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);

        //Julien
        //Générateur de hash BCrypt au démarrage pour Administrateur et Animateur
        String adminHash = encoder.encode("admin123");
        String animateurHash = encoder.encode("anim123");

        System.out.println("==== HASHES À COPIER ====");
        System.out.println("Admin : " + adminHash);
        System.out.println("Animateur : " + animateurHash);
        System.out.println("=========================");
    }
}
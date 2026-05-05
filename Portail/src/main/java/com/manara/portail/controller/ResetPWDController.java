package com.manara.portail.controller;

import com.manara.portail.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ResetPWDController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        try {
            passwordResetService.demanderReset(payload.get("email"));
        } catch (Exception e) {
            // On retourne toujours succès pour ne pas révéler si l'email existe
        }
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Si un compte existe avec cet email, vous recevrez un lien."
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        try {
            passwordResetService.reinitialiserMotDePasse(
                    payload.get("token"),
                    payload.get("nouveauMotDePasse")
            );
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Mot de passe réinitialisé avec succès."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}

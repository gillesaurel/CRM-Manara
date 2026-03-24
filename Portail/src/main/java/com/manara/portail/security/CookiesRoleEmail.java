package com.manara.portail.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class CookiesRoleEmail implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        System.out.println("onAuthenticationSuccess appellée");
        // Récupérer les rôles
        String roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining("|"));

        // Cookie pour le rôle
        Cookie roleCookie = new Cookie("role", roles);
        roleCookie.setPath("/");
        roleCookie.setHttpOnly(true);
        roleCookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(roleCookie);

        // Cookie pour l'email
        Cookie emailCookie = new Cookie("email", authentication.getName());
        emailCookie.setPath("/");
        emailCookie.setHttpOnly(true);
        emailCookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(emailCookie);

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isAnimateur = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ANIMATEUR"));
        String contextPath = request.getContextPath();
        if (isAdmin) {
            response.sendRedirect(contextPath + "/admin/administrateur");
        } else if (isAnimateur) {
            response.sendRedirect(contextPath + "/animateur");
        }
        else {
            response.sendRedirect(contextPath + "/");
        }
    }
}
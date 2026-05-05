//package com.manara.portail.config;
//
//import com.manara.portail.model.Utilisateur;
//import com.manara.portail.service.UtilisateurService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private UtilisateurService utilisateurService;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return email -> {
//            Utilisateur utilisateur = utilisateurService.findByEmail(email);
//
//            if (utilisateur == null) {
//                throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
//            }
//
//            return User.builder()
//                    .username(utilisateur.getEmail())
//                    .password(utilisateur.getMotdepasse())
//                    .roles(utilisateur.getRole().getNom())
//                    .build();
//        };
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authz -> authz
//                        // Pages publiques
//                        .requestMatchers("/", "/index", "/activites", "/register", "/register/save", "/login",
//                                "/css/**", "/js/**", "/images/**", "/uploads/**", "/apropos",
//                                "/contact", "/mentions-legales", "/confidentialite").permitAll()
//                        // Pages parent
//                        .requestMatchers("/parent/**").authenticated()
//                        // Pages admin
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        // Pages animateur
//                        .requestMatchers("/animateur/**").hasRole("ANIMATEUR")
//                        // Toutes les autres pages
//                        .anyRequest().authenticated()
//                )
//
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/login")
//                        .defaultSuccessUrl("/parent/dashboard", true)
//                        .failureUrl("/login?error=true")
//                        .permitAll()
//                )
//
//                .logout(logout -> logout
//                        .logoutUrl("/deconnexion")
//                        .logoutSuccessUrl("/login?logout=true")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                )
//                .csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }
//
//    private String getCookieValue(HttpServletRequest request, String cookieName) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookieName.equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//}
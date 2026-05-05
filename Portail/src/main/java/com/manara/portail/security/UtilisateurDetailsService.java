package com.manara.portail.security;

import com.manara.portail.model.Utilisateur;
import com.manara.portail.service.UtilisateurService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurDetailsService implements UserDetailsService {
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @NonNull
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException{

        Utilisateur user = utilisateurService.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }
        if (user.getRole() == null || user.getRole().getNom() == null) {
            throw new UsernameNotFoundException("Utilisateur sans rôle assigné");
        }

        System.out.println("Login attempt for: " + email);
        System.out.println("User found: " + (user != null));
        System.out.println("User role: " + (user != null ? user.getRole().getNom() : "none"));

        String nomRole = user.getRole().getNom();

        return User.builder()
                .username(user.getEmail())
                .password(user.getMotdepasse())
                .authorities(new SimpleGrantedAuthority("ROLE_" + nomRole))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}

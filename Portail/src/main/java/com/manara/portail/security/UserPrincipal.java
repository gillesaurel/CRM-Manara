package com.manara.portail.security;

import com.manara.portail.model.Personne;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final Personne personne;
    private final String role;

    public UserPrincipal(Personne personne) {
        this.personne = personne;
        // Déterminer le rôle basé sur le type réel de l'objet
        if (personne.getClass().getSimpleName().equals("Parent")) {
            this.role = "PARENT";
        } else if (personne.getClass().getSimpleName().equals("Animation")) {
            this.role = "ANIMATEUR";
        } else {
            this.role = "USER";
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return personne.getMotDePasseHash();
    }

    @Override
    public String getUsername() {
        return personne.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return personne.isActif();
    }

    public Personne getPersonne() {
        return personne;
    }

    public String getRole() {
        return role;
    }

    public Integer getId() {
        return personne.getId();
    }

    public String getNomComplet() {
        return personne.getlNom() + " " + personne.getpPrenom();
    }
}

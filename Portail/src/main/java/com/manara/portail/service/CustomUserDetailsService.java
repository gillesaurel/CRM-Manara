package com.manara.portail.service;

import com.manara.portail.model.Parent;
import com.manara.portail.model.Animation;
import com.manara.portail.repository.ParentRepository;
import com.manara.portail.repository.AnimationRepository;
import com.manara.portail.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ParentRepository parentRepository;
    private final AnimationRepository animationRepository;

    public CustomUserDetailsService(ParentRepository parentRepository,
                                    AnimationRepository animationRepository) {
        this.parentRepository = parentRepository;
        this.animationRepository = animationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Chercher d'abord dans les parents
        Parent parent = parentRepository.findByEmail(email)
                .orElse(null);

        if (parent != null) {
            return new UserPrincipal(parent);
        }

        // Chercher dans les animateurs
        Animation animation = animationRepository.findByEmail(email)
                .orElse(null);

        if (animation != null) {
            return new UserPrincipal(animation);
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé avec email: " + email);
    }
}
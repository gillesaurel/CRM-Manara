package com.manara.portail.service;

import com.manara.portail.dto.ParentDTO;
import com.manara.portail.dto.EnfantDTO;
import com.manara.portail.model.Parent;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.model.Role;
import com.manara.portail.repository.ParentRepository;
import com.manara.portail.repository.EnfantRepository;
import com.manara.portail.repository.UtilisateurRepository;
import com.manara.portail.repository.RoleRepository;
import com.manara.portail.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ParentService {

    private final ParentRepository parentRepository;
    private final EnfantRepository enfantRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ParentService(ParentRepository parentRepository,
                         EnfantRepository enfantRepository,
                         UtilisateurRepository utilisateurRepository,
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder) {
        this.parentRepository = parentRepository;
        this.enfantRepository = enfantRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Parent inscrireParent(ParentDTO parentDTO) {
        if (parentRepository.existsByEmail(parentDTO.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe deja");
        }

        Role parentRole = roleRepository.findByNom("PARENT");
        if (parentRole == null) {
            throw new RuntimeException("Le role PARENT n'existe pas");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(parentDTO.getNom());
        utilisateur.setPrenom(parentDTO.getPrenom());
        utilisateur.setEmail(parentDTO.getEmail());
        utilisateur.setTelephone(parentDTO.getTelephone());
        utilisateur.setMotdepasse(passwordEncoder.encode(parentDTO.getMotDePasse()));
        utilisateur.setRole(parentRole);
        utilisateur = utilisateurRepository.save(utilisateur);

        Parent parent = new Parent(utilisateur);
        return parentRepository.save(parent);
    }

    public Parent getParentParEmail(String email) {
        return parentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouve avec email: " + email));
    }

    public Parent getParentParId(Integer id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouve avec id: " + id));
    }

    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    @Transactional
    public Enfant ajouterEnfant(Integer parentId, EnfantDTO enfantDTO) {
        Parent parent = getParentParId(parentId);

        Enfant enfant = new Enfant();
        enfant.setNom(enfantDTO.getNom());
        enfant.setPrenom(enfantDTO.getPrenom());
        enfant.setDateNaissance(enfantDTO.getDateNaissance());
        enfant.setAllergies(enfantDTO.getAllergies());
        enfant.setNotesMedicales(enfantDTO.getNotesMedicales());
        enfant.setParent(parent);

        return enfantRepository.save(enfant);
    }

    public List<Enfant> getEnfantsDuParent(Integer parentId) {
        return enfantRepository.findByParentId(parentId);
    }

    public Enfant getEnfantParId(Integer id) {
        return enfantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfant non trouve avec id: " + id));
    }

    @Transactional
    public void supprimerEnfant(Integer id) {
        Enfant enfant = getEnfantParId(id);
        if (!enfant.getInscriptions().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un enfant qui a des inscriptions");
        }
        enfantRepository.delete(enfant);
    }


    @Transactional
    public Parent mettreAJourParent(Integer id, ParentDTO parentDTO) {
        Parent parent = getParentParId(id);
        Utilisateur utilisateur = parent.getUtilisateur();

        utilisateur.setNom(parentDTO.getNom());
        utilisateur.setPrenom(parentDTO.getPrenom());
        utilisateur.setTelephone(parentDTO.getTelephone());

        utilisateurRepository.save(utilisateur);

        return parent;
    }

    @Transactional
    public Enfant modifierEnfant(Integer enfantId, EnfantDTO enfantDTO) {
        Enfant enfant = getEnfantParId(enfantId);
        enfant.setNom(enfantDTO.getNom());
        enfant.setPrenom(enfantDTO.getPrenom());
        enfant.setDateNaissance(enfantDTO.getDateNaissance());
        enfant.setAllergies(enfantDTO.getAllergies());
        enfant.setNotesMedicales(enfantDTO.getNotesMedicales());
        return enfantRepository.save(enfant);
    }

    public long getNombreParentsActifs() {
        return parentRepository.countByActif(true);
    }

    public List<Enfant> getAllEnfants() {
        return enfantRepository.findAll();
    }
}
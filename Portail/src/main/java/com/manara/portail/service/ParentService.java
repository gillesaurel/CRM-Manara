package com.manara.portail.service;

import com.manara.portail.dto.ParentDTO;
import com.manara.portail.dto.EnfantDTO;
import com.manara.portail.model.Parent;
import com.manara.portail.model.EnfantParent;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.model.Role;
import com.manara.portail.repository.ParentRepository;
import com.manara.portail.repository.EnfantParentRepository;
import com.manara.portail.repository.UtilisateurRepository;
import com.manara.portail.repository.RoleRepository;
import com.manara.portail.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParentService {

    private final ParentRepository parentRepository;
    private final EnfantParentRepository enfantRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ParentService(ParentRepository parentRepository,
                         EnfantParentRepository enfantRepository,
                         UtilisateurRepository utilisateurRepository,
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder) {
        this.parentRepository = parentRepository;
        this.enfantRepository = enfantRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ========== Gestion des Parents ==========

    @Transactional
    public Parent inscrireParent(ParentDTO parentDTO) {
        if (parentRepository.existsByEmail(parentDTO.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }

        // Récupérer le rôle PARENT
        Role parentRole = roleRepository.findByNom("PARENT");
        if (parentRole == null) {
            throw new RuntimeException("Le rôle PARENT n'existe pas dans la base de données");
        }

        // Créer l'utilisateur associé avec tous les champs
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(parentDTO.getNom());
        utilisateur.setPrenom(parentDTO.getPrenom());
        utilisateur.setEmail(parentDTO.getEmail());
        utilisateur.setTelephone(parentDTO.getTelephone());
        utilisateur.setMotdepasse(passwordEncoder.encode(parentDTO.getMotDePasse()));
        utilisateur.setAdresse(parentDTO.getAdresse());
        utilisateur.setVille(parentDTO.getVille());
        utilisateur.setCodePostal(parentDTO.getCodePostal());
        utilisateur.setTypeLien(parentDTO.getTypeLien());
        utilisateur.setRole(parentRole);
        utilisateur = utilisateurRepository.save(utilisateur);

        // Créer le parent lié à l'utilisateur
        Parent parent = new Parent();
        parent.setUtilisateur(utilisateur);
        parent.setlNom(parentDTO.getNom());
        parent.setpPrenom(parentDTO.getPrenom());
        parent.setEmail(parentDTO.getEmail());
        parent.setTelephone(parentDTO.getTelephone());
        parent.setAdresse(parentDTO.getAdresse());
        parent.setVille(parentDTO.getVille());
        parent.setCodePostale(parentDTO.getCodePostal());
        parent.setTypeLien(parentDTO.getTypeLien());
        parent.setActif(true);

        return parentRepository.save(parent);
    }

    @Transactional
    public Parent mettreAJourParent(Integer id, ParentDTO parentDTO) {
        Parent parent = getParentParId(id);

        parent.setlNom(parentDTO.getNom());
        parent.setpPrenom(parentDTO.getPrenom());
        parent.setTelephone(parentDTO.getTelephone());
        parent.setAdresse(parentDTO.getAdresse());
        parent.setVille(parentDTO.getVille());
        parent.setCodePostale(parentDTO.getCodePostal());
        parent.setTypeLien(parentDTO.getTypeLien());

        // Mettre à jour également l'utilisateur associé
        Utilisateur utilisateur = parent.getUtilisateur();
        if (utilisateur != null) {
            utilisateur.setNom(parentDTO.getNom());
            utilisateur.setPrenom(parentDTO.getPrenom());
            utilisateur.setTelephone(parentDTO.getTelephone());
            utilisateur.setAdresse(parentDTO.getAdresse());
            utilisateur.setVille(parentDTO.getVille());
            utilisateur.setCodePostal(parentDTO.getCodePostal());
            utilisateur.setTypeLien(parentDTO.getTypeLien());
            utilisateurRepository.save(utilisateur);
        }

        return parentRepository.save(parent);
    }

    @Transactional
    public void changerMotDePasse(Integer id, String ancienMotDePasse, String nouveauMotDePasse) {
        Parent parent = getParentParId(id);
        Utilisateur utilisateur = parent.getUtilisateur();

        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé pour ce parent");
        }

        if (!passwordEncoder.matches(ancienMotDePasse, utilisateur.getMotdepasse())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        utilisateur.setMotdepasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void reinitialiserMotDePasse(String email, String nouveauMotDePasse) {
        Parent parent = getParentParEmail(email);
        Utilisateur utilisateur = parent.getUtilisateur();

        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé pour ce parent");
        }

        utilisateur.setMotdepasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
    }

    public Parent getParentParEmail(String email) {
        return parentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé avec email: " + email));
    }

    public Parent getParentParId(Integer id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé avec id: " + id));
    }

    public Parent getParentParUtilisateurId(Long utilisateurId) {
        return parentRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé pour l'utilisateur id: " + utilisateurId));
    }

    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    public List<Parent> getAllParentsActifs() {
        return parentRepository.findAllActive();
    }

    public List<Parent> getParentsParVille(String ville) {
        return parentRepository.findByVille(ville);
    }

    @Transactional
    public void activerCompte(Integer id) {
        Parent parent = getParentParId(id);
        parent.setActif(true);
        parentRepository.save(parent);

        Utilisateur utilisateur = parent.getUtilisateur();
        if (utilisateur != null) {
            // Si l'utilisateur a un champ actif, le mettre à jour
            // utilisateur.setActif(true);
            utilisateurRepository.save(utilisateur);
        }
    }

    @Transactional
    public void desactiverCompte(Integer id) {
        Parent parent = getParentParId(id);
        parent.setActif(false);
        parentRepository.save(parent);

        Utilisateur utilisateur = parent.getUtilisateur();
        if (utilisateur != null) {
            // Si l'utilisateur a un champ actif, le mettre à jour
            // utilisateur.setActif(false);
            utilisateurRepository.save(utilisateur);
        }
    }

    @Transactional
    public void supprimerParent(Integer id) {
        Parent parent = getParentParId(id);

        if (!parent.getEnfants().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un parent qui a des enfants associés");
        }

        // Supprimer d'abord l'utilisateur associé
        Utilisateur utilisateur = parent.getUtilisateur();
        if (utilisateur != null) {
            utilisateurRepository.delete(utilisateur);
        }

        parentRepository.delete(parent);
    }

    public long getNombreTotalParents() {
        return parentRepository.count();
    }

    public long getNombreParentsActifs() {
        return parentRepository.countByActif(true);
    }

    // ========== Gestion des Enfants ==========

    @Transactional
    public EnfantParent ajouterEnfant(Integer parentId, EnfantDTO enfantDTO) {
        Parent parent = getParentParId(parentId);

        EnfantParent enfant = new EnfantParent();
        enfant.setNom(enfantDTO.getNom());
        enfant.setPrenom(enfantDTO.getPrenom());
        enfant.setDateNaissance(enfantDTO.getDateNaissance());
        enfant.setAllergies(enfantDTO.getAllergies());
        enfant.setNotesMedicales(enfantDTO.getNotesMedicales());
        enfant.setParent(parent);

        return enfantRepository.save(enfant);
    }

    @Transactional
    public EnfantParent modifierEnfant(Integer enfantId, EnfantDTO enfantDTO) {
        EnfantParent enfant = getEnfantParId(enfantId);

        enfant.setNom(enfantDTO.getNom());
        enfant.setPrenom(enfantDTO.getPrenom());
        enfant.setDateNaissance(enfantDTO.getDateNaissance());
        enfant.setAllergies(enfantDTO.getAllergies());
        enfant.setNotesMedicales(enfantDTO.getNotesMedicales());

        return enfantRepository.save(enfant);
    }

    public EnfantParent getEnfantParId(Integer id) {
        return enfantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfant non trouvé avec id: " + id));
    }

    public List<EnfantParent> getEnfantsDuParent(Integer parentId) {
        return enfantRepository.findByParentId(parentId);
    }

    public List<EnfantParent> getAllEnfants() {
        return enfantRepository.findAll();
    }

    public List<EnfantParent> getEnfantsParParent(Parent parent) {
        return enfantRepository.findByParent(parent);
    }

    public List<EnfantParent> getEnfantsNesApres(LocalDate date) {
        return enfantRepository.findNesApres(date);
    }

    @Transactional
    public void supprimerEnfant(Integer id) {
        EnfantParent enfant = getEnfantParId(id);

        if (!enfant.getInscriptions().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un enfant qui a des inscriptions");
        }

        enfantRepository.delete(enfant);
    }

    @Transactional
    public void transfererEnfant(Integer enfantId, Integer nouveauParentId) {
        EnfantParent enfant = getEnfantParId(enfantId);
        Parent nouveauParent = getParentParId(nouveauParentId);

        enfant.setParent(nouveauParent);
        enfantRepository.save(enfant);
    }

    public List<EnfantParent> rechercherEnfantsParNom(String nom) {
        return enfantRepository.rechercherParNom(nom);
    }

    public long getNombreEnfantsParParent(Integer parentId) {
        return enfantRepository.countByParentId(parentId);
    }

    // ========== Méthodes de vérification ==========

    public boolean checkEmailExists(String email) {
        return parentRepository.existsByEmail(email);
    }

    public boolean checkParentActif(Integer id) {
        Parent parent = getParentParId(id);
        return parent.isActif();
    }

    public boolean checkEnfantAppartientParent(Integer enfantId, Integer parentId) {
        EnfantParent enfant = getEnfantParId(enfantId);
        return enfant.getParent().getId().equals(parentId);
    }

    // ========== Méthodes de recherche avancée ==========

    public List<Parent> rechercherParents(String searchTerm) {
        return parentRepository.rechercher(searchTerm);
    }

    public List<Parent> getParentsAvecEnfants() {
        return parentRepository.findParentsAvecEnfants();
    }

    public List<Parent> getParentsSansEnfants() {
        return parentRepository.findParentsSansEnfants();
    }

    // ========== Statistiques ==========

    public List<Object[]> getStatistiquesParentsParVille() {
        return parentRepository.getStatistiquesParVille();
    }

    public List<Object[]> getStatistiquesEnfantsParAge() {
        return enfantRepository.getStatistiquesParAge();
    }

    public List<Object[]> getTopParentsAvecPlusEnfants() {
        return parentRepository.findTopParentsWithMostChildren();
    }
}
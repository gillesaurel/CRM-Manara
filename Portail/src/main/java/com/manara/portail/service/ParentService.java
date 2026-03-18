package com.manara.portail.service;

import com.manara.portail.dto.ParentDTO;
import com.manara.portail.dto.EnfantDTO;
import com.manara.portail.model.Parent;
import com.manara.portail.model.Enfant;
import com.manara.portail.repository.ParentRepository;
import com.manara.portail.repository.EnfantRepository;
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
    private final EnfantRepository enfantRepository;
    private final PasswordEncoder passwordEncoder;

    public ParentService(ParentRepository parentRepository,
                         EnfantRepository enfantRepository,
                         PasswordEncoder passwordEncoder) {
        this.parentRepository = parentRepository;
        this.enfantRepository = enfantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ========== Gestion des Parents ==========

    @Transactional
    public Parent inscrireParent(ParentDTO parentDTO) {
        // Vérifier si l'email existe déjà
        if (parentRepository.existsByEmail(parentDTO.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }

        Parent parent = new Parent();
        parent.setlNom(parentDTO.getNom());
        parent.setpPrenom(parentDTO.getPrenom());
        parent.setEmail(parentDTO.getEmail());
        parent.setTelephone(parentDTO.getTelephone());
        parent.setMotDePasseHash(passwordEncoder.encode(parentDTO.getMotDePasse()));
        parent.setAdresse(parentDTO.getAdresse());
        parent.setVille(parentDTO.getVille());
        parent.setCodePostale(parentDTO.getCodePostal());
        parent.setTypeLien(parentDTO.getTypeLien());
        parent.setActif(true);

        return parentRepository.save(parent);
    }

    @Transactional
    public Parent mettreAJourParent(Integer id, ParentDTO parentDTO) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé avec id: " + id));

        parent.setlNom(parentDTO.getNom());
        parent.setpPrenom(parentDTO.getPrenom());
        parent.setTelephone(parentDTO.getTelephone());
        parent.setAdresse(parentDTO.getAdresse());
        parent.setVille(parentDTO.getVille());
        parent.setCodePostale(parentDTO.getCodePostal());
        parent.setTypeLien(parentDTO.getTypeLien());

        return parentRepository.save(parent);
    }

    @Transactional
    public void changerMotDePasse(Integer id, String ancienMotDePasse, String nouveauMotDePasse) {
        Parent parent = getParentParId(id);

        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(ancienMotDePasse, parent.getMotDePasseHash())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        // Changer le mot de passe
        parent.setMotDePasseHash(passwordEncoder.encode(nouveauMotDePasse));
        parentRepository.save(parent);
    }

    @Transactional
    public void reinitialiserMotDePasse(String email, String nouveauMotDePasse) {
        Parent parent = getParentParEmail(email);
        parent.setMotDePasseHash(passwordEncoder.encode(nouveauMotDePasse));
        parentRepository.save(parent);
    }

    public Parent getParentParEmail(String email) {
        return parentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé avec email: " + email));
    }

    public Parent getParentParId(Integer id) {
        return parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé avec id: " + id));
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
    }

    @Transactional
    public void desactiverCompte(Integer id) {
        Parent parent = getParentParId(id);
        parent.setActif(false);
        parentRepository.save(parent);
    }

    @Transactional
    public void supprimerParent(Integer id) {
        Parent parent = getParentParId(id);

        // Vérifier si le parent a des enfants
        if (!parent.getEnfants().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un parent qui a des enfants associés");
        }

        parentRepository.delete(parent);
    }

    public long getNombreTotalParents() {
        return parentRepository.count();
    }

    public long getNombreParentsActifs() {
        return parentRepository.findAllActive().size();
    }

    // ========== Gestion des Enfants ==========

    @Transactional
    public Enfant ajouterEnfant(Integer parentId, EnfantDTO enfantDTO) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent non trouvé avec id: " + parentId));

        Enfant enfant = new Enfant();
        enfant.setNom(enfantDTO.getNom());
        enfant.setPrenom(enfantDTO.getPrenom());
        enfant.setDateNaissance(enfantDTO.getDateNaissance());
        enfant.setAllergies(enfantDTO.getAllergies());
        enfant.setNotesMedicales(enfantDTO.getNotesMedicales());
        enfant.setParent(parent);

        return enfantRepository.save(enfant);
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

    public Enfant getEnfantParId(Integer id) {
        return enfantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfant non trouvé avec id: " + id));
    }

    public List<Enfant> getEnfantsDuParent(Integer parentId) {
        return enfantRepository.findByParentId(parentId);
    }

    public List<Enfant> getAllEnfants() {
        return enfantRepository.findAll();
    }

    public List<Enfant> getEnfantsParParent(Parent parent) {
        return enfantRepository.findByParent(parent);
    }

    public List<Enfant> getEnfantsNesApres(LocalDate date) {
        return enfantRepository.findNesApres(date);
    }

    @Transactional
    public void supprimerEnfant(Integer id) {
        Enfant enfant = getEnfantParId(id);

        // Vérifier si l'enfant a des inscriptions
        if (!enfant.getInscriptions().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un enfant qui a des inscriptions");
        }

        enfantRepository.delete(enfant);
    }

    @Transactional
    public void transfererEnfant(Integer enfantId, Integer nouveauParentId) {
        Enfant enfant = getEnfantParId(enfantId);
        Parent nouveauParent = getParentParId(nouveauParentId);

        enfant.setParent(nouveauParent);
        enfantRepository.save(enfant);
    }

    public List<Enfant> rechercherEnfantsParNom(String nom) {
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
        Enfant enfant = getEnfantParId(enfantId);
        return enfant.getParent().getId().equals(parentId);
    }

    // ========== Méthodes de recherche avancée ==========

    public List<Parent> rechercherParents(String searchTerm) {
        // Cette méthode nécessiterait une requête personnalisée dans ParentRepository
        // Par exemple: findByNomContainingOrPrenomContainingOrEmailContaining
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
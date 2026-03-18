package com.manara.portail.repository;

import com.manara.portail.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Integer> {

    // ========== Recherche par email ==========
    Optional<Parent> findByEmail(String email);
    boolean existsByEmail(String email);

    // ========== Recherche par statut ==========
    @Query("SELECT p FROM Parent p WHERE p.actif = true")
    List<Parent> findAllActive();

    @Query("SELECT p FROM Parent p WHERE p.actif = false")
    List<Parent> findAllInactive();

    // ========== Recherche par localisation ==========
    @Query("SELECT p FROM Parent p WHERE p.ville = :ville")
    List<Parent> findByVille(@Param("ville") String ville);

    @Query("SELECT p FROM Parent p WHERE p.codePostale = :codePostale")
    List<Parent> findByCodePostale(@Param("codePostale") String codePostale);

    @Query("SELECT p FROM Parent p WHERE p.ville = :ville AND p.actif = true")
    List<Parent> findActiveByVille(@Param("ville") String ville);

    // ========== Recherche par nom ==========
    List<Parent> findByLNomContainingIgnoreCase(String nom);
    List<Parent> findByPPrenomContainingIgnoreCase(String prenom);

    @Query("SELECT p FROM Parent p WHERE LOWER(p.lNom) LIKE LOWER(CONCAT('%', :nom, '%')) " +
            "OR LOWER(p.pPrenom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Parent> rechercherParNom(@Param("nom") String nom);

    // ========== Recherche avancée ==========
    @Query("SELECT p FROM Parent p WHERE LOWER(p.lNom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.pPrenom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.telephone) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.ville) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Parent> rechercher(@Param("search") String search);

    // ========== Recherche par type de lien ==========
    List<Parent> findByTypeLien(String typeLien);

    @Query("SELECT p FROM Parent p WHERE p.typeLien = :typeLien AND p.actif = true")
    List<Parent> findActiveByTypeLien(@Param("typeLien") String typeLien);

    // ========== Parents avec/sans enfants ==========
    @Query("SELECT DISTINCT p FROM Parent p WHERE SIZE(p.enfants) > 0")
    List<Parent> findParentsAvecEnfants();

    @Query("SELECT p FROM Parent p WHERE SIZE(p.enfants) = 0")
    List<Parent> findParentsSansEnfants();

    @Query("SELECT p FROM Parent p WHERE SIZE(p.enfants) >= :minEnfants")
    List<Parent> findParentsAvecAuMoinsEnfants(@Param("minEnfants") int minEnfants);

    // ========== Statistiques ==========
    @Query("SELECT p.ville, COUNT(p) as total FROM Parent p GROUP BY p.ville ORDER BY total DESC")
    List<Object[]> getStatistiquesParVille();

    @Query("SELECT p.typeLien, COUNT(p) as total FROM Parent p GROUP BY p.typeLien")
    List<Object[]> getStatistiquesParTypeLien();

    @Query("SELECT FUNCTION('YEAR', p.dateCreation), FUNCTION('MONTH', p.dateCreation), COUNT(p) " +
            "FROM Parent p GROUP BY FUNCTION('YEAR', p.dateCreation), FUNCTION('MONTH', p.dateCreation) " +
            "ORDER BY FUNCTION('YEAR', p.dateCreation) DESC, FUNCTION('MONTH', p.dateCreation) DESC")
    List<Object[]> getStatistiquesInscriptionsParMois();

    // ========== Top parents ==========
    @Query("SELECT p, SIZE(p.enfants) as nbEnfants FROM Parent p ORDER BY nbEnfants DESC")
    List<Object[]> findTopParentsWithMostChildren();

    @Query("SELECT p FROM Parent p ORDER BY SIZE(p.enfants) DESC")
    List<Parent> findAllOrderByNbEnfantsDesc();

    // ========== Recherche par date ==========
    @Query("SELECT p FROM Parent p WHERE p.dateCreation BETWEEN :debut AND :fin")
    List<Parent> findBetweenDates(@Param("debut") LocalDateTime debut,
                                  @Param("fin") LocalDateTime fin);

    @Query("SELECT p FROM Parent p WHERE DATE(p.dateCreation) = :date")
    List<Parent> findByDateCreation(@Param("date") LocalDate date);

    // ========== Comptages ==========
    long countByActif(boolean actif);

    @Query("SELECT COUNT(p) FROM Parent p WHERE p.ville = :ville")
    long countByVille(@Param("ville") String ville);

    @Query("SELECT COUNT(p) FROM Parent p WHERE p.typeLien = :typeLien")
    long countByTypeLien(@Param("typeLien") String typeLien);

    // ========== Recherche par téléphone ==========
    Optional<Parent> findByTelephone(String telephone);
    boolean existsByTelephone(String telephone);

    // ========== Recherche par QR code ==========
    Optional<Parent> findByQrId(Integer qrId);

    @Query("SELECT p FROM Parent p WHERE p.qrId IS NOT NULL")
    List<Parent> findWithQRCode();

    // ========== Requêtes personnalisées complexes ==========
    @Query("SELECT p FROM Parent p WHERE " +
            "(:nom IS NULL OR LOWER(p.lNom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
            "(:prenom IS NULL OR LOWER(p.pPrenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
            "(:ville IS NULL OR LOWER(p.ville) = LOWER(:ville)) AND " +
            "(:actif IS NULL OR p.actif = :actif) AND " +
            "(:typeLien IS NULL OR p.typeLien = :typeLien)")
    List<Parent> rechercherParents(@Param("nom") String nom,
                                   @Param("prenom") String prenom,
                                   @Param("ville") String ville,
                                   @Param("actif") Boolean actif,
                                   @Param("typeLien") String typeLien);

    // ========== Parents récents ==========
    @Query("SELECT p FROM Parent p ORDER BY p.id DESC")
    List<Parent> findRecentParents();

    @Query(value = "SELECT * FROM parent ORDER BY id DESC LIMIT :limit", nativeQuery = true)
    List<Parent> findLastParents(@Param("limit") int limit);

    // ========== Parents avec communications non lues ==========
    @Query("SELECT DISTINCT p FROM Parent p JOIN p.communications c WHERE c.lu = false")
    List<Parent> findParentsWithUnreadMessages();

    // ========== Mise à jour massive ==========
    @Modifying
    @Query("UPDATE Parent p SET p.actif = false WHERE p.actif = true AND p.id NOT IN " +
            "(SELECT DISTINCT e.parent.id FROM Enfant e)")
    void desactiverParentsSansEnfants();
}
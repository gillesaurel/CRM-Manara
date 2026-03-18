package com.manara.portail.repository;

import com.manara.portail.model.Enfant;
import com.manara.portail.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnfantRepository extends JpaRepository<Enfant, Integer> {

    // ========== RECHERCHE PAR PARENT ==========
    List<Enfant> findByParent(Parent parent);

    @Query("SELECT e FROM Enfant e WHERE e.parent.id = :parentId")
    List<Enfant> findByParentId(@Param("parentId") Integer parentId);

    @Query("SELECT COUNT(e) FROM Enfant e WHERE e.parent.id = :parentId")
    long countByParentId(@Param("parentId") Integer parentId);

    @Query("SELECT e.parent.id, COUNT(e) FROM Enfant e GROUP BY e.parent.id")
    List<Object[]> countEnfantsParParent();

    // ========== RECHERCHE PAR DATE DE NAISSANCE ==========
    @Query("SELECT e FROM Enfant e WHERE e.dateNaissance > :date")
    List<Enfant> findNesApres(@Param("date") LocalDate date);

    @Query("SELECT e FROM Enfant e WHERE e.dateNaissance < :date")
    List<Enfant> findNesAvant(@Param("date") LocalDate date);

    @Query("SELECT e FROM Enfant e WHERE e.dateNaissance BETWEEN :debut AND :fin")
    List<Enfant> findNesEntre(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    @Query("SELECT e FROM Enfant e ORDER BY e.dateNaissance DESC")
    List<Enfant> findPlusJeunes();

    @Query("SELECT e FROM Enfant e ORDER BY e.dateNaissance ASC")
    List<Enfant> findPlusAgés();

    // ========== RECHERCHE PAR NOM/PRÉNOM ==========
    List<Enfant> findByNomContainingIgnoreCase(String nom);

    List<Enfant> findByPrenomContainingIgnoreCase(String prenom);

    @Query("SELECT e FROM Enfant e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Enfant> rechercherParNom(@Param("search") String search);

    // ========== RECHERCHE PAR SESSION/INSCRIPTION ==========
    @Query("SELECT e FROM Enfant e JOIN e.inscriptions i WHERE i.session.id = :sessionId")
    List<Enfant> findBySessionId(@Param("sessionId") Integer sessionId);

    @Query("SELECT e FROM Enfant e JOIN e.inscriptions i WHERE i.session.id = :sessionId AND i.statut = 'CONFIRMEE'")
    List<Enfant> findBySessionIdAndConfirmee(@Param("sessionId") Integer sessionId);

    @Query("SELECT e FROM Enfant e WHERE e.id NOT IN " +
            "(SELECT i.enfant.id FROM Inscription i WHERE i.session.id = :sessionId)")
    List<Enfant> findNonInscritsBySession(@Param("sessionId") Integer sessionId);

    // ========== RECHERCHE PAR PRÉSENCE ==========
    @Query("SELECT DISTINCT e FROM Enfant e JOIN e.presences p WHERE p.session.id = :sessionId AND p.present = true")
    List<Enfant> findPresentsBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT e FROM Enfant e JOIN e.inscriptions i WHERE i.session.id = :sessionId " +
            "AND e.id NOT IN (SELECT p.enfant.id FROM Presence p WHERE p.session.id = :sessionId AND p.present = true)")
    List<Enfant> findAbsentsBySession(@Param("sessionId") Integer sessionId);

    // ========== RECHERCHE MÉDICALE ==========
    List<Enfant> findByAllergiesIsNotNullAndAllergiesNot(String empty);

    List<Enfant> findByNotesMedicalesIsNotNullAndNotesMedicalesNot(String empty);

    @Query("SELECT e FROM Enfant e WHERE e.allergies IS NOT NULL AND e.allergies <> ''")
    List<Enfant> findAvecAllergies();

    @Query("SELECT e FROM Enfant e WHERE e.notesMedicales IS NOT NULL AND e.notesMedicales <> ''")
    List<Enfant> findAvecNotesMedicales();

    // ========== STATISTIQUES ==========
    @Query("SELECT EXTRACT(YEAR FROM e.dateNaissance) as annee, COUNT(e) FROM Enfant e GROUP BY annee ORDER BY annee")
    List<Object[]> getStatistiquesParAnneeNaissance();

    @Query("SELECT FLOOR((CURRENT_DATE - e.dateNaissance) / 365) as age, COUNT(e) " +
            "FROM Enfant e GROUP BY age ORDER BY age")
    List<Object[]> getStatistiquesParAge();

    @Query("SELECT MONTH(e.dateNaissance) as mois, COUNT(e) FROM Enfant e GROUP BY mois ORDER BY mois")
    List<Object[]> getStatistiquesParMoisNaissance();

    // ========== RECHERCHE AVANCÉE ==========
    @Query("SELECT e FROM Enfant e WHERE " +
            "(:nom IS NULL OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
            "(:prenom IS NULL OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
            "(:ageMin IS NULL OR (YEAR(CURRENT_DATE) - YEAR(e.dateNaissance)) >= :ageMin) AND " +
            "(:ageMax IS NULL OR (YEAR(CURRENT_DATE) - YEAR(e.dateNaissance)) <= :ageMax) AND " +
            "(:parentId IS NULL OR e.parent.id = :parentId)")
    List<Enfant> rechercherEnfants(@Param("nom") String nom,
                                   @Param("prenom") String prenom,
                                   @Param("ageMin") Integer ageMin,
                                   @Param("ageMax") Integer ageMax,
                                   @Param("parentId") Integer parentId);

    // ========== TRI ==========
    List<Enfant> findAllByOrderByNomAscPrenomAsc();

    List<Enfant> findAllByOrderByDateNaissanceDesc();

    // ========== COMPTAGES ==========
    long countByParent(Parent parent);

    @Query("SELECT COUNT(e) FROM Enfant e WHERE e.parent.actif = true")
    long countEnfantsDeParentsActifs();

    // ========== VÉRIFICATIONS ==========
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Enfant e " +
            "WHERE e.parent.id = :parentId AND e.id = :enfantId")
    boolean verifierAppartenance(@Param("enfantId") Integer enfantId,
                                 @Param("parentId") Integer parentId);

    // ========== POUR LE TABLEAU DE BORD ==========
    @Query("SELECT e FROM Enfant e ORDER BY e.id DESC")
    List<Enfant> findDerniersInscrits();

    @Query("SELECT e FROM Enfant e WHERE SIZE(e.inscriptions) > 0")
    List<Enfant> findEnfantsAvecInscriptions();

    @Query("SELECT e FROM Enfant e WHERE SIZE(e.inscriptions) = 0")
    List<Enfant> findEnfantsSansInscriptions();

    // ========== POUR LES NOTIFICATIONS ==========
    @Query("SELECT e FROM Enfant e WHERE e.id IN " +
            "(SELECT i.enfant.id FROM Inscription i WHERE i.session.date = :date AND i.statut = 'CONFIRMEE')")
    List<Enfant> findEnfantsAvecActiviteLe(@Param("date") LocalDate date);

    // ========== SUPPRESSION ==========
    @Modifying
    @Query("DELETE FROM Enfant e WHERE e.parent.actif = false AND SIZE(e.inscriptions) = 0 AND SIZE(e.presences) = 0")
    void deleteOrphelins();
}
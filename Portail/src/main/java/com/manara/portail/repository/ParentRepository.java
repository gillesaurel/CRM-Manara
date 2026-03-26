package com.manara.portail.repository;

import com.manara.portail.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Integer> {

    Optional<Parent> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Parent> findByUtilisateurId(Long utilisateurId);

    @Query("SELECT p FROM Parent p WHERE p.actif = true")
    List<Parent> findAllActive();

    @Query("SELECT p FROM Parent p WHERE p.ville = :ville")
    List<Parent> findByVille(@Param("ville") String ville);

    @Query("SELECT p FROM Parent p WHERE LOWER(p.lNom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.pPrenom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Parent> rechercher(@Param("search") String search);

    @Query("SELECT DISTINCT p FROM Parent p WHERE SIZE(p.enfants) > 0")
    List<Parent> findParentsAvecEnfants();

    @Query("SELECT p FROM Parent p WHERE SIZE(p.enfants) = 0")
    List<Parent> findParentsSansEnfants();

    @Query("SELECT p.ville, COUNT(p) as total FROM Parent p GROUP BY p.ville ORDER BY total DESC")
    List<Object[]> getStatistiquesParVille();

    @Query("SELECT p, SIZE(p.enfants) as nbEnfants FROM Parent p ORDER BY nbEnfants DESC")
    List<Object[]> findTopParentsWithMostChildren();

    long countByActif(boolean actif);

}
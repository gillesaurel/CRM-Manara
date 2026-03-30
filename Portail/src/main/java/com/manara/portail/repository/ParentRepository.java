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

    // Recherche par email de l'utilisateur associé
    @Query("SELECT p FROM Parent p WHERE p.utilisateur.email = :email")
    Optional<Parent> findByEmail(@Param("email") String email);

    // Vérifier si un email existe déjà
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Parent p WHERE p.utilisateur.email = :email")
    boolean existsByEmail(@Param("email") String email);

    Optional<Parent> findByUtilisateurId(Long utilisateurId);

    @Query("SELECT DISTINCT p FROM Parent p WHERE SIZE(p.enfants) > 0")
    List<Parent> findParentsAvecEnfants();

    @Query("SELECT p FROM Parent p WHERE SIZE(p.enfants) = 0")
    List<Parent> findParentsSansEnfants();

    @Query("SELECT p, SIZE(p.enfants) as nbEnfants FROM Parent p ORDER BY nbEnfants DESC")
    List<Object[]> findTopParentsWithMostChildren();
}
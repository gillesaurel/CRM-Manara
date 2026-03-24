package com.manara.portail.repository;

import com.manara.portail.model.Animation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnimationRepository extends JpaRepository<Animation, Integer> {

    // Trouver par email (pour l'authentification)
    Optional<Animation> findByEmail(String email);

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);

    // Trouver les animateurs actifs
    @Query("SELECT a FROM Animation a WHERE a.actif = true")
    List<Animation> findAllActive();

    // Trouver par spécialité
    List<Animation> findBySpecialite(String specialite);

    // Recherche par nom ou prénom
    @Query("SELECT a FROM Animation a WHERE LOWER(a.lNom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(a.pPrenom) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Animation> rechercher(@Param("search") String search);

    // Trouver les animateurs disponibles pour une date donnée
    @Query("SELECT a FROM Animation a WHERE a.actif = true AND a.id NOT IN " +
            "(SELECT s.animateur.id FROM Session s WHERE s.date = :date AND s.animateur IS NOT NULL)")
    List<Animation> findDisponiblesByDate(@Param("date") LocalDate date);

    // Compter le nombre de sessions par animateur
    @Query("SELECT a.id, a.lNom, a.pPrenom, COUNT(s) as nbSessions " +
            "FROM Animation a LEFT JOIN a.sessions s " +
            "GROUP BY a.id, a.lNom, a.pPrenom")
    List<Object[]> countSessionsByAnimateur();

    // Trouver les animateurs avec leurs spécialités
    @Query("SELECT a FROM Animation a ORDER BY a.specialite, a.lNom")
    List<Animation> findAllOrderBySpecialite();

    // Trouver par ville (si ajouté dans Animation)
    @Query("SELECT a FROM Animation a WHERE a.ville = :ville")
    List<Animation> findByVille(@Param("ville") String ville);

    // Trouver les animateurs qui ont des notes
    @Query("SELECT a FROM Animation a WHERE a.notes IS NOT NULL AND a.notes <> ''")
    List<Animation> findWithNotes();

    // Statistiques par spécialité
    @Query("SELECT a.specialite, COUNT(a) as total FROM Animation a GROUP BY a.specialite")
    List<Object[]> getStatistiquesParSpecialite();

    // Trouver les animateurs les plus sollicités
    @Query("SELECT a, COUNT(s) as nbSessions FROM Animation a JOIN a.sessions s " +
            "WHERE s.date >= CURRENT_DATE " +
            "GROUP BY a ORDER BY nbSessions DESC")
    List<Animation> findMostRequested();
}
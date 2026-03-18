package com.manara.portail.repository;

import com.manara.portail.model.Presence;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresenceRepository extends JpaRepository<Presence, Integer> {

    // ========== RECHERCHE PAR ENFANT ==========
    List<Presence> findByEnfant(Enfant enfant);

    @Query("SELECT p FROM Presence p WHERE p.enfant.id = :enfantId")
    List<Presence> findByEnfantId(@Param("enfantId") Integer enfantId);

    // ========== RECHERCHE PAR SESSION ==========
    List<Presence> findBySession(Session session);

    @Query("SELECT p FROM Presence p WHERE p.session.id = :sessionId")
    List<Presence> findBySessionId(@Param("sessionId") Integer sessionId);

    // ========== RECHERCHE COMBINÉE ==========
    Optional<Presence> findByEnfantAndSession(Enfant enfant, Session session);

    @Query("SELECT p FROM Presence p WHERE p.enfant.id = :enfantId AND p.session.id = :sessionId")
    Optional<Presence> findByEnfantIdAndSessionId(@Param("enfantId") Integer enfantId,
                                                  @Param("sessionId") Integer sessionId);

    // ========== COMPTAGES ==========
    @Query("SELECT COUNT(p) FROM Presence p WHERE p.session.id = :sessionId AND p.present = true")
    long countPresencesBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(p) FROM Presence p WHERE p.session.id = :sessionId AND p.present = false")
    long countAbsencesBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(p) FROM Presence p WHERE p.enfant.id = :enfantId AND p.present = true")
    long countPresencesByEnfant(@Param("enfantId") Integer enfantId);

    // ========== RECHERCHE PAR DATE ==========
    @Query("SELECT p FROM Presence p WHERE DATE(p.dateSaisie) = :date")
    List<Presence> findByDateSaisie(@Param("date") LocalDate date);

    @Query("SELECT p FROM Presence p WHERE p.dateSaisie BETWEEN :debut AND :fin")
    List<Presence> findBetweenDates(@Param("debut") LocalDateTime debut,
                                    @Param("fin") LocalDateTime fin);

    // ========== RECHERCHE PAR PÉRIODE ET ENFANT ==========
    @Query("SELECT p FROM Presence p WHERE p.enfant.id = :enfantId AND p.dateSaisie BETWEEN :debut AND :fin")
    List<Presence> findEnfantPresencesBetweenDates(@Param("enfantId") Integer enfantId,
                                                   @Param("debut") LocalDateTime debut,
                                                   @Param("fin") LocalDateTime fin);

    // ========== STATISTIQUES ==========
    @Query("SELECT p.enfant.id, COUNT(p) as total, SUM(CASE WHEN p.present = true THEN 1 ELSE 0 END) as presents " +
            "FROM Presence p WHERE p.session.activite.id = :activiteId GROUP BY p.enfant.id")
    List<Object[]> getStatistiquesPresencesByActivite(@Param("activiteId") Integer activiteId);

    @Query("SELECT p.session.id, COUNT(p) as total, SUM(CASE WHEN p.present = true THEN 1 ELSE 0 END) as presents " +
            "FROM Presence p GROUP BY p.session.id")
    List<Object[]> getStatistiquesPresencesParSession();

    @Query("SELECT EXTRACT(MONTH FROM p.dateSaisie) as mois, EXTRACT(YEAR FROM p.dateSaisie) as annee, " +
            "COUNT(p) as total, SUM(CASE WHEN p.present = true THEN 1 ELSE 0 END) as presents " +
            "FROM Presence p GROUP BY annee, mois ORDER BY annee DESC, mois DESC")
    List<Object[]> getStatistiquesParMois();

    // ========== VÉRIFICATIONS ==========
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Presence p " +
            "WHERE p.enfant.id = :enfantId AND p.session.id = :sessionId")
    boolean existsByEnfantAndSession(@Param("enfantId") Integer enfantId,
                                     @Param("sessionId") Integer sessionId);

    // ========== ENFANTS PRÉSENTS/ABSENTS ==========
    @Query("SELECT e FROM Enfant e WHERE e.id IN " +
            "(SELECT i.enfant.id FROM Inscription i WHERE i.session.id = :sessionId AND i.statut = 'CONFIRMEE') " +
            "AND e.id IN " +
            "(SELECT p.enfant.id FROM Presence p WHERE p.session.id = :sessionId AND p.present = true)")
    List<Enfant> findEnfantsPresentsBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT e FROM Enfant e WHERE e.id IN " +
            "(SELECT i.enfant.id FROM Inscription i WHERE i.session.id = :sessionId AND i.statut = 'CONFIRMEE') " +
            "AND e.id NOT IN " +
            "(SELECT p.enfant.id FROM Presence p WHERE p.session.id = :sessionId AND p.present = true)")
    List<Enfant> findEnfantsAbsentsBySession(@Param("sessionId") Integer sessionId);

    // ========== MISES À JOUR MASSIVES ==========
    @Modifying
    @Query("UPDATE Presence p SET p.present = :present WHERE p.session.id = :sessionId AND p.enfant.id = :enfantId")
    void updatePresence(@Param("sessionId") Integer sessionId,
                        @Param("enfantId") Integer enfantId,
                        @Param("present") boolean present);

    @Modifying
    @Query("DELETE FROM Presence p WHERE p.session.id = :sessionId")
    void deleteBySessionId(@Param("sessionId") Integer sessionId);

    // ========== POUR LE TABLEAU DE BORD ==========
    @Query("SELECT p FROM Presence p ORDER BY p.dateSaisie DESC")
    List<Presence> findDernieresPresences();

    @Query("SELECT p.session.id, COUNT(p) FROM Presence p WHERE p.present = true GROUP BY p.session.id")
    List<Object[]> countPresentsParSession();

    // ========== RECHERCHE AVANCÉE ==========
    @Query("SELECT p FROM Presence p WHERE " +
            "(:enfantId IS NULL OR p.enfant.id = :enfantId) AND " +
            "(:sessionId IS NULL OR p.session.id = :sessionId) AND " +
            "(:present IS NULL OR p.present = :present) AND " +
            "(:dateDebut IS NULL OR p.dateSaisie >= :dateDebut) AND " +
            "(:dateFin IS NULL OR p.dateSaisie <= :dateFin)")
    List<Presence> rechercherPresences(@Param("enfantId") Integer enfantId,
                                       @Param("sessionId") Integer sessionId,
                                       @Param("present") Boolean present,
                                       @Param("dateDebut") LocalDateTime dateDebut,
                                       @Param("dateFin") LocalDateTime dateFin);
}
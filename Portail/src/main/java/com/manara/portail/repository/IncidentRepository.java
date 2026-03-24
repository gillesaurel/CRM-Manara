package com.manara.portail.repository;

import com.manara.portail.model.Incident;
import com.manara.portail.model.Enfant;
import com.manara.portail.model.Animation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

    // ========== RECHERCHE PAR ENFANT ==========
    List<Incident> findByEnfant(Enfant enfant);

    @Query("SELECT i FROM Incident i WHERE i.enfant.id = :enfantId")
    List<Incident> findByEnfantId(@Param("enfantId") Integer enfantId);

    // ========== RECHERCHE PAR ANIMATEUR ==========
    List<Incident> findByAnimation(Animation animation);

    @Query("SELECT i FROM Incident i WHERE i.animation.id = :animateurId")
    List<Incident> findByAnimationId(@Param("animateurId") Integer animateurId);

    // ========== RECHERCHE PAR TYPE ==========
    List<Incident> findByType(String type);

    @Query("SELECT DISTINCT i.type FROM Incident i")
    List<String> findDistinctTypes();

    // ========== RECHERCHE PAR STATUT ==========
    List<Incident> findByStatut(String statut);

    @Query("SELECT COUNT(i) FROM Incident i WHERE i.statut != 'RESOLU' AND i.statut != 'CLOTURE'")
    long countNonResolus();

    // ========== RECHERCHE PAR GRAVITÉ ==========
    List<Incident> findByGravite(int gravite);

    @Query("SELECT i FROM Incident i WHERE i.gravite >= :seuil")
    List<Incident> findIncidentsGraves(@Param("seuil") int seuil);

    @Query("SELECT COUNT(i) FROM Incident i WHERE i.gravite >= :seuil")
    long countByGraviteGreaterThanEqual(@Param("seuil") int seuil);

    @Query("SELECT COUNT(i) FROM Incident i WHERE i.gravite = :gravite")
    long countByGravite(@Param("gravite") int gravite);

    // ========== RECHERCHE PAR DATE ==========
    @Query("SELECT i FROM Incident i WHERE i.dateHeure BETWEEN :debut AND :fin")
    List<Incident> findBetweenDates(@Param("debut") LocalDateTime debut,
                                    @Param("fin") LocalDateTime fin);

    @Query("SELECT i FROM Incident i WHERE i.enfant.id = :enfantId AND i.dateHeure BETWEEN :debut AND :fin")
    List<Incident> findEnfantIncidentsBetweenDates(@Param("enfantId") Integer enfantId,
                                                   @Param("debut") LocalDateTime debut,
                                                   @Param("fin") LocalDateTime fin);

    // ========== STATISTIQUES ==========
    @Query("SELECT i.type, COUNT(i) as total FROM Incident i GROUP BY i.type ORDER BY total DESC")
    List<Object[]> getStatistiquesParType();

    @Query("SELECT i.gravite, COUNT(i) as total FROM Incident i GROUP BY i.gravite ORDER BY i.gravite")
    List<Object[]> getStatistiquesParGravite();

    @Query("SELECT YEAR(i.dateHeure) as annee, MONTH(i.dateHeure) as mois, COUNT(i) as total " +
            "FROM Incident i GROUP BY YEAR(i.dateHeure), MONTH(i.dateHeure) ORDER BY annee DESC, mois DESC")
    List<Object[]> getStatistiquesParMois();

    @Query("SELECT i.enfant.id, COUNT(i) as total FROM Incident i GROUP BY i.enfant.id")
    List<Object[]> countIncidentsByEnfant();

    // ========== INCIDENTS NON RÉSOLUS ==========
    @Query("SELECT i FROM Incident i WHERE i.statut != 'RESOLU' AND i.statut != 'CLOTURE'")
    List<Incident> findIncidentsNonResolus();

    @Query("SELECT i FROM Incident i WHERE i.statut IN ('SIGNALÉ', 'EN_COURS', 'EN_ATTENTE')")
    List<Incident> findIncidentsOuverts();

    // ========== DERNIERS INCIDENTS ==========
    @Query("SELECT i FROM Incident i ORDER BY i.dateHeure DESC")
    List<Incident> findDerniersIncidents();

    @Query("SELECT i FROM Incident i ORDER BY i.dateHeure DESC")
    List<Incident> findTop10ByOrderByDateHeureDesc();

    // ========== RECHERCHE AVANCÉE ==========
    @Query("SELECT i FROM Incident i WHERE " +
            "(:type IS NULL OR i.type = :type) AND " +
            "(:statut IS NULL OR i.statut = :statut) AND " +
            "(:graviteMin IS NULL OR i.gravite >= :graviteMin) AND " +
            "(:graviteMax IS NULL OR i.gravite <= :graviteMax) AND " +
            "(:dateDebut IS NULL OR i.dateHeure >= :dateDebut) AND " +
            "(:dateFin IS NULL OR i.dateHeure <= :dateFin) AND " +
            "(:enfantId IS NULL OR i.enfant.id = :enfantId) AND " +
            "(:animateurId IS NULL OR i.animation.id = :animateurId)")
    List<Incident> rechercherIncidents(@Param("type") String type,
                                       @Param("statut") String statut,
                                       @Param("graviteMin") Integer graviteMin,
                                       @Param("graviteMax") Integer graviteMax,
                                       @Param("dateDebut") LocalDateTime dateDebut,
                                       @Param("dateFin") LocalDateTime dateFin,
                                       @Param("enfantId") Integer enfantId,
                                       @Param("animateurId") Integer animateurId);

    // ========== MISES À JOUR ==========
    @Modifying
    @Query("UPDATE Incident i SET i.statut = :statut, i.actionsPrises = :actionsPrises WHERE i.id = :id")
    void updateIncident(@Param("id") Integer id,
                        @Param("statut") String statut,
                        @Param("actionsPrises") String actionsPrises);

    @Modifying
    @Query("UPDATE Incident i SET i.statut = 'CLOTURE' WHERE i.id IN " +
            "(SELECT i2.id FROM Incident i2 WHERE i2.dateHeure < :date AND i2.statut = 'RESOLU')")
    void cloturerAnciensIncidents(@Param("date") LocalDateTime date);

    // ========== COMPTAGES ==========
    long countByEnfantId(Integer enfantId);

    long countByAnimationId(Integer animateurId);

    @Query("SELECT COUNT(i) FROM Incident i WHERE i.statut = :statut")
    long countByStatut(@Param("statut") String statut);

    // ========== POUR TABLEAU DE BORD ==========
    @Query("SELECT i.statut, COUNT(i) FROM Incident i GROUP BY i.statut")
    List<Object[]> countByStatutGrouped();

    @Query("SELECT i.gravite, COUNT(i) FROM Incident i GROUP BY i.gravite")
    List<Object[]> countByGraviteGrouped();

    // ========== POUR NOTIFICATIONS ==========
    @Query("SELECT i FROM Incident i WHERE i.statut = 'SIGNALÉ' AND i.dateHeure > :since")
    List<Incident> findNouveauxIncidents(@Param("since") LocalDateTime since);

    // ========== SUPPRESSION ==========
    @Modifying
    @Query("DELETE FROM Incident i WHERE i.dateHeure < :date AND i.statut = 'CLOTURE'")
    void deleteAnciensIncidents(@Param("date") LocalDateTime date);
}
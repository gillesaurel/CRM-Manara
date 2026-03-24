package com.manara.portail.repository;

import com.manara.portail.model.IncidentParent;
import com.manara.portail.model.EnfantParent;
import com.manara.portail.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentParentRepository extends JpaRepository<IncidentParent, Integer> {

    List<IncidentParent> findByEnfant(EnfantParent enfant);

    @Query("SELECT i FROM IncidentParent i WHERE i.enfant.id = :enfantId")
    List<IncidentParent> findByEnfantId(@Param("enfantId") Integer enfantId);

    List<IncidentParent> findByAnimateur(Utilisateur animateur);

    @Query("SELECT i FROM IncidentParent i WHERE i.animateur.id = :animateurId")
    List<IncidentParent> findByAnimateurId(@Param("animateurId") Integer animateurId);

    List<IncidentParent> findByType(String type);

    List<IncidentParent> findByStatut(String statut);

    List<IncidentParent> findByGravite(int gravite);

    @Query("SELECT i FROM IncidentParent i WHERE i.gravite >= :seuil")
    List<IncidentParent> findIncidentsGraves(@Param("seuil") int seuil);

    @Query("SELECT COUNT(i) FROM IncidentParent i WHERE i.gravite >= :seuil")
    long countByGraviteGreaterThanEqual(@Param("seuil") int seuil);

    @Query("SELECT i FROM IncidentParent i WHERE i.dateHeure BETWEEN :debut AND :fin")
    List<IncidentParent> findBetweenDates(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT i FROM IncidentParent i WHERE i.statut != 'RESOLU' AND i.statut != 'CLOTURE'")
    List<IncidentParent> findIncidentsNonResolus();

    @Query("SELECT COUNT(i) FROM IncidentParent i WHERE i.statut != 'RESOLU' AND i.statut != 'CLOTURE'")
    long countNonResolus();

    @Query("SELECT i FROM IncidentParent i ORDER BY i.dateHeure DESC")
    List<IncidentParent> findTop10ByOrderByDateHeureDesc();

    @Modifying
    @Query("UPDATE IncidentParent i SET i.statut = :statut, i.actionsPrises = :actionsPrises WHERE i.id = :id")
    void updateIncident(@Param("id") Integer id, @Param("statut") String statut, @Param("actionsPrises") String actionsPrises);
}
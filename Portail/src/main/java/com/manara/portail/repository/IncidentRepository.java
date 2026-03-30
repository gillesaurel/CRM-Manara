package com.manara.portail.repository;

import com.manara.portail.model.Incident;
import com.manara.portail.model.Enfant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

    List<Incident> findByEnfant(Enfant enfant);

    @Query("SELECT i FROM Incident i WHERE i.enfant.id = :enfantId")
    List<Incident> findByEnfantId(@Param("enfantId") Integer enfantId);

    List<Incident> findByAnimateurId(Integer animateurId);

    List<Incident> findByType(String type);

    List<Incident> findByStatut(String statut);

    List<Incident> findByGravite(int gravite);

    @Query("SELECT i FROM Incident i WHERE i.gravite >= :seuil")
    List<Incident> findIncidentsGraves(@Param("seuil") int seuil);

    @Query("SELECT i FROM Incident i WHERE i.dateHeure BETWEEN :debut AND :fin")
    List<Incident> findBetweenDates(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT i FROM Incident i WHERE i.statut != 'RESOLU' AND i.statut != 'CLOTURE'")
    List<Incident> findIncidentsNonResolus();

    @Query("SELECT i FROM Incident i ORDER BY i.dateHeure DESC")
    List<Incident> findTop10ByOrderByDateHeureDesc();

    @Modifying
    @Query("UPDATE Incident i SET i.statut = :statut, i.actionsPrises = :actionsPrises WHERE i.id = :id")
    void updateIncident(@Param("id") Integer id, @Param("statut") String statut, @Param("actionsPrises") String actionsPrises);
}
package com.manara.portail.repository;

import com.manara.portail.model.SessionParent;
import com.manara.portail.model.ActiviteParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionParentRepository extends JpaRepository<SessionParent, Integer> {

    List<SessionParent> findByActivite(ActiviteParent activite);

    List<SessionParent> findByDate(LocalDate date);

    @Query("SELECT s FROM SessionParent s WHERE s.date BETWEEN :debut AND :fin")
    List<SessionParent> findBetweenDates(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    @Query("SELECT s FROM SessionParent s WHERE s.activite.id = :activiteId AND s.date >= :date")
    List<SessionParent> findFuturesByActivite(@Param("activiteId") Integer activiteId, @Param("date") LocalDate date);

    @Query("SELECT s FROM SessionParent s WHERE s.statut = :statut")
    List<SessionParent> findByStatut(@Param("statut") String statut);

    @Query("SELECT s FROM SessionParent s WHERE s.date >= :date ORDER BY s.date ASC")
    List<SessionParent> findFuturesSessions(@Param("date") LocalDate date);

    @Query("SELECT COUNT(s) FROM SessionParent s WHERE s.date >= :date")
    long countFuturesSessions(@Param("date") LocalDate date);

    @Query("SELECT s FROM SessionParent s WHERE s.animateur.id = :animateurId")
    List<SessionParent> findByAnimateurId(@Param("animateurId") Integer animateurId);

    @Query("SELECT s FROM SessionParent s WHERE s.animateur.id = :animateurId AND s.date = :date")
    List<SessionParent> findByAnimateurIdAndDate(@Param("animateurId") Integer animateurId, @Param("date") LocalDate date);
    @Query("SELECT s FROM SessionParent s LEFT JOIN FETCH s.inscriptions i LEFT JOIN FETCH i.enfant WHERE s.animateur.id = :animateurId")
    List<SessionParent> findSessionsAvecEnfantsByAnimateur(@Param("animateurId") Integer animateurId);
}
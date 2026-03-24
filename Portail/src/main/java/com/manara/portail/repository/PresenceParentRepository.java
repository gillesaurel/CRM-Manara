package com.manara.portail.repository;

import com.manara.portail.model.PresenceParent;
import com.manara.portail.model.EnfantParent;
import com.manara.portail.model.SessionParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresenceParentRepository extends JpaRepository<PresenceParent, Integer> {

    List<PresenceParent> findByEnfant(EnfantParent enfant);

    @Query("SELECT p FROM PresenceParent p WHERE p.enfant.id = :enfantId")
    List<PresenceParent> findByEnfantId(@Param("enfantId") Integer enfantId);

    List<PresenceParent> findBySession(SessionParent session);

    @Query("SELECT p FROM PresenceParent p WHERE p.session.id = :sessionId")
    List<PresenceParent> findBySessionId(@Param("sessionId") Integer sessionId);

    Optional<PresenceParent> findByEnfantAndSession(EnfantParent enfant, SessionParent session);

    @Query("SELECT COUNT(p) FROM PresenceParent p WHERE p.session.id = :sessionId AND p.present = true")
    long countPresencesBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(p) FROM PresenceParent p WHERE p.session.id = :sessionId AND p.present = false")
    long countAbsencesBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT p FROM PresenceParent p WHERE DATE(p.dateSaisie) = :date")
    List<PresenceParent> findByDateSaisie(@Param("date") LocalDate date);

    @Query("SELECT p FROM PresenceParent p WHERE p.enfant.id = :enfantId AND p.dateSaisie BETWEEN :debut AND :fin")
    List<PresenceParent> findEnfantPresencesBetweenDates(@Param("enfantId") Integer enfantId,
                                                         @Param("debut") LocalDateTime debut,
                                                         @Param("fin") LocalDateTime fin);

    @Query("SELECT e FROM EnfantParent e WHERE e.id IN " +
            "(SELECT i.enfant.id FROM InscriptionParent i WHERE i.session.id = :sessionId AND i.statut = 'CONFIRMEE') " +
            "AND e.id NOT IN " +
            "(SELECT p.enfant.id FROM PresenceParent p WHERE p.session.id = :sessionId AND p.present = true)")
    List<EnfantParent> findEnfantsAbsentsBySession(@Param("sessionId") Integer sessionId);
}
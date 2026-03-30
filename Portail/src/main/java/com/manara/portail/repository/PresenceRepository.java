package com.manara.portail.repository;

import com.manara.portail.model.Presence;
import com.manara.portail.model.Enfant;
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
public interface PresenceRepository extends JpaRepository<Presence, Integer> {

    List<Presence> findByEnfant(Enfant enfant);

    @Query("SELECT p FROM Presence p WHERE p.enfant.id = :enfantId")
    List<Presence> findByEnfantId(@Param("enfantId") Integer enfantId);

    List<Presence> findBySession(SessionParent session);

    @Query("SELECT p FROM Presence p WHERE p.session.id = :sessionId")
    List<Presence> findBySessionId(@Param("sessionId") Integer sessionId);

    Optional<Presence> findByEnfantAndSession(Enfant enfant, SessionParent session);

    @Query("SELECT COUNT(p) FROM Presence p WHERE p.session.id = :sessionId AND p.present = true")
    long countPresencesBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(p) FROM Presence p WHERE p.session.id = :sessionId AND p.present = false")
    long countAbsencesBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT p FROM Presence p WHERE DATE(p.dateSaisie) = :date")
    List<Presence> findByDateSaisie(@Param("date") LocalDate date);

    @Query("SELECT e FROM Enfant e WHERE e.id IN " +
            "(SELECT i.enfant.id FROM InscriptionEnfant i WHERE i.session.id = :sessionId AND i.enAttente = false) " +
            "AND e.id NOT IN " +
            "(SELECT p.enfant.id FROM Presence p WHERE p.session.id = :sessionId AND p.present = true)")
    List<Enfant> findEnfantsAbsentsBySession(@Param("sessionId") Integer sessionId);
}
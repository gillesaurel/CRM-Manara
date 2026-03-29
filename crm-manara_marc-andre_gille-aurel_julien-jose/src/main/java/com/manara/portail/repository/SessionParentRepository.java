package com.manara.portail.repository;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.SessionParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessionParentRepository extends JpaRepository<ActiviteAdmin, Long> {
    @Query("SELECT DISTINCT s FROM SessionParent s " +
            "JOIN FETCH s.activite a " +
            "JOIN FETCH s.inscriptions i " +
            "JOIN FETCH i.enfant e " +
            "WHERE s.animateur.id = :animateurId")
    List<SessionParent> findSessionsAvecEnfantsByAnimateur(@Param("animateurId") Integer animateurId);
}

package com.manara.portail.repository;

import com.manara.portail.model.InscriptionEnfant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionEnfantRepository extends JpaRepository<InscriptionEnfant, Long> {

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.session.id = :sessionId")
    List<InscriptionEnfant> findBySessionId(@Param("sessionId") Integer sessionId);
}
package com.manara.portail.repository;

import com.manara.portail.model.InscriptionEnfant;
<<<<<<< HEAD
import com.manara.portail.model.Enfant;
import com.manara.portail.model.SessionParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
>>>>>>> temporary-brench-manu

@Repository
public interface InscriptionEnfantRepository extends JpaRepository<InscriptionEnfant, Long> {

<<<<<<< HEAD
    List<InscriptionEnfant> findByEnfant(Enfant enfant);

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.enfant.id = :enfantId")
    List<InscriptionEnfant> findByEnfantId(@Param("enfantId") Integer enfantId);

    List<InscriptionEnfant> findBySession(SessionParent session);

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.session.id = :sessionId")
    List<InscriptionEnfant> findBySessionId(@Param("sessionId") Integer sessionId);

    Optional<InscriptionEnfant> findByEnfantAndSession(Enfant enfant, SessionParent session);

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.enfant.id = :enfantId AND i.session.id = :sessionId")
    Optional<InscriptionEnfant> findByEnfantIdAndSessionId(@Param("enfantId") Integer enfantId,
                                                           @Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(i) FROM InscriptionEnfant i WHERE i.session.id = :sessionId AND i.enAttente = false")
    long countConfirmationsBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(i) FROM InscriptionEnfant i WHERE i.session.id = :sessionId AND i.enAttente = true")
    long countAttenteBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(i) FROM InscriptionEnfant i WHERE i.session.id = :sessionId")
    long countTotalBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(i) FROM InscriptionEnfant i WHERE i.enfant.id = :enfantId")
    long countByEnfantId(@Param("enfantId") Integer enfantId);

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.session.date >= :date AND i.enfant.parent.id = :parentId")
    List<InscriptionEnfant> findFuturesByParent(@Param("parentId") Integer parentId, @Param("date") LocalDate date);

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.session.date < :date AND i.enfant.parent.id = :parentId")
    List<InscriptionEnfant> findPassesByParent(@Param("parentId") Integer parentId, @Param("date") LocalDate date);

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.enfant.parent.id = :parentId ORDER BY i.id DESC")
    List<InscriptionEnfant> findAllByParent(@Param("parentId") Integer parentId);

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.enAttente = true ORDER BY i.id")
    List<InscriptionEnfant> findListeAttente();

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.session.id = :sessionId AND i.enAttente = true ORDER BY i.id")
    List<InscriptionEnfant> findListeAttenteBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT i FROM InscriptionEnfant i WHERE i.enfant.parent.id = :parentId AND i.enAttente = true")
    List<InscriptionEnfant> findAttenteByParent(@Param("parentId") Integer parentId);

    @Query("SELECT i FROM InscriptionEnfant i ORDER BY i.id DESC")
    List<InscriptionEnfant> findTop10ByOrderByIdDesc();

    @Query("SELECT i.session.activite.nom, COUNT(i) FROM InscriptionEnfant i GROUP BY i.session.activite.nom")
    List<Object[]> countInscriptionsParActivite();

    @Modifying
    @Query("UPDATE InscriptionEnfant i SET i.enAttente = false WHERE i.id = :id")
    void confirmerInscription(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM InscriptionEnfant i WHERE i.session.date < :date")
    void deleteAnciennesInscriptions(@Param("date") LocalDate date);
=======
    @Query("SELECT i FROM InscriptionEnfant i WHERE i.session.id = :sessionId")
    List<InscriptionEnfant> findBySessionId(@Param("sessionId") Integer sessionId);
>>>>>>> temporary-brench-manu
}
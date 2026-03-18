package com.manara.portail.repository;

import com.manara.portail.model.Inscription;
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
public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {

    // ========== RECHERCHE PAR ENFANT ==========
    List<Inscription> findByEnfant(Enfant enfant);

    @Query("SELECT i FROM Inscription i WHERE i.enfant.id = :enfantId")
    List<Inscription> findByEnfantId(@Param("enfantId") Integer enfantId);

    @Query("SELECT i FROM Inscription i WHERE i.enfant.id = :enfantId AND i.statut = 'CONFIRMEE'")
    List<Inscription> findConfirmationsByEnfant(@Param("enfantId") Integer enfantId);

    // ========== RECHERCHE PAR SESSION ==========
    List<Inscription> findBySession(Session session);

    @Query("SELECT i FROM Inscription i WHERE i.session.id = :sessionId")
    List<Inscription> findBySessionId(@Param("sessionId") Integer sessionId);

    @Query("SELECT i FROM Inscription i WHERE i.session.id = :sessionId AND i.statut = 'CONFIRMEE'")
    List<Inscription> findConfirmationsBySessionId(@Param("sessionId") Integer sessionId);

    // ========== RECHERCHE COMBINÉE ==========
    Optional<Inscription> findByEnfantAndSession(Enfant enfant, Session session);

    @Query("SELECT i FROM Inscription i WHERE i.enfant.id = :enfantId AND i.session.id = :sessionId")
    Optional<Inscription> findByEnfantIdAndSessionId(@Param("enfantId") Integer enfantId,
                                                     @Param("sessionId") Integer sessionId);

    // ========== COMPTAGES PAR SESSION ==========
    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.session.id = :sessionId AND i.statut = 'CONFIRMEE'")
    long countConfirmationsBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.session.id = :sessionId AND i.statut = 'EN_ATTENTE'")
    long countAttenteBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.session.id = :sessionId")
    long countTotalBySession(@Param("sessionId") Integer sessionId);

    // ========== COMPTAGES PAR ENFANT ==========
    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.enfant.id = :enfantId")
    long countByEnfantId(@Param("enfantId") Integer enfantId);

    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.enfant.id = :enfantId AND i.statut = 'CONFIRMEE'")
    long countConfirmationsByEnfant(@Param("enfantId") Integer enfantId);

    // ========== RECHERCHE PAR PARENT ==========
    @Query("SELECT i FROM Inscription i WHERE i.session.date >= :date AND i.enfant.parent.id = :parentId")
    List<Inscription> findFuturesByParent(@Param("parentId") Integer parentId,
                                          @Param("date") LocalDate date);

    @Query("SELECT i FROM Inscription i WHERE i.session.date < :date AND i.enfant.parent.id = :parentId")
    List<Inscription> findPassesByParent(@Param("parentId") Integer parentId,
                                         @Param("date") LocalDate date);

    @Query("SELECT i FROM Inscription i WHERE i.enfant.parent.id = :parentId ORDER BY i.dateInscription DESC")
    List<Inscription> findAllByParent(@Param("parentId") Integer parentId);

    // ========== LISTE D'ATTENTE ==========
    @Query("SELECT i FROM Inscription i WHERE i.statut = 'EN_ATTENTE' ORDER BY i.positionAttente")
    List<Inscription> findListeAttente();

    @Query("SELECT i FROM Inscription i WHERE i.session.id = :sessionId AND i.statut = 'EN_ATTENTE' ORDER BY i.positionAttente")
    List<Inscription> findListeAttenteBySession(@Param("sessionId") Integer sessionId);

    @Query("SELECT MAX(i.positionAttente) FROM Inscription i WHERE i.session.id = :sessionId AND i.statut = 'EN_ATTENTE'")
    Integer getMaxPositionAttente(@Param("sessionId") Integer sessionId);

    // ========== RECHERCHE PAR STATUT ==========
    List<Inscription> findByStatut(String statut);

    @Query("SELECT i FROM Inscription i WHERE i.statut IN :statuts")
    List<Inscription> findByStatuts(@Param("statuts") List<String> statuts);

    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.statut = :statut")
    long countByStatut(@Param("statut") String statut);

    // ========== RECHERCHE PAR DATE ==========
    @Query("SELECT i FROM Inscription i WHERE i.dateInscription BETWEEN :debut AND :fin")
    List<Inscription> findByDateInscriptionBetween(@Param("debut") LocalDateTime debut,
                                                   @Param("fin") LocalDateTime fin);

    @Query("SELECT i FROM Inscription i WHERE DATE(i.dateInscription) = :date")
    List<Inscription> findByDateInscription(@Param("date") LocalDate date);

    // ========== POUR LE TABLEAU DE BORD ==========
    @Query("SELECT i FROM Inscription i ORDER BY i.dateInscription DESC")
    List<Inscription> findTop10ByOrderByDateInscriptionDesc();

    @Query("SELECT FUNCTION('YEAR', i.dateInscription) as annee, FUNCTION('MONTH', i.dateInscription) as mois, COUNT(i) " +
            "FROM Inscription i GROUP BY annee, mois ORDER BY annee DESC, mois DESC")
    List<Object[]> getStatistiquesParMois();

    // ========== STATISTIQUES AVANCÉES ==========
    @Query("SELECT i.session.activite.nom, COUNT(i) FROM Inscription i GROUP BY i.session.activite.nom")
    List<Object[]> countInscriptionsParActivite();

    @Query("SELECT i.statut, COUNT(i) FROM Inscription i GROUP BY i.statut")
    List<Object[]> countInscriptionsParStatut();

    @Query("SELECT AVG(CAST((SELECT COUNT(i2) FROM Inscription i2 WHERE i2.session = i.session AND i2.statut = 'CONFIRMEE') AS float)) " +
            "FROM Session i")
    Double getMoyenneInscriptionsParSession();

    // ========== RECHERCHE POUR CONFLITS ==========
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Inscription i " +
            "WHERE i.enfant.id = :enfantId AND i.session.id IN " +
            "(SELECT s.id FROM Session s WHERE s.date = :date AND " +
            "((s.heureDebut BETWEEN :heureDebut AND :heureFin) OR (s.heureFin BETWEEN :heureDebut AND :heureFin)))")
    boolean existsConflitHoraire(@Param("enfantId") Integer enfantId,
                                 @Param("date") LocalDate date,
                                 @Param("heureDebut") java.time.LocalTime heureDebut,
                                 @Param("heureFin") java.time.LocalTime heureFin);

    // ========== MISES À JOUR MASSIVES ==========
    @Modifying
    @Query("UPDATE Inscription i SET i.statut = 'ANNULEE' WHERE i.session.id = :sessionId AND i.statut != 'ANNULEE'")
    void annulerInscriptionsBySession(@Param("sessionId") Integer sessionId);

    @Modifying
    @Query("UPDATE Inscription i SET i.positionAttente = i.positionAttente - 1 " +
            "WHERE i.session.id = :sessionId AND i.statut = 'EN_ATTENTE' AND i.positionAttente > :position")
    void decrementerPositionsAttente(@Param("sessionId") Integer sessionId,
                                     @Param("position") Integer position);

    // ========== SUPPRESSION ==========
    @Modifying
    @Query("DELETE FROM Inscription i WHERE i.session.date < :date AND i.statut = 'ANNULEE'")
    void deleteAnciennesInscriptionsAnnulees(@Param("date") LocalDate date);

    // ========== RECHERCHE POUR NOTIFICATIONS ==========
    @Query("SELECT i FROM Inscription i WHERE i.session.date = :date AND i.statut = 'CONFIRMEE'")
    List<Inscription> findPourRappel(@Param("date") LocalDate date);

    @Query("SELECT i FROM Inscription i WHERE i.statut = 'EN_ATTENTE' AND i.positionAttente = 1")
    List<Inscription> findProchainsListeAttente();

    // ========== RECHERCHE AVANCÉE ==========
    @Query("SELECT i FROM Inscription i WHERE " +
            "(:enfantId IS NULL OR i.enfant.id = :enfantId) AND " +
            "(:sessionId IS NULL OR i.session.id = :sessionId) AND " +
            "(:statut IS NULL OR i.statut = :statut) AND " +
            "(:dateDebut IS NULL OR i.dateInscription >= :dateDebut) AND " +
            "(:dateFin IS NULL OR i.dateInscription <= :dateFin)")
    List<Inscription> rechercherInscriptions(@Param("enfantId") Integer enfantId,
                                             @Param("sessionId") Integer sessionId,
                                             @Param("statut") String statut,
                                             @Param("dateDebut") LocalDateTime dateDebut,
                                             @Param("dateFin") LocalDateTime dateFin);

    // ========== POUR LE TAUX DE REMPLISSAGE ==========
    @Query("SELECT i.session.id, COUNT(i) as nbInscrits, i.session.capaciteMax " +
            "FROM Inscription i WHERE i.session.id IN :sessionIds AND i.statut = 'CONFIRMEE' " +
            "GROUP BY i.session.id, i.session.capaciteMax")
    List<Object[]> getTauxRemplissageSessions(@Param("sessionIds") List<Integer> sessionIds);

    // ========== POUR LE SUIVI INDIVIDUEL ==========
    @Query("SELECT i FROM Inscription i WHERE i.enfant.id = :enfantId ORDER BY i.session.date DESC")
    List<Inscription> findHistoriqueEnfant(@Param("enfantId") Integer enfantId);

    @Query("SELECT i FROM Inscription i WHERE i.enfant.parent.id = :parentId AND i.statut = 'EN_ATTENTE'")
    List<Inscription> findAttenteByParent(@Param("parentId") Integer parentId);
}
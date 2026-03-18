package com.manara.portail.repository;

import com.manara.portail.model.Communication;
import com.manara.portail.model.Parent;
import com.manara.portail.model.Animation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Integer> {

    // ========== RECHERCHE PAR PARENT ==========
    List<Communication> findByParent(Parent parent);

    @Query("SELECT c FROM Communication c WHERE c.parent.id = :parentId")
    List<Communication> findByParentId(@Param("parentId") Integer parentId);

    // ========== RECHERCHE PAR ANIMATEUR ==========
    List<Communication> findByAnimation(Animation animation);

    @Query("SELECT c FROM Communication c WHERE c.animation.id = :animateurId")  // ✅ La méthode qui manquait
    List<Communication> findByAnimationId(@Param("animateurId") Integer animateurId);

    // ========== RECHERCHE PAR EXPÉDITEUR ==========
    @Query("SELECT c FROM Communication c WHERE c.parent IS NOT NULL")
    List<Communication> findEnvoyeesParParents();

    @Query("SELECT c FROM Communication c WHERE c.animation IS NOT NULL")
    List<Communication> findEnvoyeesParAnimateurs();

    // ========== MESSAGES NON LUS ==========
    @Query("SELECT c FROM Communication c WHERE c.parent.id = :parentId AND c.lu = false")
    List<Communication> findNonLuesByParent(@Param("parentId") Integer parentId);

    @Query("SELECT c FROM Communication c WHERE c.animation.id = :animateurId AND c.lu = false")
    List<Communication> findNonLuesByAnimateur(@Param("animateurId") Integer animateurId);

    @Query("SELECT COUNT(c) FROM Communication c WHERE c.parent.id = :parentId AND c.lu = false")
    long countNonLuesByParent(@Param("parentId") Integer parentId);

    @Query("SELECT COUNT(c) FROM Communication c WHERE c.animation.id = :animateurId AND c.lu = false")
    long countNonLuesByAnimateur(@Param("animateurId") Integer animateurId);

    // ========== RECHERCHE PAR CANAL ==========
    List<Communication> findByCanal(String canal);

    @Query("SELECT c FROM Communication c WHERE c.canal = :canal ORDER BY c.dateEnvoi DESC")
    List<Communication> findRecentByCanal(@Param("canal") String canal);

    // ========== RECHERCHE PAR DATE ==========
    @Query("SELECT c FROM Communication c WHERE c.dateEnvoi BETWEEN :debut AND :fin")
    List<Communication> findBetweenDates(@Param("debut") LocalDateTime debut,
                                         @Param("fin") LocalDateTime fin);

    @Query("SELECT c FROM Communication c WHERE DATE(c.dateEnvoi) = :date")
    List<Communication> findByDateEnvoi(@Param("date") LocalDate date);

    // ========== RECHERCHE PAR SUJET ==========
    @Query("SELECT c FROM Communication c WHERE LOWER(c.sujet) LIKE LOWER(CONCAT('%', :sujet, '%'))")
    List<Communication> rechercherParSujet(@Param("sujet") String sujet);

    @Query("SELECT c FROM Communication c WHERE LOWER(c.message) LIKE LOWER(CONCAT('%', :texte, '%'))")
    List<Communication> rechercherParContenu(@Param("texte") String texte);

    // ========== COMMUNICATIONS GÉNÉRALES ==========
    @Query("SELECT c FROM Communication c WHERE c.parent IS NULL AND c.animation IS NULL")
    List<Communication> findCommunicationsGenerales();

    @Query("SELECT c FROM Communication c WHERE c.parent IS NULL AND c.animation IS NULL ORDER BY c.dateEnvoi DESC")
    List<Communication> findRecentCommunicationsGenerales();

    // ========== RECHERCHE PAR DESTINATAIRE ==========
    @Query("SELECT c FROM Communication c WHERE c.parent.id = :destinataireId OR c.animation.id = :destinataireId")
    List<Communication> findCommunicationsPourUtilisateur(@Param("destinataireId") Integer destinataireId);

    // ========== STATISTIQUES ==========
    @Query("SELECT YEAR(c.dateEnvoi) as annee, MONTH(c.dateEnvoi) as mois, COUNT(c) as total " +
            "FROM Communication c GROUP BY YEAR(c.dateEnvoi), MONTH(c.dateEnvoi) ORDER BY annee DESC, mois DESC")
    List<Object[]> getStatistiquesCommunicationsParMois();

    @Query("SELECT c.canal, COUNT(c) FROM Communication c GROUP BY c.canal")
    List<Object[]> getStatistiquesParCanal();

    @Query("SELECT CASE WHEN c.parent IS NOT NULL THEN 'PARENT' ELSE 'ANIMATEUR' END, COUNT(c) " +
            "FROM Communication c WHERE c.parent IS NOT NULL OR c.animation IS NOT NULL GROUP BY CASE WHEN c.parent IS NOT NULL THEN 'PARENT' ELSE 'ANIMATEUR' END")
    List<Object[]> getStatistiquesParExpediteur();

    // ========== MISES À JOUR ==========
    @Modifying
    @Query("UPDATE Communication c SET c.lu = true WHERE c.id = :id")
    void marquerCommeLue(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Communication c SET c.lu = true WHERE c.parent.id = :parentId AND c.lu = false")
    void marquerToutesCommeLues(@Param("parentId") Integer parentId);

    @Modifying
    @Query("UPDATE Communication c SET c.lu = true WHERE c.animation.id = :animateurId AND c.lu = false")
    void marquerToutesCommeLuesAnimateur(@Param("animateurId") Integer animateurId);

    // ========== RECHERCHE AVANCÉE ==========
    @Query("SELECT c FROM Communication c WHERE " +
            "(:parentId IS NULL OR c.parent.id = :parentId) AND " +
            "(:animateurId IS NULL OR c.animation.id = :animateurId) AND " +
            "(:canal IS NULL OR c.canal = :canal) AND " +
            "(:lu IS NULL OR c.lu = :lu) AND " +
            "(:dateDebut IS NULL OR c.dateEnvoi >= :dateDebut) AND " +
            "(:dateFin IS NULL OR c.dateEnvoi <= :dateFin)")
    List<Communication> rechercherCommunications(@Param("parentId") Integer parentId,
                                                 @Param("animateurId") Integer animateurId,
                                                 @Param("canal") String canal,
                                                 @Param("lu") Boolean lu,
                                                 @Param("dateDebut") LocalDateTime dateDebut,
                                                 @Param("dateFin") LocalDateTime dateFin);

    // ========== SUPPRESSION ==========
    @Modifying
    @Query("DELETE FROM Communication c WHERE c.dateEnvoi < :date AND c.lu = true")
    void deleteAnciennesLues(@Param("date") LocalDateTime date);

    // ========== POUR LE TABLEAU DE BORD ==========
    @Query("SELECT c FROM Communication c ORDER BY c.dateEnvoi DESC")
    List<Communication> findDernieresCommunications();

    @Query("SELECT c FROM Communication c WHERE c.lu = false ORDER BY c.dateEnvoi DESC")
    List<Communication> findCommunicationsNonLues();

    // ========== COMPTAGES ==========
    long countByParentId(Integer parentId);

    long countByAnimationId(Integer animateurId);

    @Query("SELECT COUNT(c) FROM Communication c WHERE c.dateEnvoi > :since")
    long countCommunicationsRecentes(@Param("since") LocalDateTime since);
}
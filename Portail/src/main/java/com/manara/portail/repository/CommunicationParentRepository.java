package com.manara.portail.repository;

import com.manara.portail.model.CommunicationParent;
import com.manara.portail.model.Parent;
import com.manara.portail.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommunicationParentRepository extends JpaRepository<CommunicationParent, Integer> {

    List<CommunicationParent> findByParent(Parent parent);

    @Query("SELECT c FROM CommunicationParent c WHERE c.parent.id = :parentId")
    List<CommunicationParent> findByParentId(@Param("parentId") Integer parentId);

    List<CommunicationParent> findByAnimateur(Utilisateur animateur);

    @Query("SELECT c FROM CommunicationParent c WHERE c.animateur.id = :animateurId")
    List<CommunicationParent> findByAnimateurId(@Param("animateurId") Integer animateurId);

    @Query("SELECT c FROM CommunicationParent c WHERE c.parent.id = :parentId AND c.lu = false")
    List<CommunicationParent> findNonLuesByParent(@Param("parentId") Integer parentId);

    @Query("SELECT COUNT(c) FROM CommunicationParent c WHERE c.parent.id = :parentId AND c.lu = false")
    long countNonLuesByParent(@Param("parentId") Integer parentId);

    List<CommunicationParent> findByCanal(String canal);

    @Query("SELECT c FROM CommunicationParent c WHERE c.dateEnvoi BETWEEN :debut AND :fin")
    List<CommunicationParent> findBetweenDates(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT c FROM CommunicationParent c WHERE LOWER(c.sujet) LIKE LOWER(CONCAT('%', :sujet, '%'))")
    List<CommunicationParent> rechercherParSujet(@Param("sujet") String sujet);

    @Modifying
    @Query("UPDATE CommunicationParent c SET c.lu = true WHERE c.id = :id")
    void marquerCommeLue(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE CommunicationParent c SET c.lu = true WHERE c.parent.id = :parentId AND c.lu = false")
    void marquerToutesCommeLues(@Param("parentId") Integer parentId);
}
package com.manara.portail.repository;

import com.manara.portail.model.Communication;
import com.manara.portail.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Integer> {

    List<Communication> findByParent(Parent parent);

    @Query("SELECT c FROM Communication c WHERE c.parent.id = :parentId")
    List<Communication> findByParentId(@Param("parentId") Integer parentId);

    @Query("SELECT c FROM Communication c WHERE c.parent.id = :parentId AND c.lu = false")
    List<Communication> findNonLuesByParent(@Param("parentId") Integer parentId);

    @Query("SELECT COUNT(c) FROM Communication c WHERE c.parent.id = :parentId AND c.lu = false")
    long countNonLuesByParent(@Param("parentId") Integer parentId);

    List<Communication> findByCanal(String canal);

    @Query("SELECT c FROM Communication c WHERE c.dateEnvoi BETWEEN :debut AND :fin")
    List<Communication> findBetweenDates(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT c FROM Communication c WHERE LOWER(c.sujet) LIKE LOWER(CONCAT('%', :sujet, '%'))")
    List<Communication> rechercherParSujet(@Param("sujet") String sujet);

    @Modifying
    @Query("UPDATE Communication c SET c.lu = true WHERE c.id = :id")
    void marquerCommeLue(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Communication c SET c.lu = true WHERE c.parent.id = :parentId AND c.lu = false")
    void marquerToutesCommeLues(@Param("parentId") Integer parentId);
}
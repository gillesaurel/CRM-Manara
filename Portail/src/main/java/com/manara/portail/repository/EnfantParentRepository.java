package com.manara.portail.repository;

import com.manara.portail.model.EnfantParent;
import com.manara.portail.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnfantParentRepository extends JpaRepository<EnfantParent, Integer> {

    List<EnfantParent> findByParent(Parent parent);

    @Query("SELECT e FROM EnfantParent e WHERE e.parent.id = :parentId")
    List<EnfantParent> findByParentId(@Param("parentId") Integer parentId);

    @Query("SELECT COUNT(e) FROM EnfantParent e WHERE e.parent.id = :parentId")
    long countByParentId(@Param("parentId") Integer parentId);

    @Query("SELECT e FROM EnfantParent e WHERE e.dateNaissance > :date")
    List<EnfantParent> findNesApres(@Param("date") LocalDate date);

    @Query("SELECT e FROM EnfantParent e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<EnfantParent> rechercherParNom(@Param("search") String search);

    @Query("SELECT e FROM EnfantParent e WHERE e.id IN " +
            "(SELECT i.enfant.id FROM InscriptionParent i WHERE i.session.id = :sessionId)")
    List<EnfantParent> findBySessionId(@Param("sessionId") Integer sessionId);

    @Query("SELECT e FROM EnfantParent e WHERE e.id IN " +
            "(SELECT i.enfant.id FROM InscriptionParent i WHERE i.session.id = :sessionId AND i.statut = 'CONFIRMEE')")
    List<EnfantParent> findBySessionIdAndConfirmee(@Param("sessionId") Integer sessionId);

    @Query("SELECT e FROM EnfantParent e WHERE e.allergies IS NOT NULL AND e.allergies <> ''")
    List<EnfantParent> findAvecAllergies();

    @Query("SELECT FLOOR((CURRENT_DATE - e.dateNaissance) / 365) as age, COUNT(e) " +
            "FROM EnfantParent e GROUP BY age ORDER BY age")
    List<Object[]> getStatistiquesParAge();
}
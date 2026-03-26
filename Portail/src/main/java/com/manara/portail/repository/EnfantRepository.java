package com.manara.portail.repository;

import com.manara.portail.model.Enfant;
import com.manara.portail.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnfantRepository extends JpaRepository<Enfant, Integer> {

    List<Enfant> findByParent(Parent parent);

    @Query("SELECT e FROM Enfant e WHERE e.parent.id = :parentId")
    List<Enfant> findByParentId(@Param("parentId") Integer parentId);

    @Query("SELECT COUNT(e) FROM Enfant e WHERE e.parent.id = :parentId")
    long countByParentId(@Param("parentId") Integer parentId);

    @Query("SELECT e FROM Enfant e WHERE e.dateNaissance > :date")
    List<Enfant> findNesApres(@Param("date") LocalDate date);

    @Query("SELECT e FROM Enfant e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Enfant> rechercherParNom(@Param("search") String search);

    @Query("SELECT e FROM Enfant e WHERE e.id IN " +
            "(SELECT i.enfant.id FROM InscriptionEnfant i WHERE i.session.id = :sessionId)")
    List<Enfant> findBySessionId(@Param("sessionId") Integer sessionId);

    @Query("SELECT e FROM Enfant e WHERE e.id IN " +
            "(SELECT i.enfant.id FROM InscriptionEnfant i WHERE i.session.id = :sessionId AND i.enAttente = false)")
    List<Enfant> findBySessionIdAndConfirmee(@Param("sessionId") Integer sessionId);

    @Query("SELECT e FROM Enfant e WHERE e.allergies IS NOT NULL AND e.allergies <> ''")
    List<Enfant> findAvecAllergies();

    @Query("SELECT FLOOR((CURRENT_DATE - e.dateNaissance) / 365) as age, COUNT(e) " +
            "FROM Enfant e GROUP BY age ORDER BY age")
    List<Object[]> getStatistiquesParAge();
}
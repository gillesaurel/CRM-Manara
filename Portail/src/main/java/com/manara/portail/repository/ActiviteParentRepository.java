package com.manara.portail.repository;

import com.manara.portail.model.ActiviteParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActiviteParentRepository extends JpaRepository<ActiviteParent, Integer> {

    List<ActiviteParent> findByActiveTrue();

    @Query("SELECT a FROM ActiviteParent a WHERE a.ageMin <= :age AND a.ageMax >= :age")
    List<ActiviteParent> findAdapteesPourAge(@Param("age") int age);

    @Query("SELECT a FROM ActiviteParent a WHERE LOWER(a.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<ActiviteParent> rechercherParNom(@Param("nom") String nom);

    @Query("SELECT a FROM ActiviteParent a ORDER BY SIZE(a.sessions) DESC")
    List<ActiviteParent> findTop5ByOrderByNbInscriptionsDesc();

    @Query("SELECT COUNT(a) FROM ActiviteParent a WHERE a.active = true")
    long countByActiveTrue();

    @Query("SELECT a FROM ActiviteParent a WHERE a.active = true")
    List<ActiviteParent> findAllActive();

    boolean existsByNom(String nom);
}
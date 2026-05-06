package com.manara.portail.repository;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.ActiviteParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActiviteRepository extends JpaRepository<ActiviteAdmin, Long>{

    @Query("SELECT a FROM ActiviteAdmin a LEFT JOIN FETCH a.inscriptions WHERE a.id = :id")
    Optional<ActiviteAdmin> findByIdWithInscriptions(@Param("id") Long id);

}
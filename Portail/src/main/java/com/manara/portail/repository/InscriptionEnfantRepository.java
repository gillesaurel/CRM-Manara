package com.manara.portail.repository;

import com.manara.portail.model.InscriptionEnfant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscriptionEnfantRepository extends JpaRepository<InscriptionEnfant, Long> {
    List<InscriptionEnfant> findByActiviteId(Long activiteId);
    long countByActiviteIdAndEnAttenteFalse(Long activiteId);
    long countByActiviteIdAndEnAttenteTrue(Long activiteId);
    List<InscriptionEnfant> findByActiviteIdAndEnAttenteTrueOrderByIdAsc(Long activiteId);
}

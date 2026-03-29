package com.manara.portail.repository;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.model.SessionAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<SessionAdmin, Long> {
    public List<SessionAdmin> findAllByActivite(ActiviteAdmin activite);
    List<SessionAdmin> findByActivite_Id(Long id);
}

package com.manara.portail.repository;

import com.manara.portail.model.Activite;
import com.manara.portail.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    public List<Session> findAllByActivite(Activite activite);
    List<Session> findByActivite_Id(Long id);
}

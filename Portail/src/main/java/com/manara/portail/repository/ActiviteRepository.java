package com.manara.portail.repository;

import com.manara.portail.model.Activite;
import com.manara.portail.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActiviteRepository extends JpaRepository<Activite, Long>{

}

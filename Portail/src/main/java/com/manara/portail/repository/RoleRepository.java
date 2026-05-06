package com.manara.portail.repository;

import com.manara.portail.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByNom(String nom);

    @Query("SELECT r FROM Role r WHERE r.nom LIKE %?1% OR r.description LIKE %?1%")
    List<Role> search(String keyword);
}
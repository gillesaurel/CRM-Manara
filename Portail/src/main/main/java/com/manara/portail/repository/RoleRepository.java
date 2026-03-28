package com.manara.portail.repository;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT u FROM Role u WHERE u.nom LIKE %?1%")
    public Role findByNom(String nom);

    @Query("SELECT u FROM Role u WHERE u.nom  LIKE %?1% OR u.description  LIKE %?1%")
    public List<Role> findAll(String keyword);


}

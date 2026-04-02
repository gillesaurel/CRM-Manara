package com.manara.portail.repository;

import com.manara.portail.model.Enfant;
import com.manara.portail.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Utilisateur, Long> {
    @Query("SELECT u FROM Utilisateur u WHERE " +
            "u.nom LIKE %?1% OR " +
            "u.prenom LIKE %?1% OR " +
            "u.email LIKE %?1% OR " +
            "u.telephone LIKE %?1%")
    List<Utilisateur> rechercheUtilisateur(String keyword);

    @Query("SELECT u FROM Utilisateur u WHERE (" +
            "u.nom LIKE %:keyword% OR " +
            "u.prenom LIKE %:keyword% OR " +
            "u.email LIKE %:keyword% OR " +
            "u.telephone LIKE %:keyword%) AND u.role.nom = :role")
    List<Utilisateur> rechercheUtilisateurByRole(@Param("keyword") String keyword,
                                                 @Param("role") String role);

    @Query("SELECT c FROM Enfant c WHERE c.nom LIKE %?1%")
    List<Enfant> rechercheEnfant(String keyword);

    @Query("SELECT c FROM Enfant c")
    List<Enfant> findAllEnfant();

    @Query("SELECT u FROM Utilisateur u WHERE u.role.nom = ?1")
    List<Utilisateur> findByRole(String role);
}

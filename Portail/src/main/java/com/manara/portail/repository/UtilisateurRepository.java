package com.manara.portail.repository;

import com.manara.portail.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer>  {
    @Query("SELECT u FROM Utilisateur u WHERE u.email = :email")
    public Utilisateur getUtilisateurByEmail(@Param("email") String email);

    @Query("SELECT u FROM Utilisateur u WHERE u.email = :email and u.motdepasse=:motdepasse")
    //@Param est utilisé pour lier le paramètre method au paramètre Query.
    public Utilisateur getUtilisateurByEmailAndMotDePasse(@Param("email") String email,@Param("motdepasse") String motdepasse);

    @Query("SELECT u FROM Utilisateur u WHERE u.nom  LIKE %?1% OR u.prenom  LIKE %?1%")
    public List<Utilisateur> findAll(String keyword);

    @Query("SELECT u FROM Utilisateur u JOIN u.role r WHERE r.nom = ?1")
    public List<Utilisateur> findAllByNameRole(String nom);


    public Utilisateur findByNom(String nom);
    //  Recherche par nom et MotDePasse (utilisation de la convention de nommage)
    public Utilisateur findByNomAndMotdepasse(@Param("nom") String nom, @Param("motdepasse") String motdepasse);
    List<Utilisateur> findByRoleNom(String nomRole);
    // Recherche par nom equale (utilisation de la convention de nommage)
    public Utilisateur findByNomEquals(String nom);

}

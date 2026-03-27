// fait par Manu Charland-Rivard

package com.manara.portail.service;

import com.manara.portail.model.Role;
import com.manara.portail.model.Utilisateur;
import com.manara.portail.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }

    public Role findByName(String nom) { return roleRepository.findByNom(nom);}

}

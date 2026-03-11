package com.manara.portail.service;

import com.manara.portail.model.Role;
import com.manara.portail.repos.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    public Optional<Role> findByName(String nom) {
        return roleRepository.findByNom(nom);
    }
}

package com.manara.portail.service;

import com.manara.portail.model.Enfant;
import com.manara.portail.repository.EnfantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnfantService {

    @Autowired
    private EnfantRepository enfantRepository;

    public List<Enfant> getAllEnfants() {
        return enfantRepository.findAll();
    }

    public Enfant getEnfantById(Integer id) {
        return enfantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enfant introuvable avec id: " + id));
    }
}
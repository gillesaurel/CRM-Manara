package com.manara.portail.service;

import com.manara.portail.model.ActiviteAdmin;
import com.manara.portail.repository.ActiviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActiviteService {
    @Autowired
    private ActiviteRepository activiteRepository;

    public List<ActiviteAdmin> getAllActivites() {
        return activiteRepository.findAll();
    }

    public ActiviteAdmin save(ActiviteAdmin activite) {
        return activiteRepository.save(activite);
    }

    public ActiviteAdmin getById(Long id) {
        return activiteRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        if (!activiteRepository.existsById(id)) {
            throw new ActiviteNotFoundException("L'activité avec l'ID " + id + " n'existe pas.");
        }
        activiteRepository.deleteById(id);
    }
}

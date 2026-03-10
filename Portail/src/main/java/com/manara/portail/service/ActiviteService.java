package com.manara.portail.service;

import com.manara.portail.model.Activite;
import com.manara.portail.repository.ActiviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActiviteService {
    @Autowired
    private ActiviteRepository activiteRepository;

    public List<Activite> getAllActivites() {
        return activiteRepository.findAll();
    }

    public Activite save(Activite activite) {
        return activiteRepository.save(activite);
    }

    public Activite getById(Long id) {
        return activiteRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        if (!activiteRepository.existsById(id)) {
            throw new ActiviteNotFoundException("L'activité avec l'ID " + id + " n'existe pas.");
        }
        activiteRepository.deleteById(id);
    }
}

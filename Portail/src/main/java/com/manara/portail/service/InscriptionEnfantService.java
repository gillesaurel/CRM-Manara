package com.manara.portail.service;

import com.manara.portail.model.InscriptionEnfant;
import com.manara.portail.repository.InscriptionEnfantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InscriptionEnfantService {
    @Autowired
    private InscriptionEnfantRepository inscriptionEnfantRepository;

    public List<InscriptionEnfant> getInscriptionsBySession(Integer sessionId) {
        return inscriptionEnfantRepository.findBySessionId(sessionId);
    }
}

package com.manara.portail.service;

import com.manara.portail.model.SessionParent;
import com.manara.portail.repository.SessionParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionParentService {

    @Autowired
    private SessionParentRepository sessionParentRepository;

    public List<SessionParent> getSessionsAvecEnfantsByAnimateur(Integer animateurId) {
        return sessionParentRepository.findSessionsAvecEnfantsByAnimateur(animateurId);
    }
}
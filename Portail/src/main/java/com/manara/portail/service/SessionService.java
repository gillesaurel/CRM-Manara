package com.manara.portail.service;

import com.manara.portail.model.Session;
import com.manara.portail.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session save(Session session) {
        return sessionRepository.save(session);
    }
}

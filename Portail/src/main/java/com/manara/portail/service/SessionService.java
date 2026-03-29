package com.manara.portail.service;

import com.manara.portail.model.SessionAdmin;
import com.manara.portail.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public List<SessionAdmin> getAllSessions() {
        return sessionRepository.findAll();
    }

    public SessionAdmin save(SessionAdmin session) {
        return sessionRepository.save(session);
    }
}

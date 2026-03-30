package com.manara.portail.service;

public class ActiviteDejaFullException extends RuntimeException {
    public ActiviteDejaFullException(String message) {
        super(message);
    }
}

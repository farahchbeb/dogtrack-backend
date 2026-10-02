package com.dogtrack.dogtrack_backend.security;

import com.dogtrack.dogtrack_backend.entity.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

    // token -> utilisateur connecté
    private final Map<String, Utilisateur> sessions = new ConcurrentHashMap<>();

    public String creerToken(Utilisateur utilisateur) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, utilisateur);
        return token;
    }

    public Utilisateur getUtilisateur(String token) {
        return sessions.get(token);
    }

    public void invalider(String token) {
        sessions.remove(token);
    }
}
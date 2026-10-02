package com.dogtrack.dogtrack_backend.repository;

import com.dogtrack.dogtrack_backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByLogin(String login);
    boolean existsByLogin(String login);
}
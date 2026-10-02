package com.dogtrack.dogtrack_backend.repository;

import com.dogtrack.dogtrack_backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByNomContainingIgnoreCase(String nom);
}
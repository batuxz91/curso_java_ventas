package com.cesarmaydana.cursojava.repository;

import com.cesarmaydana.cursojava.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
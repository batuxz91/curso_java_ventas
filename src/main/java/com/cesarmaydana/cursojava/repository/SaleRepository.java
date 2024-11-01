package com.cesarmaydana.cursojava.repository;

import com.cesarmaydana.cursojava.model.Sale;
import com.cesarmaydana.cursojava.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}

package com.cesarmaydana.cursojava.repository;

import com.cesarmaydana.cursojava.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Product, Long> {
}

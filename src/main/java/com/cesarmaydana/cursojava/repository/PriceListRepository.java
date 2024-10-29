package com.cesarmaydana.cursojava.repository;

import com.cesarmaydana.cursojava.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {
    List<PriceList> findByProductoId(Long productoId);
}

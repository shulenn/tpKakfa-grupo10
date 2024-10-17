package com.tpKafka_grupo10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpKafka_grupo10.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Método para encontrar el stock por productoId
    Stock findByProductoCodigo(Long productoId);
}
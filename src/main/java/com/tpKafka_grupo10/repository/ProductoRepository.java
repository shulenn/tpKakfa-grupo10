package com.tpKafka_grupo10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpKafka_grupo10.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
   
}
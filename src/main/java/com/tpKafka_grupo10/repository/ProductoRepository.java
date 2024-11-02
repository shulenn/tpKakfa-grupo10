package com.tpKafka_grupo10.repository;

import com.tpKafka_grupo10.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Método para buscar un producto por su código
    Producto findByCodigo(Long codigo);
}

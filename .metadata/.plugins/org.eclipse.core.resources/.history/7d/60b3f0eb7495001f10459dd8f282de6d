package com.tpKafka_grupo10.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tpKafka_grupo10.model.EstadoOrden;
import com.tpKafka_grupo10.model.OrdenCompra;

import jakarta.transaction.Transactional;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
	
	@Modifying
	@Transactional
	@Query("UPDATE OrdenCompra oc SET oc.estado = :estado WHERE oc.codigo = :codigo")
	void actualizarEstadoOrdenCompra(@Param("codigo") Long codigo, @Param("estado") EstadoOrden estado);
	
	@Query("SELECT o FROM OrdenCompra o WHERE o.estado = :estado AND o.pausada = :pausada")
	List<OrdenCompra> findByEstadoAndPausada(@Param("estado") EstadoOrden estado, @Param("pausada") boolean pausada);
	
	@Modifying
	@Query("UPDATE OrdenCompra o SET o.estado = :estado, o.pausada = :pausada WHERE o.codigo = :codigo")
	void actualizarEstadoYPausada(@Param("codigo") Long codigo, @Param("estado") EstadoOrden estado, @Param("pausada") boolean pausada);
	
	@Query("SELECT o FROM OrdenCompra o JOIN o.itemsOrdenCompra i WHERE o.pausada = true AND i.producto.codigo = :productoId")
    List<OrdenCompra> findOrdenesPausadasPorProducto(@Param("productoId") Long productoId);

}


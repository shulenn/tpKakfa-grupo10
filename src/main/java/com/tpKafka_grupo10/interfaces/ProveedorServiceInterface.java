package com.tpKafka_grupo10.interfaces;

import com.tpKafka_grupo10.model.OrdenCompra;

public interface ProveedorServiceInterface {
    boolean proveedorProveeProducto(Long productoId);
    void procesarOrdenCompra(OrdenCompra ordenCompra);
}

package com.tpKafka_grupo10.event;

import java.io.Serializable;

public class StockUpdateEvent implements Serializable {
 
    private Long productoId;
    private int nuevaCantidad;

    public StockUpdateEvent() {
        // Constructor por defecto
    }

	public StockUpdateEvent(Long productoId, int nuevaCantidad) {
		super();
		this.productoId = productoId;
		this.nuevaCantidad = nuevaCantidad;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

	public int getNuevaCantidad() {
		return nuevaCantidad;
	}

	public void setNuevaCantidad(int nuevaCantidad) {
		this.nuevaCantidad = nuevaCantidad;
	}
    
    

    
    
}

package com.tpKafka_grupo10.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tienda_id", nullable = false)
    private Long tiendaId;

    @ManyToOne
    @JoinColumn(name = "producto_codigo", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    
    
    public Stock() {
		super();
	}
    

	public Stock(Long id, Long tiendaId, Producto producto, int cantidad) {
		super();
		this.id = id;
		this.tiendaId = tiendaId;
		this.producto = producto;
		this.cantidad = cantidad;
	}


	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTiendaId() {
        return tiendaId;
    }

    public void setTiendaId(Long tiendaId) {
        this.tiendaId = tiendaId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
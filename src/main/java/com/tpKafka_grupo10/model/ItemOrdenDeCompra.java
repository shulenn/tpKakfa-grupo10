package com.tpKafka_grupo10.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "item_orden_compra")
@JsonIgnoreProperties({"ordenDeCompra"})
public class ItemOrdenDeCompra {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Long codigo;

	@Column(name = "color", length = 45)
	private String color;

	@Column(name = "talle", length = 45)
	private String talle;

	@Column(name = "cantidad")
	private int cantidad;

	@ManyToOne
	@JoinColumn(name = "producto_codigo", referencedColumnName = "codigo")
	private Producto producto;

	@ManyToOne
	@JoinColumn(name = "orden_compra_codigo")
	private OrdenCompra ordenCompra;
	

	public ItemOrdenDeCompra() {
		super();
	}
	
	public ItemOrdenDeCompra(Long codigo, String color, String talle, int cantidad, Producto producto,
			OrdenCompra ordenCompra) {
		super();
		this.codigo = codigo;
		this.color = color;
		this.talle = talle;
		this.cantidad = cantidad;
		this.producto = producto;
		this.ordenCompra = ordenCompra;
	}

	// Getters and Setters
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTalle() {
		return talle;
	}

	public void setTalle(String talle) {
		this.talle = talle;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public OrdenCompra getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(OrdenCompra ordenCompra) {
		this.ordenCompra = ordenCompra;
	}

}

package com.tpKafka_grupo10.model;

import jakarta.persistence.*;

@Entity
@Table(name = "item_despacho")
public class ItemDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto; // Relación directa con el producto

    @Column(nullable = false)
    private int cantidad; // Cantidad del producto en el despacho

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_despacho_id", nullable = false)
    private OrdenDespacho ordenDespacho; // Relación con la orden de despacho

    // Constructores, getters y setters

    public ItemDespacho() {
    }

    public ItemDespacho(Producto producto, int cantidad, OrdenDespacho ordenDespacho) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.ordenDespacho = ordenDespacho;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public OrdenDespacho getOrdenDespacho() {
        return ordenDespacho;
    }

    public void setOrdenDespacho(OrdenDespacho ordenDespacho) {
        this.ordenDespacho = ordenDespacho;
    }
}

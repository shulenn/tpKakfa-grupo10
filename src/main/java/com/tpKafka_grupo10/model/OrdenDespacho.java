package com.tpKafka_grupo10.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class OrdenDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_orden_compra")
    private Long idOrdenCompra;

    @Column(name = "fecha_estimada_envio")
    private LocalDate fechaEstimadaEnvio;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_despacho_id")
    private List<ItemDespacho> productos = new ArrayList<>(); // Inicializar la lista

    public OrdenDespacho() {
    }

    public OrdenDespacho(Long idOrdenCompra, LocalDate fechaEstimadaEnvio) {
        this.idOrdenCompra = idOrdenCompra;
        this.fechaEstimadaEnvio = fechaEstimadaEnvio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(Long idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public LocalDate getFechaEstimadaEnvio() {
        return fechaEstimadaEnvio;
    }

    public void setFechaEstimadaEnvio(LocalDate fechaEstimadaEnvio) {
        this.fechaEstimadaEnvio = fechaEstimadaEnvio;
    }

    public List<ItemDespacho> getProductos() {
        return productos;
    }

    public void setProductos(List<ItemDespacho> productos) {
        this.productos = productos;
    }

    // Método para agregar un producto a la orden de despacho
    public void agregarProducto(ItemDespacho item) {
        productos.add(item);
    }

    // Método para eliminar un producto de la orden de despacho
    public void eliminarProducto(ItemDespacho item) {
        productos.remove(item);
    }
    
    @Override
    public String toString() {
        return "OrdenDespacho{" +
               "id=" + id +
               ", idOrdenCompra=" + idOrdenCompra +
               ", fechaEstimadaEnvio=" + fechaEstimadaEnvio +
               '}';
    }

}


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

    private Long idOrdenCompra;

    private LocalDate fechaEstimadaEnvio;

    @Enumerated(EnumType.STRING)
    private EstadoDespacho estado;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_despacho_id")
    private List<ItemDespacho> productos = new ArrayList<>(); // Inicializar la lista

    public OrdenDespacho() {
    }

    public OrdenDespacho(Long idOrdenCompra, LocalDate fechaEstimadaEnvio, EstadoDespacho estado) {
        this.idOrdenCompra = idOrdenCompra;
        this.fechaEstimadaEnvio = fechaEstimadaEnvio;
        this.estado = estado;
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

    public EstadoDespacho getEstado() {
        return estado;
    }

    public void setEstado(EstadoDespacho estado) {
        this.estado = estado;
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

    // Método para validar el estado de la orden
    public boolean esCompletada() {
        return estado == EstadoDespacho.ENTREGADO;
    }
}

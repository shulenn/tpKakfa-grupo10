package com.tpKafka_grupo10.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tpKafka_grupo10.model.EstadoOrden;


import jakarta.persistence.*;

@Entity
@Table(name = "orden_compra") // Nombre de la tabla en MySQL
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de IDs
    @Column(name = "codigo")
    private Long id;

    @Column(name = "tienda_id")
    private Long tiendaId;

    @Enumerated(EnumType.STRING) // Almacenar el valor del enum como cadena
    private EstadoOrden estado;
    
    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "orden_despacho")
    private String ordenDespacho;
    
    @Column(name = "fecha_solicitud")
    private LocalDate fechaSolicitud;

    @Column(name = "fecha_recepcion")
    private LocalDate fechaRecepcion;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL) // Relación uno a muchos con Productos
    private List<ItemOrdenDeCompra> itemsOrdenCompra;
    
    public OrdenCompra() {
    	this.itemsOrdenCompra = new ArrayList<>();
        // Constructor por defecto
    }

    public OrdenCompra(Long tiendaId, String observaciones, String ordenDespacho, LocalDate fechaSolicitud, LocalDate fechaRecepcion, List<ItemOrdenDeCompra> itemsOrdenCompra) {
        this.tiendaId = tiendaId;
        this.estado = EstadoOrden.SOLICITADA;
        this.observaciones = observaciones;
        this.ordenDespacho = ordenDespacho;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaRecepcion = fechaRecepcion;
        this.itemsOrdenCompra = (itemsOrdenCompra != null) ? itemsOrdenCompra : new ArrayList<>();
    }


    // Getters y Setters

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

	public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getOrdenDespacho() {
        return ordenDespacho;
    }

    public void setOrdenDespacho(String ordenDespacho) {
        this.ordenDespacho = ordenDespacho;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDate fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

	public List<ItemOrdenDeCompra> getItemsOrdenCompra() {
		return itemsOrdenCompra;
	}

	public void setItemsOrdenCompra(List<ItemOrdenDeCompra> itemsOrdenCompra) {
		this.itemsOrdenCompra = itemsOrdenCompra;
	}

    
}


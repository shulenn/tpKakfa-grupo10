package com.tpKafka_grupo10.model;

import jakarta.persistence.*;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @Column(name = "talle", length = 10)
    private String talle;

    @Column(name = "foto", length = 255)
    private String foto;

    @Column(name = "color", length = 50)
    private String color;
    
    
    public Producto() {
		super();
	}
    
	public Producto(String nombre, String talle, String foto, String color) {
		super();
		this.nombre = nombre;
		this.talle = talle;
		this.foto = foto;
		this.color = color;
	}

	// Getters and Setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

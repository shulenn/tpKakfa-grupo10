package com.tpKafka_grupo10.model;
import jakarta.persistence.*;

@Entity
@Table(name = "tienda")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "direccion", length = 255, nullable = false)
    private String direccion;

    @Column(name = "ciudad", length = 100, nullable = false)
    private String ciudad;

    @Column(name = "provincia", length = 100, nullable = false)
    private String provincia;

    @Column(name = "habilitada", nullable = false)
    private boolean habilitada;
    

    public Tienda() {
		super();
	}

	public Tienda(Long codigo, String direccion, String ciudad, String provincia, boolean habilitada) {
		super();
		this.codigo = codigo;
		this.direccion = direccion;
		this.ciudad = ciudad;
		this.provincia = provincia;
		this.habilitada = habilitada;
	}

	// Getters and Setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public boolean isHabilitada() {
        return habilitada;
    }

    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
}

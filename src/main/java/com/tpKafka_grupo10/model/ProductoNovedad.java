package com.tpKafka_grupo10.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;

public class ProductoNovedad {
	@Id
	private Long codigo;
	
	private String nombre;
	
	@ElementCollection
	private List<String> talles;
	
	@ElementCollection
	private List<String> colores;

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

	public List<String> getTalles() {
		return talles;
	}

	public void setTalles(List<String> talles) {
		this.talles = talles;
	}

	public List<String> getColores() {
		return colores;
	}

	public void setColores(List<String> colores) {
		this.colores = colores;
	}

	

}

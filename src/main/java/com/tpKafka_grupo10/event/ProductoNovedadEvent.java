package com.tpKafka_grupo10.event;

import java.util.List;
import java.util.Map;

public class ProductoNovedadEvent {

    private Long codigoProducto;
    private String nombre;
    private Map<String, List<String>> tallesYColores; // Mapa de talles con sus respectivos colores
    private List<String> fotosUrls; // Lista de URLs para las fotos

    
    public ProductoNovedadEvent() {
		super();
	}

	// Constructor completo para inicializar rápidamente el evento
    public ProductoNovedadEvent(Long codigoProducto, String nombre, Map<String, List<String>> tallesYColores, List<String> fotosUrls) {
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.tallesYColores = tallesYColores;
        this.fotosUrls = fotosUrls;
    }

    // Getters y Setters
    public Long getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Long codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Map<String, List<String>> getTallesYColores() {
        return tallesYColores;
    }

    public void setTallesYColores(Map<String, List<String>> tallesYColores) {
        this.tallesYColores = tallesYColores;
    }

    public List<String> getFotosUrls() {
        return fotosUrls;
    }

    public void setFotosUrls(List<String> fotosUrls) {
        this.fotosUrls = fotosUrls;
    }

    // Método toString para facilitar la depuración y los logs
    @Override
    public String toString() {
        return "ProductoNovedadEvent{" +
                "codigoProducto=" + codigoProducto +
                ", nombre='" + nombre + '\'' +
                ", tallesYColores=" + tallesYColores +
                ", fotosUrls=" + fotosUrls +
                '}';
    }
}

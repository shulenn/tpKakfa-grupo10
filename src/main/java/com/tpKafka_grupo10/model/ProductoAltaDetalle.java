package com.tpKafka_grupo10.model;

import java.util.List;
import java.util.Map;

public class ProductoAltaDetalle {
    private Long codigoProducto;
    private Map<String, List<String>> tallesYColoresSeleccionados; // Talles y colores seleccionados por el usuario

    // Constructor
    public ProductoAltaDetalle(Long codigoProducto, Map<String, List<String>> tallesYColoresSeleccionados) {
        this.codigoProducto = codigoProducto;
        this.tallesYColoresSeleccionados = tallesYColoresSeleccionados;
    }

    // Getters y Setters
    public Long getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Long codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public Map<String, List<String>> getTallesYColoresSeleccionados() {
        return tallesYColoresSeleccionados;
    }

    public void setTallesYColoresSeleccionados(Map<String, List<String>> tallesYColoresSeleccionados) {
        this.tallesYColoresSeleccionados = tallesYColoresSeleccionados;
    }
}

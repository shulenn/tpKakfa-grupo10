package com.tpKafka_grupo10.event;

import java.util.List;
import java.util.Map;

public class ProductoNovedadEvent {
    private Long codigoProducto;
    private Map<String, List<String>> tallesYColores; // Mapa de talles con sus respectivos colores
    private List<String> fotosUrls; // Lista de URLs para las fotos

    
    public Long getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Long codigoProducto) {
        this.codigoProducto = codigoProducto;
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
}

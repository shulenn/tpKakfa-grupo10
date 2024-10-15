package com.tpKafka_grupo10.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Proveedor {

    // Simulación de productos que el proveedor maneja y su stock
    private Map<String, Integer> stockProductos = new HashMap<>();

    public Proveedor() {
        // Inicializar algunos productos con su stock (simulación)
        stockProductos.put("1", 10);  // Producto con código PROD001 tiene 10 en stock
        stockProductos.put("2", 5);   // Producto con código PROD002 tiene 5 en stock
        stockProductos.put("3", 0);   // Producto con código PROD003 está agotado
    }

    // Método para procesar una orden de compra
    public void procesarOrdenCompra(OrdenCompra ordenCompra) {
        List<ItemOrdenDeCompra> items = ordenCompra.getItemsOrdenCompra();
        boolean tieneErrores = false;
        StringBuilder observaciones = new StringBuilder();

        // Verificar cada ítem de la orden
        for (ItemOrdenDeCompra item : items) {
            // Verificar si el artículo existe y la cantidad es válida
            if (!proveeArticulo(item.getProducto()) || item.getCantidad() < 1) {
                tieneErrores = true;
                observaciones.append("Artículo ").append(item.getProducto().getCodigo())
                             .append(": no existe o cantidad mal informada. ");
            } else if (!hayStockSuficiente(item.getProducto(), item.getCantidad())) {
                // Si no hay stock suficiente, agregar la observación
                observaciones.append("Artículo ").append(item.getProducto().getCodigo())
                             .append(": faltante de stock. ");
            }
        }

        if (tieneErrores) {
            // Marcar como RECHAZADA y enviar notificación al topic
            ordenCompra.setEstado(EstadoOrden.RECHAZADA);
            enviarNotificacion(ordenCompra, observaciones.toString(), "solicitudes");
        } else {
            // Verificar si se puede cumplir con todo el pedido
            if (ordenCompraCompleta(items)) {
                ordenCompra.setEstado(EstadoOrden.ACEPTADA);
                generarOrdenDespacho(ordenCompra);
            } else {
                ordenCompra.setEstado(EstadoOrden.ACEPTADA);
                enviarNotificacion(ordenCompra, observaciones.toString(), "solicitudes");
            }
        }
    }

    // Método que verifica si el proveedor maneja el producto
    private boolean proveeArticulo(Producto producto) {
        return stockProductos.containsKey(producto.getCodigo());
    }

    // Método que verifica si hay stock suficiente para el producto
    private boolean hayStockSuficiente(Producto producto, int cantidad) {
        Integer stockDisponible = stockProductos.get(producto.getCodigo());
        return stockDisponible != null && stockDisponible >= cantidad;
    }

    // Método que verifica si todos los ítems de la orden pueden ser cumplidos
    private boolean ordenCompraCompleta(List<ItemOrdenDeCompra> items) {
        for (ItemOrdenDeCompra item : items) {
            if (!hayStockSuficiente(item.getProducto(), item.getCantidad())) {
                return false;
            }
        }
        return true;
    }

    // Generar una orden de despacho si se puede cumplir con toda la orden
    private void generarOrdenDespacho(OrdenCompra ordenCompra) {
        // Generar ID de la orden de despacho
        String idOrdenDespacho = "DESP_" + ordenCompra.getId();
        
        // Simulación de restar del stock del proveedor
        for (ItemOrdenDeCompra item : ordenCompra.getItemsOrdenCompra()) {
            restarStock(item.getProducto(), item.getCantidad());
        }
        
        // Enviar al topic de despacho
        String mensajeDespacho = "Orden de despacho ID: " + idOrdenDespacho 
                                + ", Orden de compra ID: " + ordenCompra.getId() 
                                + ", Fecha estimada de envío: " + LocalDate.now().plusDays(3);
        
        enviarNotificacion(ordenCompra, mensajeDespacho, "despacho");
    }

    // Método que resta el stock del producto una vez que se genera una orden de despacho
    private void restarStock(Producto producto, int cantidad) {
        String codigoProducto = producto.getCodigo().toString();
        if (stockProductos.containsKey(codigoProducto)) {
            int stockActual = stockProductos.get(codigoProducto);
            stockProductos.put(codigoProducto, stockActual - cantidad);
        }
    }

    // Simulación de envío de mensajes a Kafka
    private void enviarNotificacion(OrdenCompra ordenCompra, String mensaje, String topic) {
        // Aquí envías el mensaje a Kafka
        String topicName = "/" + ordenCompra.getTiendaId() + "/" + topic;
        System.out.println("Enviando mensaje a Kafka Topic: " + topicName);
        System.out.println("Mensaje: " + mensaje);
        // Lógica real para enviar el mensaje al topic de Kafka iría aquí
    }
}


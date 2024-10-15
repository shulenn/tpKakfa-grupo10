package com.tpKafka_grupo10.service;

import com.tpKafka_grupo10.model.ItemDespacho;
import com.tpKafka_grupo10.model.Producto;
import com.tpKafka_grupo10.repository.ItemDespachoRepository; // Asegúrate de tener un repositorio
import com.tpKafka_grupo10.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemDespachoService {

    @Autowired
    private ItemDespachoRepository itemDespachoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    // Crear un nuevo ítem de despacho
    public ItemDespacho crearItemDespacho(ItemDespacho itemDespacho) {
    	// Validaciones
        if (itemDespacho.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        // Verificar que el producto existe
        Producto productoExistente = productoRepository.findById(itemDespacho.getProducto().getCodigo()).orElse(null);
        if (productoExistente == null) {
            throw new IllegalArgumentException("El producto no existe.");
        }

        // Asignar el producto al ItemDespacho
        itemDespacho.setProducto(productoExistente);
        
        // Guardar el ItemDespacho
        return itemDespachoRepository.save(itemDespacho);
    }

    // Obtener un ítem de despacho por ID
    public ItemDespacho obtenerItemDespacho(Long id) {
        return itemDespachoRepository.findById(id).orElse(null);
    }

    // Listar todos los ítems de despacho
    public List<ItemDespacho> listarItemsDespacho() {
        return itemDespachoRepository.findAll();
    }
}


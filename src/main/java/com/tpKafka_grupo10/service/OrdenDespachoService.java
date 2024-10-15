package com.tpKafka_grupo10.service;

import com.tpKafka_grupo10.model.OrdenDespacho;
import com.tpKafka_grupo10.repository.OrdenDespachoRepository; // Asegúrate de tener un repositorio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenDespachoService {

    @Autowired
    private OrdenDespachoRepository ordenDespachoRepository;

    public OrdenDespacho crearOrdenDespacho(OrdenDespacho ordenDespacho) {
        return ordenDespachoRepository.save(ordenDespacho);
    }

    public OrdenDespacho obtenerOrdenDespacho(Long id) {
        return ordenDespachoRepository.findById(id).orElse(null);
    }

    public List<OrdenDespacho> listarOrdenesDespacho() {
        return ordenDespachoRepository.findAll();
    }
}

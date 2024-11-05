package com.tpKafka_grupo10.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

@Controller
public class WebController {
    
	@GetMapping("/home/tienda")
    public String homeTienda() {
        return "home_tienda"; 
    }
	
	@GetMapping("/home/proveedor")
    public String homeProveedor() {
        return "home_proveedor"; 
    }
	
    @GetMapping("/orden-compra/crear")
    public String mostrarFormularioOrdenCompra() {
        return "crear_oc"; 
    }
  
    @GetMapping("/stock/actualizar")
    public String mostrarFormularioActualizarStock() { 
        return "actualizarStock";  
    }
}

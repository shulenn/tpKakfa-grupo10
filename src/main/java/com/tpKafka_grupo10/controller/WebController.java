package com.tpKafka_grupo10.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    
    @GetMapping("/orden-despacho/crear")
    public String crearOrdenDespacho() {
        return "crear_od"; 
    }
}

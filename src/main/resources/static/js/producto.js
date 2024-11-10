document.getElementById("crearProductoForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const producto = {
        nombre: document.getElementById("nombre").value,
        talle: document.getElementById("talle").value,
        foto: document.getElementById("foto").value,
        color: document.getElementById("color").value
    };

    fetch("http://localhost:8080/productos/crear", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(producto)
    })
    .then(response => {
        const mensajeDiv = document.getElementById("mensaje");

        if (response.status === 201) {
        	alert("Producto creado exitosamente.");
            window.location.href = "/home/proveedor"; 
        } else {
            mensajeDiv.innerHTML = "Error al crear el producto.";
            mensajeDiv.style.color = "red";  
        }
    })
    .catch(error => {
        console.error("Error:", error);
        document.getElementById("mensaje").innerHTML = "Error de conexión.";
        document.getElementById("mensaje").style.color = "red";
    });
});

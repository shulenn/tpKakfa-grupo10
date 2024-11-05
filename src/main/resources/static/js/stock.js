function actualizarStock() {
    const productoId = document.getElementById("productoId").value; 
    const nuevaCantidad = document.getElementById("nuevaCantidad").value;  

    // Validar que ambos campos esten completos
    if (!productoId || !nuevaCantidad) {
        alert("Por favor, ingrese ambos campos: Producto ID y Nueva Cantidad.");
        return;
    }

    // Crear la URL con los parámetros en la URL
    const url = `http://localhost:8080/stock/actualizar/${productoId}?nuevaCantidad=${nuevaCantidad}`;

    // Enviar la solicitud HTTP con el método POST
    fetch(url, {
        method: "POST",
    })
    .then(response => {
        if (response.ok) {
            alert("Stock actualizado con éxito.");
            window.location.href = "/home/proveedor";
        } else {
            return response.text().then(text => { throw new Error(text) });
        }
    })
    .catch(error => {
        alert("Error al actualizar el stock: " + error.message);
    });
}

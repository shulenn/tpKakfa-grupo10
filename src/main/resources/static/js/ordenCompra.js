// Agregar un nuevo item al formulario
function agregarItem() {
    const itemsContainer = document.getElementById("itemsContainer");
    const itemDiv = document.createElement("div");

    itemDiv.classList.add("mb-3");

    itemDiv.innerHTML = `
        <label for="productoCodigo" class="form-label">Producto Código:</label>
        <input type="number" class="form-control" name="productoCodigo" required>
        
        <label for="color" class="form-label">Color:</label>
        <input type="text" class="form-control" name="color" required>
        
        <label for="talle" class="form-label">Talle:</label>
        <input type="text" class="form-control" name="talle" required>
        
        <label for="cantidad" class="form-label">Cantidad:</label>
        <input type="number" class="form-control" name="cantidad" required>
        
        <button type="button" class="btn btn-danger mt-2" onclick="eliminarItem(this)">Eliminar</button>
    `;

    itemsContainer.appendChild(itemDiv);
}

// Eliminar un producto del formulario
function eliminarItem(button) {
    const itemDiv = button.parentElement;
    itemDiv.remove();
}

// Manejar la creación de la orden de compra
document.getElementById("ordenCompraForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const tiendaId = parseInt(document.getElementById("tiendaId").value);

    const items = Array.from(document.getElementById("itemsContainer").children).map(itemDiv => {
        return {

            color: itemDiv.querySelector("input[name='color']").value,
            talle: itemDiv.querySelector("input[name='talle']").value,
            cantidad: parseInt(itemDiv.querySelector("input[name='cantidad']").value),
            producto: {
                codigo: parseInt(itemDiv.querySelector("input[name='productoCodigo']").value)
            }
        };
    });

    const ordenCompra = {
        tiendaId,
        itemsOrdenCompra: items
    };

    try {
        // Enviar la solicitud POST
        const response = await axios.post("http://localhost:8080/ordenes-compra", ordenCompra);
        
        alert("Orden de compra creada con éxito");
        
        window.location.href = "/home/tienda";  // Redirige al usuario a la página de inicio de tienda
  
    } catch (error) {
        alert("Error al crear la orden de compra: " + (error.response ? error.response.data : error.message));
    }
});

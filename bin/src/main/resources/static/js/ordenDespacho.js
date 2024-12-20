function agregarProducto() {
    console.log("Agregando un nuevo producto...");
    const itemsContainer = document.getElementById("itemsContainer");
    const itemDiv = document.createElement("div");
    
    itemDiv.innerHTML = `
        <label>Producto ID:</label>
        <input type="number" name="productoId" required>
        <label>Cantidad:</label>
        <input type="number" name="cantidad" required>
        <button type="button" onclick="eliminarProducto(this)">Eliminar</button>
        <br><br>
    `;
    
    itemsContainer.appendChild(itemDiv);
    console.log("Producto agregado:", itemDiv);
}

// Eliminar un producto del formulario
function eliminarProducto(button) {
    const itemDiv = button.parentElement;
    itemDiv.remove();
}

//Manejar la creación de la orden de despacho
document.getElementById("ordenDespachoForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const idOrdenCompra = parseInt(document.getElementById("idOrdenCompra").value);
    const fechaEstimadaEnvio = document.getElementById("fechaEstimadaEnvio").value;

    const itemDivs = Array.from(document.getElementById("itemsContainer").children).map(itemDiv => {
        console.log("itemDiv:", itemDiv); 
        const productoIdInput = itemDiv.querySelector("input[name='productoId']");
        const cantidadInput = itemDiv.querySelector("input[name='cantidad']");
        
        if (productoIdInput && cantidadInput) {
            return {
                producto: {
                    id: parseInt(productoIdInput.value)
                },
                cantidad: parseInt(cantidadInput.value)
            };
        } else {
            console.error("Producto ID input or cantidad input is missing in itemDiv:", itemDiv);
            return null;
        }
    }).filter(item => item !== null);

    
    const ordenDespacho = {
        idOrdenCompra: idOrdenCompra,
        fechaEstimadaEnvio,
        productos: itemDivs 
    };

    try {
        const response = await axios.post("http://localhost:8080/ordenes-despacho", ordenDespacho);
        alert("Orden de despacho creada con éxito: " + JSON.stringify(response.data));
    }catch (error) {
        console.error("Error:", error); 
        alert("Error al crear la orden de despacho: " + (error.response ? error.response.data : error.message));
    }

});


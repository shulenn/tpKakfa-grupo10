// Agregar un nuevo item al formulario
function agregarItem() {
    const itemsContainer = document.getElementById("itemsContainer");
    const itemDiv = document.createElement("div");
    
    itemDiv.innerHTML = `
        <label>Producto ID:</label>
        <input type="number" name="productoId" required>
        <label>Cantidad:</label>
        <input type="number" name="cantidad" required>
        <br><br>
    `;
    
    itemsContainer.appendChild(itemDiv);
}

// Manejar la creación de la orden de compra
document.getElementById("ordenCompraForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const tiendaId = document.getElementById("tiendaId").value;
    const observaciones = document.getElementById("observaciones").value;
    const ordenDespacho = document.getElementById("ordenDespacho").value;
    const fechaRecepcion = document.getElementById("fechaRecepcion").value;

    const items = Array.from(document.getElementById("itemsContainer").children).map(itemDiv => {
        return {
            productoId: itemDiv.querySelector("input[name='productoId']").value,
            cantidad: itemDiv.querySelector("input[name='cantidad']").value
        };
    });

    const ordenCompra = {
        tiendaId: parseInt(tiendaId),
        observaciones,
        ordenDespacho,
        fechaRecepcion,
        itemsOrdenCompra: items,
        pausada: false
    };

    try {
        const response = await axios.post("http://localhost:8080/ordenes-compra", ordenCompra);
        alert("Orden de compra creada con éxito: " + JSON.stringify(response.data));
    } catch (error) {
        alert("Error al crear la orden de compra: " + error.response.data);
    }
});

let html5QrCode = null;
let cameraRunning = false;

const modal = document.getElementById("modalCrearProducto");

modal.addEventListener("shown.bs.modal", () => {
  if (cameraRunning) return;

  html5QrCode = new Html5Qrcode("reader");

  Html5Qrcode.getCameras().then((cameras) => {
    if (!cameras || cameras.length === 0) {
      alert("No se encontró cámara");
      return;
    }

    html5QrCode.start(
      { facingMode: "environment" },
      { fps: 10, qrbox: 250 },
      onScanSuccess,
    );

    cameraRunning = true;
  });
});

modal.addEventListener("hidden.bs.modal", () => {
  if (html5QrCode && cameraRunning) {
    html5QrCode
      .stop()
      .then(() => {
        cameraRunning = false;
        html5QrCode.clear();
      })
      .catch((err) => console.log("Error al detener", err));
  }

  // limpiar UI
  document.getElementById("txtCodigo").value = "";
  document.getElementById("txtNombre").value = "";
  document.getElementById("txtUnidades").value = "";
  document.getElementById("formDetalles").classList.add("d-none");
});

function onScanSuccess(decodedText) {
  document.getElementById("txtCodigo").value = decodedText;
  document.getElementById("formDetalles").classList.remove("d-none");

  // detener cámara después de escanear
  html5QrCode.stop().then(() => {
    cameraRunning = false;
  });
}

const btnGuardar = document.getElementById("btnGuardar");
btnGuardar.addEventListener("click", () => {
  const codigo = document.getElementById("txtCodigo").value;
  const nombre = document.getElementById("txtNombre").value;
  const unidades = document.getElementById("txtUnidades").value;

  if (codigo.trim() === "" || nombre.trim() === "") {
    alert("Por favor, rellena los campos obligatorios");
    return;
  }

  const productoEnviar = {
    codigoBarra: codigo,
    nombre: nombre,
    unidadesPorCaja: parseInt(unidades) || 1,
  };

  console.log("Listo para enviar:", productoEnviar);

  const opciones = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(productoEnviar),
  };

  fetch("/api/productos", opciones)
    .then((response) => {
      if (response.ok) {
        return response.json();
      } else {
        return response.json().then((err) => {
          throw new Error(err.mensaje || "Error al guardar");
        });
      }
    })
    .then((productoGuardado) => {
      alert("!Producto guardado!");

      const modalElement = document.getElementById("modalCrearProducto");
      const modalInstance = bootstrap.Modal.getInstance(modalElement);
      modalInstance.hide();
    })
    .catch((error) => {
      console.error(error);
      alert("Hubo un error: " + error.message);
    });
});

function cargarProductos() {
  console.log("Iniciando petición fetch a /api/productos...");

  fetch("/api/productos")
    .then((response) => {
      console.log("Respuesta del servidor:", response.status);
      if (!response.ok) {
        throw new Error(
          "Error en la respuesta del servidor: " + response.statusText,
        );
      }
      return response.json();
    })
    .then((productos) => {
      console.log("Productos recibidos:", productos);

      const contenedor = document.getElementById("contenedorProductos");

      // Verificación de seguridad
      if (!contenedor) {
        console.error(
          "¡ERROR CRÍTICO! No encontré el div con id='contenedorProductos'",
        );
        return;
      }

      // Si la lista está vacía
      if (productos.length === 0) {
        contenedor.innerHTML =
          '<p class="text-center text-muted">No hay productos registrados.</p>';
        return;
      }

      // Limpiamos la tarjeta de ejemplo
      contenedor.innerHTML = "";

      // Dibujamos las tarjetas reales
      productos.forEach((producto) => {
        const cardHTML = `
                    <div class="col-12 col-md-6 col-lg-4">
                        <div class="card product-card h-100 shadow-sm">
                            <div class="card-body">
                                <h6 class="card-title mb-1 text-truncate" title="${producto.nombre}">
                                    ${producto.nombre}
                                </h6>
                                <small class="text-muted d-block">
                                    Código: <strong>${producto.codigoBarra}</strong>
                                </small>
                                <small class="text-muted">
                                    Pack: ${producto.unidadesPorCaja || "N/A"} unidades
                                </small>
                            </div>
                        </div>
                    </div>
                `;
        contenedor.innerHTML += cardHTML;
      });
      console.log("Tarjetas dibujadas con éxito.");
    })
    .catch((error) => console.error("Error al cargar productos:", error));
}

document.addEventListener('DOMContentLoaded', cargarProductos);
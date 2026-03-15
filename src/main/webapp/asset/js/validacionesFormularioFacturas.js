const formulario = document.getElementById('formRegistroFactura');
const botonGuardar = document.getElementById('btnGuardarFactura');

    formulario.addEventListener('submit', function() {
        // Deshabilitar el botón apenas se hace clic en enviar
        botonGuardar.disabled = true;
        botonGuardar.innerText = "Guardando...";
        botonGuardar.style.opacity = "0.5";
        botonGuardar.style.cursor = "not-allowed";
    });
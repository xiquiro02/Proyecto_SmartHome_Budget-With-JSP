document.addEventListener('DOMContentLoaded', function () {

    const formulario   = document.getElementById('formRegistroFactura');
    const botonGuardar = document.getElementById('btnGuardarFactura');
    const inputFecha   = document.getElementById('fechaVencimiento');

    if (!formulario || !botonGuardar) return;
    
    // Configurar el límite visual en el calendario ---
    if (inputFecha) {
        const hoy = new Date();
        // Restamos 3 meses a la fecha de hoy
        hoy.setMonth(hoy.getMonth() - 3);
        // Formateamos a YYYY-MM-DD para que el atributo 'min' lo entienda
        const fechaMinimaStr = hoy.toISOString().split('T')[0];
        inputFecha.min = fechaMinimaStr;
    }

    const textareaDesc = document.getElementById('descripcion');
    const contador = document.getElementById('contadorCaracteres');

    if (textareaDesc && contador) {
        textareaDesc.addEventListener('input', function() {
        const longitud = textareaDesc.value.length;
        contador.textContent = `${longitud} / 500 caracteres`;

        // Cambio de color si se acerca al límite
        if (longitud >= 450) {
            contador.style.color = "red";
        } else {
            contador.style.color = "#666";
        }
        });
    }

    formulario.addEventListener('submit', function (e) {

        // ── Validación de campos obligatorios ─────────────────────────────────
        const nombreFactura = document.getElementById('nombreFactura')?.value?.trim() || "";
        const monto         = document.getElementById('monto')?.value?.trim() || "";
        const categoria     = document.getElementById('idCategoriaEgreso')?.value || "";
        const metodo        = document.getElementById('idMetodoPago')?.value || "";
        const fechaVenc     = document.getElementById('fechaVencimiento')?.value || "";
        const estadoPago    = document.querySelector('[name="estadoPago"]:checked')?.value ||
                              document.getElementById('estadoPago')?.value || "";

        if (!nombreFactura || !monto || !categoria || !metodo || !fechaVenc) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor completa todos los campos obligatorios antes de guardar.',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#1E88E5'
            });
            return false;
        }
        
        // Expresión regular: Letras, números, espacios y los signos ( . , # - _ )
        const regexNombre = /^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\s.,#\-_]+$/;

        if (nombreFactura.length < 3) {
            e.preventDefault();
            Swal.fire({ 
                icon: 'warning', 
                title: 'Nombre muy corto', 
                text: 'El nombre debe tener al menos 3 letras o números.' 
            });
            return false;
        }
        
        if (!regexNombre.test(nombreFactura)) {
            e.preventDefault();
            Swal.fire({ 
                icon: 'error', 
                title: 'Caracteres no permitidos', 
                text: 'El nombre solo permite letras, números y los signos: punto (.), coma (,), numeral (#), guion (-) y guion bajo (_).',
                confirmButtonText: 'Corregir'
            });
            return false;
        }

        // ── Validar monto positivo ────────────────────────────────────────────
        const montoNum = parseFloat(monto.replace(',', '.'));
        if (isNaN(montoNum) || montoNum <= 0) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Monto inválido',
                text: 'El monto debe ser un número positivo mayor a cero.',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#1E88E5'
            });
            return false;
        }
        
        // Validación de Fecha (Máximo 3 meses de antigüedad) ---
        // Usamos "T00:00:00" para asegurar que compare la fecha en horario local
        const fechaSeleccionada = new Date(fechaVenc + "T00:00:00");
        const limiteAntiguedad = new Date();
        limiteAntiguedad.setMonth(limiteAntiguedad.getMonth() - 3);
        limiteAntiguedad.setHours(0, 0, 0, 0); // Limpiamos horas para comparar solo días

        if (fechaSeleccionada < limiteAntiguedad) {
            e.preventDefault();
            Swal.fire({ 
                icon: 'error', 
                title: 'Fecha fuera de rango', 
                text: 'No puedes registrar facturas con más de 3 meses de antigüedad.' 
            });
            return false;
        }
        
        // --- Validación de Notas/Descripción (Longitud) ---
        const descripcion = document.getElementById('descripcion')?.value || "";

        if (descripcion.length > 500) {
            e.preventDefault();
            Swal.fire({ 
                icon: 'error', 
                title: 'Nota demasiado larga', 
                text: 'Las notas adicionales no pueden superar los 500 caracteres.' 
            });
            return false;
        }
        
        // ── Deshabilitar botón (anti-doble-clic) ──────────────────────────────
        botonGuardar.disabled    = true;
        botonGuardar.innerText   = "Guardando...";
        botonGuardar.style.opacity  = "0.6";
        botonGuardar.style.cursor   = "not-allowed";

        return true;
    });
});

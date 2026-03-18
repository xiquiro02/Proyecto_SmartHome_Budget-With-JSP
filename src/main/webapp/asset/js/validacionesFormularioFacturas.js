document.addEventListener('DOMContentLoaded', function () {

    const formulario   = document.getElementById('formRegistroFactura');
    const botonGuardar = document.getElementById('btnGuardarFactura');
    const inputFecha   = document.getElementById('fechaVencimiento');

    if (!formulario || !botonGuardar) return;

    // ── Configurar límite visual del calendario (mínimo: hace 3 meses) ───────
    if (inputFecha) {
        const hoy = new Date();
        hoy.setMonth(hoy.getMonth() - 3);
        inputFecha.min = hoy.toISOString().split('T')[0];
    }

    // ── Contador de caracteres para el textarea descripcion ──────────────────
    const textareaDesc = document.getElementById('descripcion');
    const contador     = document.getElementById('contadorCaracteres');

    if (textareaDesc && contador) {
        textareaDesc.addEventListener('input', function () {
            const longitud = textareaDesc.value.length;
            contador.textContent = `${longitud} / 500 caracteres`;
            contador.style.color = longitud >= 450 ? 'red' : '#666';
        });
    }

    // ── Validación al enviar el formulario ────────────────────────────────────
    formulario.addEventListener('submit', function (e) {

        // Campos obligatorios (descripcion NO es obligatoria)
        const monto      = document.getElementById('monto')?.value?.trim() || '';
        const categoria  = document.getElementById('idCategoriaEgreso')?.value || '';
        const metodo     = document.getElementById('idMetodoPago')?.value || '';
        const fechaVenc  = document.getElementById('fechaVencimiento')?.value || '';

        if (!monto || !categoria || !metodo || !fechaVenc) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor completa categoría, monto, método de pago y fecha de vencimiento.',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#1E88E5'
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

        // ── Validar que la fecha no tenga más de 3 meses de antigüedad ───────
        const fechaSeleccionada = new Date(fechaVenc + 'T00:00:00');
        const limiteAntiguedad  = new Date();
        limiteAntiguedad.setMonth(limiteAntiguedad.getMonth() - 3);
        limiteAntiguedad.setHours(0, 0, 0, 0);

        if (fechaSeleccionada < limiteAntiguedad) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Fecha fuera de rango',
                text: 'No puedes registrar facturas con más de 3 meses de antigüedad.'
            });
            return false;
        }

        // ── Validar longitud máxima de descripcion (opcional, pero no ilimitada) ──
        const descripcion = document.getElementById('descripcion')?.value || '';
        if (descripcion.length > 500) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Descripción demasiado larga',
                text: 'La descripción no puede superar los 500 caracteres.'
            });
            return false;
        }

        // ── Anti-doble-clic: deshabilitar botón al enviar ─────────────────────
        botonGuardar.disabled          = true;
        botonGuardar.innerText         = 'Guardando...';
        botonGuardar.style.opacity     = '0.6';
        botonGuardar.style.cursor      = 'not-allowed';

        return true;
    });
});

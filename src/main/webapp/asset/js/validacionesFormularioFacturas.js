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

    // ── Contador de caracteres ────────────────────────────────────────────────
    const textareaDesc = document.getElementById('descripcion');
    const contador     = document.getElementById('contadorCaracteres');

    if (textareaDesc && contador) {
        textareaDesc.addEventListener('input', function () {
            const longitud = textareaDesc.value.length;
            contador.textContent = `${longitud} / 500 caracteres`;
            contador.style.color = longitud >= 450 ? 'red' : '#666';
        });
    }

    // ── Utilidades de validación inline ──────────────────────────────────────
    function mostrarError(idCampo, idSpan, mensaje) {
        const campo = document.getElementById(idCampo);
        const span  = document.getElementById(idSpan);
        if (!campo || !span) return;
        campo.classList.add('input-error');
        campo.classList.remove('input-valido');
        span.textContent = mensaje;
        span.classList.add('visible');
    }

    function marcarValido(idCampo, idSpan) {
        const campo = document.getElementById(idCampo);
        const span  = document.getElementById(idSpan);
        if (!campo || !span) return;
        campo.classList.remove('input-error');
        campo.classList.add('input-valido');
        span.textContent = '';
        span.classList.remove('visible');
    }

    function limpiarEstado(idCampo, idSpan) {
        const campo = document.getElementById(idCampo);
        const span  = document.getElementById(idSpan);
        if (!campo || !span) return;
        campo.classList.remove('input-error', 'input-valido');
        span.textContent = '';
        span.classList.remove('visible');
    }

    // ── Validadores por campo ─────────────────────────────────────────────────
    function validarCategoria() {
        const val = document.getElementById('idCategoriaEgreso')?.value || '';
        if (!val) {
            mostrarError('idCategoriaEgreso', 'error-categoria', 'Selecciona una categoría.');
            return false;
        }
        marcarValido('idCategoriaEgreso', 'error-categoria');
        return true;
    }

    function validarMonto() {
        const raw = document.getElementById('monto')?.value?.trim() || '';
        if (!raw) {
            mostrarError('monto', 'error-monto', 'El monto es obligatorio.');
            return false;
        }
        const num = parseFloat(raw.replace(',', '.'));
        if (isNaN(num) || num <= 0) {
            mostrarError('monto', 'error-monto', 'Ingresa un monto positivo mayor a cero.');
            return false;
        }
        marcarValido('monto', 'error-monto');
        return true;
    }

    function validarFecha() {
        const val = document.getElementById('fechaVencimiento')?.value || '';
        if (!val) {
            mostrarError('fechaVencimiento', 'error-fecha', 'La fecha de vencimiento es obligatoria.');
            return false;
        }
        const fecha   = new Date(val + 'T00:00:00');
        const limite  = new Date();
        limite.setMonth(limite.getMonth() - 3);
        limite.setHours(0, 0, 0, 0);
        if (fecha < limite) {
            mostrarError('fechaVencimiento', 'error-fecha', 'La fecha no puede tener más de 3 meses de antigüedad.');
            return false;
        }
        marcarValido('fechaVencimiento', 'error-fecha');
        return true;
    }

    function validarMetodo() {
        const val = document.getElementById('idMetodoPago')?.value || '';
        if (!val) {
            mostrarError('idMetodoPago', 'error-metodo', 'Selecciona un método de pago.');
            return false;
        }
        marcarValido('idMetodoPago', 'error-metodo');
        return true;
    }

    // ── Eventos blur: validar al salir del campo ──────────────────────────────
    document.getElementById('idCategoriaEgreso')?.addEventListener('blur', validarCategoria);
    document.getElementById('monto')?.addEventListener('blur', validarMonto);
    document.getElementById('fechaVencimiento')?.addEventListener('blur', validarFecha);
    document.getElementById('idMetodoPago')?.addEventListener('blur', validarMetodo);

    // ── Eventos change/input: limpiar error cuando el usuario corrige ─────────
    document.getElementById('idCategoriaEgreso')?.addEventListener('change', function () {
        if (document.getElementById('error-categoria')?.classList.contains('visible')) validarCategoria();
    });
    document.getElementById('monto')?.addEventListener('input', function () {
        if (document.getElementById('error-monto')?.classList.contains('visible')) validarMonto();
    });
    document.getElementById('fechaVencimiento')?.addEventListener('change', function () {
        if (document.getElementById('error-fecha')?.classList.contains('visible')) validarFecha();
    });
    document.getElementById('idMetodoPago')?.addEventListener('change', function () {
        if (document.getElementById('error-metodo')?.classList.contains('visible')) validarMetodo();
    });

    // ── Validación al enviar ──────────────────────────────────────────────────
    formulario.addEventListener('submit', function (e) {
        const okCategoria = validarCategoria();
        const okMonto     = validarMonto();
        const okFecha     = validarFecha();
        const okMetodo    = validarMetodo();

        // Validar descripción si supera el máximo (campo opcional)
        const desc = document.getElementById('descripcion')?.value || '';
        if (desc.length > 500) {
            e.preventDefault();
            mostrarError('descripcion', 'error-categoria', 'La descripción no puede superar los 500 caracteres.');
            return false;
        }

        if (!okCategoria || !okMonto || !okFecha || !okMetodo) {
            e.preventDefault();
            // Enfocar el primer campo con error
            for (const id of ['idCategoriaEgreso', 'monto', 'fechaVencimiento', 'idMetodoPago']) {
                const el = document.getElementById(id);
                if (el?.classList.contains('input-error')) { el.focus(); break; }
            }
            return false;
        }

        // Anti-doble-clic
        botonGuardar.disabled      = true;
        botonGuardar.innerText     = 'Guardando...';
        botonGuardar.style.opacity = '0.6';
        botonGuardar.style.cursor  = 'not-allowed';
        return true;
    });
});

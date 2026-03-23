// ============================================================
//  SmartHome Budget — Sistema de Notificaciones In-App
// ============================================================

// ── 1. CORE: consultar preferencia ──────────────────────────
window.NotifActiva = function(clave) {
    return localStorage.getItem(clave) !== 'off';
};

// ── 2. TOAST ─────────────────────────────────────────────────
function _contenedorToast() {
    var c = document.getElementById('shb-toast-container');
    if (!c) {
        c = document.createElement('div');
        c.id = 'shb-toast-container';
        document.body.appendChild(c);
    }
    return c;
}

window.mostrarToast = function(mensaje, tipo, duracion) {
    tipo     = tipo     || 'info';
    duracion = duracion || 5000;
    var iconos = { success: '✓', error: '!', warning: '⚠', info: 'i' };

    var toast = document.createElement('div');
    toast.className = 'shb-toast shb-toast--' + tipo;

    var icono = document.createElement('span');
    icono.className = 'shb-toast__icono';
    icono.textContent = iconos[tipo] || 'i';

    var msg = document.createElement('span');
    msg.className = 'shb-toast__mensaje';
    msg.innerHTML = mensaje;

    var btn = document.createElement('button');
    btn.className = 'shb-toast__cerrar';
    btn.textContent = '✕';
    btn.onclick = function() {
        toast.classList.remove('shb-toast--visible');
        setTimeout(function() { if (toast.parentElement) toast.remove(); }, 350);
    };

    toast.appendChild(icono);
    toast.appendChild(msg);
    toast.appendChild(btn);
    _contenedorToast().appendChild(toast);

    setTimeout(function() { toast.classList.add('shb-toast--visible'); }, 10);
    setTimeout(function() {
        toast.classList.remove('shb-toast--visible');
        setTimeout(function() { if (toast.parentElement) toast.remove(); }, 350);
    }, duracion);
};

// ── 3. BANNER ────────────────────────────────────────────────
window.mostrarBanner = function(mensaje, tipo) {
    tipo = tipo || 'warning';
    cerrarBanner();
    var iconos = { success: '✓', error: '!', warning: '⚠', info: 'i' };

    var banner = document.createElement('div');
    banner.id = 'shb-banner';
    banner.className = 'shb-banner shb-banner--' + tipo;

    var icono = document.createElement('span');
    icono.className = 'shb-banner__icono';
    icono.textContent = iconos[tipo] || '⚠';

    var msg = document.createElement('span');
    msg.className = 'shb-banner__mensaje';
    msg.innerHTML = mensaje;

    var btn = document.createElement('button');
    btn.className = 'shb-banner__cerrar';
    btn.textContent = '✕';
    btn.onclick = cerrarBanner;

    banner.appendChild(icono);
    banner.appendChild(msg);
    banner.appendChild(btn);

    var header = document.querySelector('header');
    if (header && header.nextSibling) {
        header.parentNode.insertBefore(banner, header.nextSibling);
    } else {
        document.body.insertBefore(banner, document.body.firstChild);
    }

    setTimeout(function() { banner.classList.add('shb-banner--visible'); }, 10);
};

window.cerrarBanner = function() {
    var b = document.getElementById('shb-banner');
    if (!b) return;
    b.classList.remove('shb-banner--visible');
    setTimeout(function() { if (b.parentElement) b.remove(); }, 350);
};

// ── 4. BADGE ─────────────────────────────────────────────────
window.actualizarBadge = function(selector, cantidad) {
    var el = document.querySelector(selector);
    if (!el) return;
    var badge = el.querySelector('.shb-badge');
    if (cantidad <= 0) {
        if (badge) badge.remove();
        return;
    }
    if (!badge) {
        badge = document.createElement('span');
        badge.className = 'shb-badge';
        el.style.position = 'relative';
        el.appendChild(badge);
    }
    badge.textContent = cantidad > 9 ? '9+' : cantidad;
};

// ── 5. LÓGICA DE NEGOCIO — 8 tipos de notificación ───────────
window.verificarNotificacionesMenu = function(datos) {
    var alertas = 0;

    function parsearFecha(str) {
        if (!str) return null;
        var p = str.split('-');
        if (p.length !== 3) return null;
        return new Date(parseInt(p[0]), parseInt(p[1]) - 1, parseInt(p[2]));
    }

    var hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    var vence = parsearFecha(datos.fechaVencimiento);

    // ── 1. Pagos atrasados → banner rojo ──────────────────────
    if (NotifActiva('notif_pagos_atrasados') && vence && vence < hoy) {
        mostrarBanner(
            'Pago atrasado: <strong>' + datos.nombrePago + '</strong> — Requiere atención',
            'error'
        );
        alertas++;
    }

    // ── 2. Recordatorios de pagos → toast naranja ─────────────
    if (NotifActiva('notif_recordatorios_pagos') && vence && vence >= hoy) {
        var dias = Math.round((vence - hoy) / 86400000);
        if (dias <= 3) {
            var txtPago = dias === 0
                ? 'Recordatorio: el pago <strong>' + datos.nombrePago + '</strong> vence <strong>hoy</strong>'
                : 'Recordatorio: el pago <strong>' + datos.nombrePago + '</strong> vence en <strong>' + dias + '</strong> día(s)';
            mostrarToast(txtPago, 'warning', 6000);
            alertas++;
        }
    }

    // ── 3. Confirmación de pagos → toast verde ────────────────
    if (NotifActiva('notif_confirmacion_pagos') && datos.pagoConfirmado) {
        mostrarToast(
            'Pago <strong>' + datos.pagoConfirmado + '</strong> registrado correctamente',
            'success', 5000
        );
        alertas++;
    }

    // ── 4. Lista de compras pendiente → toast azul ────────────
    if (NotifActiva('notif_lista_compras') && datos.listasConPendientes > 0) {
        mostrarToast(
            'Tienes <strong>' + datos.listasConPendientes + '</strong> lista(s) de compras con ítems pendientes',
            'info', 5000
        );
        alertas++;
    }

    // ── 5. Productos por agotarse → toast naranja ─────────────
    if (NotifActiva('notif_productos_agotarse') && datos.productosAgotados > 0) {
        mostrarToast(
            '<strong>' + datos.productosAgotados + '</strong> producto(s) con stock bajo en tu inventario',
            'warning', 5000
        );
        alertas++;
    }

    // ── 6. Alertas de presupuesto → toast naranja/rojo ────────
    if (NotifActiva('notif_presupuesto') && datos.porcentajePresupuesto > 0) {
        if (datos.porcentajePresupuesto >= 100) {
            mostrarToast(
                'Has <strong>superado</strong> tu presupuesto mensual (' + datos.porcentajePresupuesto + '% usado)',
                'error', 7000
            );
            alertas++;
        } else if (datos.porcentajePresupuesto >= 80) {
            mostrarToast(
                'Has usado el <strong>' + datos.porcentajePresupuesto + '%</strong> de tu presupuesto mensual',
                'warning', 6000
            );
            alertas++;
        }
    }

    // ── 7. Resumen semanal → toast azul (solo lunes, 1x/semana)
    if (NotifActiva('notif_resumen_semanal')) {
        var diaSemana    = hoy.getDay(); // 1 = lunes
        var claveUltimo  = 'shb_resumen_ultimo';
        var ultimoMs     = parseInt(localStorage.getItem(claveUltimo) || '0');
        var unaSemanaMs  = 7 * 24 * 60 * 60 * 1000;
        if (diaSemana === 1 && (hoy.getTime() - ultimoMs) >= unaSemanaMs) {
            var txtResumen = datos.totalGastadoMes > 0
                ? 'Resumen semanal: llevas <strong>$' + Number(datos.totalGastadoMes).toLocaleString('es-CO') + '</strong> gastados este mes'
                : 'Recuerda revisar tu resumen semanal de gastos';
            mostrarToast(txtResumen, 'info', 7000);
            localStorage.setItem(claveUltimo, hoy.getTime().toString());
            alertas++;
        }
    }

    // ── 8. Gasto inusual → banner naranja (>110% del presupuesto)
    if (NotifActiva('notif_gasto_inusual') && datos.porcentajePresupuesto >= 110) {
        mostrarBanner(
            'Gasto inusual: este mes llevas un <strong>' + datos.porcentajePresupuesto + '%</strong> del presupuesto — revisa tus egresos',
            'warning'
        );
        alertas++;
    }

    // Badge en la tarjeta Ajustes
    if (alertas > 0) {
        actualizarBadge('a[href*="ajustes"]', alertas);
    }
};

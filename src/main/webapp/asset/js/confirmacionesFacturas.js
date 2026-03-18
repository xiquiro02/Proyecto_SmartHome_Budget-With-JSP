document.addEventListener('DOMContentLoaded', function () {

    // ── Confirmar eliminación de factura ──────────────────────────────────────
    document.querySelectorAll('[data-confirmar-eliminar-factura]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const form   = this.closest('form');
            const nombre = this.dataset.nombreFactura || 'esta factura';
            Swal.fire({
                icon: 'warning',
                title: '¿Eliminar factura?',
                html: `¿Seguro que deseas eliminar <strong>"${nombre}"</strong>?<br><small>Esta acción no se puede revertir.</small>`,
                showCancelButton:    true,
                confirmButtonText:   'Sí, eliminar',
                cancelButtonText:    'Cancelar',
                confirmButtonColor:  '#E51E1E',
                cancelButtonColor:   '#9E9E9E'
            }).then(function (result) {
                if (result.isConfirmed && form) form.submit();
            });
        });
    });

    // ── Confirmar marcar como pagada ──────────────────────────────────────────
    document.querySelectorAll('[data-confirmar-pagada]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const form   = this.closest('form');
            const nombre = this.dataset.nombreFactura || 'esta factura';
            Swal.fire({
                icon: 'question',
                title: '¿Marcar como pagada?',
                text: `¿Confirmas que ya pagaste "${nombre}"?`,
                showCancelButton:    true,
                confirmButtonText:   'Sí, pagada',
                cancelButtonText:    'Cancelar',
                confirmButtonColor:  '#2ECC71',
                cancelButtonColor:   '#9E9E9E'
            }).then(function (result) {
                if (result.isConfirmed && form) form.submit();
            });
        });
    });

    const params = new URLSearchParams(window.location.search);
    const exito  = params.get('exito');
    const error  = params.get('error');

    const exitoMensajes = {
        registrada: { title: '¡Factura registrada!',   text: 'La factura se guardó correctamente.' },
        eliminada:  { title: 'Factura eliminada',       text: 'La factura fue eliminada.' },
        pagada:     { title: '¡Factura marcada!',       text: 'La factura fue marcada como pagada.' }
    };

    const errorMensajes = {
        sin_permiso:   'No tienes permisos para esa acción.',
        id_invalido:   'La factura no es válida.',
        no_encontrada: 'La factura no fue encontrada.',
        error_eliminar:'No se pudo eliminar la factura. Intenta de nuevo.'
    };

    if (exito && exitoMensajes[exito]) {
        const m = exitoMensajes[exito];
        Swal.fire({ icon: 'success', title: m.title, text: m.text, timer: 2500, showConfirmButton: false });
        window.history.replaceState({}, document.title, window.location.pathname);
    } else if (error && errorMensajes[error]) {
        Swal.fire({ icon: 'error', title: 'Error', text: errorMensajes[error], confirmButtonColor: '#1E88E5' });
        window.history.replaceState({}, document.title, window.location.pathname);
    }
});

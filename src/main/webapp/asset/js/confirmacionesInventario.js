document.addEventListener('DOMContentLoaded', function () {

    // ── Confirmar eliminación de producto del inventario ──────────────────────
    document.querySelectorAll('[data-confirmar-eliminar-inv]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const form   = this.closest('form');
            const nombre = this.dataset.nombreProducto || 'este producto';
            Swal.fire({
                icon: 'warning',
                title: '¿Eliminar del inventario?',
                html: `¿Seguro que deseas eliminar <strong>"${nombre}"</strong> del inventario?`,
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

    // ── Validar cantidad en formulario de edición ─────────────────────────────
    const formEditar = document.getElementById('formEditarInventario');
    if (formEditar) {
        formEditar.addEventListener('submit', function (e) {
            const cantidad = parseInt(document.getElementById('cantidad')?.value || 0);
            if (isNaN(cantidad) || cantidad < 0) {
                e.preventDefault();
                Swal.fire({
                    icon: 'error',
                    title: 'Cantidad inválida',
                    text: 'La cantidad no puede ser negativa.',
                    confirmButtonColor: '#1E88E5'
                });
                return false;
            }
        });
    }

    // ── Toast de resultado ────────────────────────────────────────────────────
    const params = new URLSearchParams(window.location.search);
    const error  = params.get('error');
    if (error === 'sin_permiso') {
        Swal.fire({ icon: 'error', title: 'Sin permisos', text: 'No tienes permisos para esa acción.', confirmButtonColor: '#1E88E5' });
        window.history.replaceState({}, document.title, window.location.pathname);
    }
    if (error === 'cantidad_invalida') {
        Swal.fire({ icon: 'error', title: 'Cantidad inválida', text: 'La cantidad ingresada no es válida.', confirmButtonColor: '#1E88E5' });
        window.history.replaceState({}, document.title, window.location.pathname);
    }
});

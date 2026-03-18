document.addEventListener('DOMContentLoaded', function () {

    // ── Confirmación al eliminar lista ────────────────────────────────────────
    document.querySelectorAll('[data-confirmar-eliminar-lista]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const form   = this.closest('form') || document.getElementById(this.dataset.confirmarEliminarLista);
            const nombre = this.dataset.nombreLista || 'esta lista';
            Swal.fire({
                icon: 'warning',
                title: '¿Eliminar lista?',
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

    // ── Confirmación al eliminar producto de lista ─────────────────────────────
    document.querySelectorAll('[data-confirmar-eliminar-producto]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const form   = this.closest('form');
            const nombre = this.dataset.nombreProducto || 'este producto';
            Swal.fire({
                icon: 'warning',
                title: '¿Quitar producto?',
                html: `¿Deseas quitar <strong>"${nombre}"</strong> de la lista?`,
                showCancelButton:    true,
                confirmButtonText:   'Sí, quitar',
                cancelButtonText:    'Cancelar',
                confirmButtonColor:  '#E51E1E',
                cancelButtonColor:   '#9E9E9E'
            }).then(function (result) {
                if (result.isConfirmed && form) form.submit();
            });
        });
    });

    // ── Confirmación al marcar/desmarcar todos ────────────────────────────────
    document.querySelectorAll('[data-marcar-todos]').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            const form    = this.closest('form');
            const accion  = this.dataset.marcarTodos === 'true' ? 'marcar' : 'desmarcar';
            Swal.fire({
                icon: 'question',
                title: `¿${accion === 'marcar' ? 'Marcar' : 'Desmarcar'} todos?`,
                text: `Se ${accion === 'marcar' ? 'marcarán' : 'desmarcarán'} todos los productos de la lista.`,
                showCancelButton:    true,
                confirmButtonText:   'Sí, continuar',
                cancelButtonText:    'Cancelar',
                confirmButtonColor:  '#1E88E5',
                cancelButtonColor:   '#9E9E9E'
            }).then(function (result) {
                if (result.isConfirmed && form) form.submit();
            });
        });
    });

    // ── Éxito al eliminar producto ────────────────────────────────────────────
    const params = new URLSearchParams(window.location.search);
    if (params.get('exito') === 'prod_eliminado') {
        Swal.fire({
            icon: 'success',
            title: 'Producto eliminado',
            text: 'El producto fue quitado de la lista.',
            timer: 2000,
            showConfirmButton: false
        });
        window.history.replaceState({}, document.title, window.location.pathname);
    }
    if (params.get('exito') === 'lista_eliminada') {
        Swal.fire({
            icon: 'success',
            title: 'Lista eliminada',
            timer: 2000,
            showConfirmButton: false
        });
        window.history.replaceState({}, document.title, window.location.pathname);
    }
    if (params.get('error') === 'sin_permiso') {
        Swal.fire({
            icon: 'error',
            title: 'Sin permisos',
            text: 'No tienes permisos para realizar esa acción.',
            confirmButtonColor: '#1E88E5'
        });
        window.history.replaceState({}, document.title, window.location.pathname);
    }
});

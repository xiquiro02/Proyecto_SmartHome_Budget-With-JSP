document.addEventListener('DOMContentLoaded', function () {
    const formulario = document.querySelector('.contenido__formulario');
    if (!formulario) return;

    formulario.addEventListener('submit', function (e) {
        const nuevaClave     = document.getElementById('nueva_clave')?.value?.trim() || "";
        const confirmarClave = document.getElementById('confirmar_clave')?.value?.trim() || "";

        // ── Validar longitud ──────────────────────────────────────────────────
        if (nuevaClave.length < 8) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Contraseña muy corta',
                text: 'La contraseña debe tener al menos 8 caracteres.',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#1E88E5'
            });
            return false;
        }

        // ── Validar letra ─────────────────────────────────────────────────────
        if (!/[A-Za-z]/.test(nuevaClave)) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Contraseña inválida',
                text: 'La contraseña debe incluir al menos una letra.',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#1E88E5'
            });
            return false;
        }

        // ── Validar número ────────────────────────────────────────────────────
        if (!/\d/.test(nuevaClave)) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Contraseña inválida',
                text: 'La contraseña debe incluir al menos un número.',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#1E88E5'
            });
            return false;
        }

        // ── Validar coincidencia ──────────────────────────────────────────────
        if (nuevaClave !== confirmarClave) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Contraseñas no coinciden',
                text: 'Verifica que ambas contraseñas sean iguales.',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#1E88E5'
            });
            return false;
        }

        return true;
    });

    const params = new URLSearchParams(window.location.search);
    const error  = params.get('error');
    if (!error) return;

    const mensajes = {
        vacia:          'La contraseña no puede estar vacía.',
        sesionexpirada: 'El enlace de recuperación expiró. Solicita uno nuevo.',
        true:           'No se pudo actualizar la contraseña. Intenta de nuevo.'
    };

    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: mensajes[error] || 'Ocurrió un error inesperado.',
        confirmButtonText: 'Entendido',
        confirmButtonColor: '#1E88E5'
    }).then(() => {
        window.history.replaceState({}, document.title, window.location.pathname);
    });
});

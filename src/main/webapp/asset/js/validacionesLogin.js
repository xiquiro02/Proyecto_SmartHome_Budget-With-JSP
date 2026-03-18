function validarCorreoContrasena() {
    const correo     = document.getElementById("email")?.value?.trim() || "";
    const contrasena = document.getElementById("password")?.value?.trim() || "";
    const recordar   = document.getElementById("recordardatos")?.checked || false;

    if (!correo || !contrasena) {
        Swal.fire({
            icon: 'warning',
            title: 'Campos incompletos',
            text: 'Por favor ingresa tu correo y contraseña.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    const reglasCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!reglasCorreo.test(correo)) {
        Swal.fire({
            icon: 'error',
            title: 'Correo inválido',
            text: 'Por favor ingresa un correo electrónico válido.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    // Guardar correo si "recordar datos" está activo
    if (recordar) {
        localStorage.setItem("email_recordado", correo);
    } else {
        localStorage.removeItem("email_recordado");
    }

    return true;
}

// ── Mostrar errores del servidor al cargar la página ─────────────────────────
window.addEventListener('DOMContentLoaded', function () {

    // Precargar correo guardado
    const correoGuardado = localStorage.getItem("email_recordado");
    if (correoGuardado) {
        const inputEmail = document.getElementById("email");
        const checkRecordar = document.getElementById("recordardatos");
        if (inputEmail) inputEmail.value = correoGuardado;
        if (checkRecordar) checkRecordar.checked = true;
    }

    const params = new URLSearchParams(window.location.search);
    const error  = params.get('error');
    if (!error) return;

    const errores = {
        invalido:          { icon: 'error',   titulo: 'Credenciales incorrectas', msg: 'Correo o contraseña incorrectos. Verifica tus datos.' },
        campos_vacios:     { icon: 'warning', titulo: 'Campos incompletos',       msg: 'Debes ingresar tu correo y contraseña.' },
        sin_hogar:         { icon: 'warning', titulo: 'Sin hogar asociado',       msg: 'Tu cuenta no tiene un hogar asociado. Contacta al administrador.' },
        sesion_requerida:  { icon: 'info',    titulo: 'Sesión requerida',         msg: 'Debes iniciar sesión para acceder a esa sección.' },
        sesion_invalida:   { icon: 'warning', titulo: 'Sesión inválida',          msg: 'Tu sesión es inválida. Por favor inicia sesión de nuevo.' },
        sesion_expirada:   { icon: 'info',    titulo: 'Sesión expirada',          msg: 'Tu sesión expiró por inactividad. Inicia sesión de nuevo.' },
        contrasena_cambiada: { icon: 'success', titulo: '¡Contraseña actualizada!', msg: 'Tu contraseña fue cambiada exitosamente. Inicia sesión.' },
        error_db:          { icon: 'error',   titulo: 'Error del sistema',        msg: 'Ocurrió un error interno. Inténtalo más tarde.' }
    };

    const e = errores[error] || { icon: 'error', titulo: 'Error', msg: 'Ocurrió un error inesperado.' };

    Swal.fire({
        icon: e.icon,
        title: e.titulo,
        text: e.msg,
        confirmButtonText: e.icon === 'success' ? '¡Listo!' : 'Reintentar',
        confirmButtonColor: '#1E88E5'
    }).then(() => {
        window.history.replaceState({}, document.title, window.location.pathname);
    });
});

// ── Utilidades de UI ──────────────────────────────────────────────────────────

function mostrarError(idCampo, mensaje) {
    const input = document.getElementById(idCampo);
    const span  = document.getElementById('error-' + idCampo);
    if (!input || !span) return;
    input.classList.add('input-error');
    input.classList.remove('input-valido');
    span.textContent = mensaje;
    span.classList.add('visible');
}

function marcarValido(idCampo) {
    const input = document.getElementById(idCampo);
    const span  = document.getElementById('error-' + idCampo);
    if (!input || !span) return;
    input.classList.remove('input-error');
    input.classList.add('input-valido');
    span.textContent = '';
    span.classList.remove('visible');
}

function limpiarEstado(idCampo) {
    const input = document.getElementById(idCampo);
    const span  = document.getElementById('error-' + idCampo);
    if (!input || !span) return;
    input.classList.remove('input-error', 'input-valido');
    span.textContent = '';
    span.classList.remove('visible');
}

// ── Reglas de validación ──────────────────────────────────────────────────────

const REGLAS_LOGIN = {

    email: function (v) {
        if (!v) return 'El correo es obligatorio.';
        if (!v.includes('@')) return 'El correo debe contener "@".';
        if (!/^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(v))
            return 'Ingresa un correo electrónico válido.';
        return null;
    },

    password: function (v) {
        if (!v) return 'La contraseña es obligatoria.';
        return null;
    }
};

// ── Validar un campo y mostrar/limpiar su mensaje ─────────────────────────────

function validarCampoLogin(idCampo) {
    const input = document.getElementById(idCampo);
    if (!input) return true;

    const valor = input.value.trim();
    const regla = REGLAS_LOGIN[idCampo];
    if (!regla) return true;

    const error = regla(valor);
    if (error) {
        mostrarError(idCampo, error);
        return false;
    }
    marcarValido(idCampo);
    return true;
}

// ── Función principal llamada por onsubmit ────────────────────────────────────

function validarCorreoContrasena() {
    const emailValido    = validarCampoLogin('email');
    const passwordValido = validarCampoLogin('password');

    if (!emailValido) {
        document.getElementById('email').focus();
        return false;
    }
    if (!passwordValido) {
        document.getElementById('password').focus();
        return false;
    }

    // Guardar correo si "recordar datos" está activo
    const correo   = document.getElementById('email').value.trim();
    const recordar = document.getElementById('recordardatos') ? document.getElementById('recordardatos').checked : false;
    if (recordar) {
        localStorage.setItem('email_recordado', correo);
    } else {
        localStorage.removeItem('email_recordado');
    }

    return true;
}

// ── Inicialización ────────────────────────────────────────────────────────────

window.addEventListener('DOMContentLoaded', function () {

    // Precargar correo guardado en localStorage
    const correoGuardado = localStorage.getItem('email_recordado');
    if (correoGuardado) {
        const inputEmail    = document.getElementById('email');
        const checkRecordar = document.getElementById('recordardatos');
        if (inputEmail)    inputEmail.value    = correoGuardado;
        if (checkRecordar) checkRecordar.checked = true;
    }

    // Listeners en tiempo real
    ['email', 'password'].forEach(function (id) {
        const input = document.getElementById(id);
        if (!input) return;

        // Al salir del campo: valida
        input.addEventListener('blur', function () {
            validarCampoLogin(id);
        });

        // Al escribir: si ya hay error, re-valida para limpiarlo cuando sea correcto
        input.addEventListener('input', function () {
            if (input.classList.contains('input-error')) {
                validarCampoLogin(id);
            }
        });
    });

    // ── Errores del servidor (parámetros en la URL) ───────────────────────────
    const params = new URLSearchParams(window.location.search);
    const error  = params.get('error');
    if (!error) return;

    const errores = {
        invalido:            { icon: 'error',   titulo: 'Credenciales incorrectas', msg: 'Correo o contraseña incorrectos. Verifica tus datos.' },
        campos_vacios:       { icon: 'warning', titulo: 'Campos incompletos',       msg: 'Debes ingresar tu correo y contraseña.' },
        sin_hogar:           { icon: 'warning', titulo: 'Sin hogar asociado',       msg: 'Tu cuenta no tiene un hogar asociado. Contacta al administrador.' },
        sesion_requerida:    { icon: 'info',    titulo: 'Sesión requerida',         msg: 'Debes iniciar sesión para acceder a esa sección.' },
        sesion_invalida:     { icon: 'warning', titulo: 'Sesión inválida',          msg: 'Tu sesión es inválida. Por favor inicia sesión de nuevo.' },
        sesion_expirada:     { icon: 'info',    titulo: 'Sesión expirada',          msg: 'Tu sesión expiró por inactividad. Inicia sesión de nuevo.' },
        contrasena_cambiada: { icon: 'success', titulo: '¡Contraseña actualizada!', msg: 'Tu contraseña fue cambiada exitosamente. Inicia sesión.' },
        error_db:            { icon: 'error',   titulo: 'Error del sistema',        msg: 'Ocurrió un error interno. Inténtalo más tarde.' }
    };

    const e = errores[error] || { icon: 'error', titulo: 'Error', msg: 'Ocurrió un error inesperado.' };

    Swal.fire({
        icon:               e.icon,
        title:              e.titulo,
        text:               e.msg,
        confirmButtonText:  e.icon === 'success' ? '¡Listo!' : 'Reintentar',
        confirmButtonColor: '#1E88E5'
    }).then(function () {
        window.history.replaceState({}, document.title, window.location.pathname);
    });
});

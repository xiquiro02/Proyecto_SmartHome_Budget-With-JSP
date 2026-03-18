function validarFormularioRegistro() {

    const documento         = document.getElementById("documento")?.value?.trim() || "";
    const nombre            = document.getElementById("nombre")?.value?.trim() || "";
    const apellido1         = document.getElementById("apellido")?.value?.trim() || "";
    const apellido2         = document.getElementById("segundoApellido")?.value?.trim() || "";
    const correo            = document.getElementById("correo")?.value?.trim() || "";
    const telefono          = document.getElementById("telefono")?.value?.trim() || "";
    const contrasena        = document.getElementById("password")?.value?.trim() || "";
    const confirmarContrasena = document.getElementById("confirmarPassword")?.value?.trim() || "";
    const codigoInvitacion  = document.getElementById("codigoInvitacion")?.value?.trim() || "";

    // ── Campos obligatorios ────────────────────────────────────────────────────
    if (!documento || !nombre || !apellido1 || !correo || !telefono || !contrasena || !confirmarContrasena) {
        Swal.fire({
            icon: 'warning',
            title: 'Campos incompletos',
            text: 'Por favor completa todos los campos obligatorios.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    // ── Documento (Colombia: Solo números, entre 6 y 11 dígitos) ──────────────────
    const reglasDocumento = /^[0-9]{6,11}$/;

    if (!reglasDocumento.test(documento)) {
        Swal.fire({
            icon: 'error',
            title: 'Documento inválido',
            text: 'El documento debe contener solo números y tener entre 6 y 11 dígitos.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    // ── Nombre y apellidos (solo letras) ──────────────────────────────────────
    const reglasLetras = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/;
    if (!reglasLetras.test(nombre) || !reglasLetras.test(apellido1) ||
        (apellido2 && !reglasLetras.test(apellido2))) {
        Swal.fire({
            icon: 'error',
            title: 'Nombre inválido',
            text: 'El nombre y los apellidos solo pueden contener letras.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    // ── Correo ────────────────────────────────────────────────────────────────
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

    // ── Teléfono ──────────────────────────────────────────────────────────────
    const reglasTelefono = /^[0-9]{8,15}$/;
    if (!reglasTelefono.test(telefono)) {
        Swal.fire({
            icon: 'error',
            title: 'Teléfono inválido',
            text: 'El teléfono debe contener solo números, entre 8 y 15 dígitos.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    // ── Contraseña (mínimo 8 caracteres, letras y números) ─────────────
    if (contrasena.length < 8) {
        Swal.fire({
            icon: 'error',
            title: 'Contraseña muy corta',
            text: 'La contraseña debe tener al menos 8 caracteres.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    if (!/[A-Za-z]/.test(contrasena)) {
        Swal.fire({
            icon: 'error',
            title: 'Contraseña inválida',
            text: 'La contraseña debe incluir al menos una letra.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    if (!/\d/.test(contrasena)) {
        Swal.fire({
            icon: 'error',
            title: 'Contraseña inválida',
            text: 'La contraseña debe incluir al menos un número.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    // ── Confirmar contraseña ──────────────────────────────────────────────────
    if (contrasena !== confirmarContrasena) {
        Swal.fire({
            icon: 'error',
            title: 'Contraseñas no coinciden',
            text: 'Verifica que ambas contraseñas sean iguales.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    // ── Código de invitación (opcional) ───────────────────────────────────────
    if (codigoInvitacion && !/^[A-Za-z0-9]{4,50}$/.test(codigoInvitacion)) {
        Swal.fire({
            icon: 'warning',
            title: 'Código inválido',
            text: 'El código de invitación solo puede contener letras y números.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#1E88E5'
        });
        return false;
    }

    return true;
}

window.addEventListener('DOMContentLoaded', function () {
    const params = new URLSearchParams(window.location.search);
    const error  = params.get('error');
    if (!error) return;

    const errores = {
        campos_vacios:    { icon: 'warning', titulo: 'Campos incompletos',    msg: 'Por favor completa todos los campos obligatorios.' },
        correo_existe:    { icon: 'info',    titulo: 'Correo registrado',      msg: 'Este correo electrónico ya está registrado.' },
        documento_existe: { icon: 'info',    titulo: 'Documento registrado',   msg: 'Este número de documento ya está registrado.' },
        codigo_invalido:  { icon: 'warning', titulo: 'Código inválido',        msg: 'El código de invitación no es válido o ha expirado.' },
        registro_fallido: { icon: 'error',   titulo: 'Error al registrar',     msg: 'No se pudo completar el registro. Intenta de nuevo.' },
        crear_hogar:      { icon: 'error',   titulo: 'Error interno',          msg: 'No se pudo crear el hogar. Intenta de nuevo.' },
        error_db:         { icon: 'error',   titulo: 'Error del sistema',      msg: 'Ocurrió un error interno. Inténtalo más tarde.' }
    };

    const e = errores[error] || { icon: 'error', titulo: 'Error', msg: 'Ocurrió un error inesperado.' };

    Swal.fire({
        icon: e.icon,
        title: e.titulo,
        text: e.msg,
        confirmButtonText: 'Entendido',
        confirmButtonColor: '#1E88E5'
    }).then(() => {
        window.history.replaceState({}, document.title, window.location.pathname);
    });
});

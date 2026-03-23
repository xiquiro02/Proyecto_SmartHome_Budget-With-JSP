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

const REGLAS = {

    documento: function (v) {
        if (!v)                        return 'El documento es obligatorio.';
        if (!/^[0-9]{6,11}$/.test(v)) return 'Solo números, entre 6 y 11 dígitos.';
        return null;
    },

    nombre: function (v) {
        if (!v)                                     return 'El nombre es obligatorio.';
        if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/.test(v))  return 'Solo letras, sin números ni símbolos.';
        return null;
    },

    apellido: function (v) {
        if (!v)                                     return 'El apellido es obligatorio.';
        if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/.test(v))  return 'Solo letras, sin números ni símbolos.';
        return null;
    },

    segundoApellido: function (v) {
        if (!v) return null; // opcional
        if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/.test(v)) return 'Solo letras, sin números ni símbolos.';
        return null;
    },

    correo: function (v) {
        if (!v) return 'El correo es obligatorio.';

        const arrobaIdx = v.indexOf('@');
        if (arrobaIdx === -1) return 'El correo debe contener "@".';

        const local   = v.substring(0, arrobaIdx);
        const dominio = v.substring(arrobaIdx + 1).toLowerCase();

        if (!local) return 'Falta el usuario antes del "@".';
        if (local.length > 25)
            return 'La parte antes del "@" tiene ' + local.length + ' caracteres (máximo 25).';
        if (!/^[a-zA-Z0-9._%+\-]+$/.test(local))
            return 'El usuario antes del "@" contiene caracteres no permitidos.';

        const permitidos = ['gmail.com', 'hotmail.com', 'outlook.com', 'soy.sena.edu.co'];
        if (!permitidos.includes(dominio))
            return 'Dominio no permitido. Usa: gmail.com, hotmail.com, outlook.com o soy.sena.edu.co';

        return null;
    },

    telefono: function (v) {
        if (!v)                        return 'El teléfono es obligatorio.';
        if (!/^[0-9]{8,15}$/.test(v)) return 'Solo números, entre 8 y 15 dígitos.';
        return null;
    },

    password: function (v) {
        if (!v)           return 'La contraseña es obligatoria.';
        if (v.length < 8) return 'Debe tener al menos 8 caracteres.';
        if (!/[A-Za-z]/.test(v)) return 'Debe incluir al menos una letra.';
        if (!/\d/.test(v))       return 'Debe incluir al menos un número.';
        return null;
    },

    confirmarPassword: function (v) {
        if (!v) return 'Confirma tu contraseña.';
        const clave = document.getElementById('password') ? document.getElementById('password').value : '';
        if (v !== clave) return 'Las contraseñas no coinciden.';
        return null;
    },

    codigoInvitacion: function (v) {
        if (!v) return null; // opcional
        if (!/^[A-Za-z0-9]{4,50}$/.test(v)) return 'Solo letras y números (entre 4 y 50 caracteres).';
        return null;
    }
};

// ── Validar un campo y mostrar/limpiar su mensaje ─────────────────────────────

function validarCampo(idCampo) {
    const input = document.getElementById(idCampo);
    if (!input) return true;

    const valor = input.value.trim();
    const regla = REGLAS[idCampo];
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

function validarFormularioRegistro() {
    const camposObligatorios = [
        'documento', 'nombre', 'apellido',
        'correo', 'telefono', 'password', 'confirmarPassword'
    ];
    const camposOpcionales = ['segundoApellido', 'codigoInvitacion'];

    let valido      = true;
    let primerError = null;

    camposObligatorios.forEach(function (id) {
        if (!validarCampo(id)) {
            valido = false;
            if (!primerError) primerError = id;
        }
    });

    camposOpcionales.forEach(function (id) {
        const input = document.getElementById(id);
        if (input && input.value.trim()) {
            if (!validarCampo(id)) {
                valido = false;
                if (!primerError) primerError = id;
            }
        }
    });

    if (!valido && primerError) {
        document.getElementById(primerError).focus();
    }

    return valido;
}

// ── Validación en tiempo real (blur + input) ──────────────────────────────────

window.addEventListener('DOMContentLoaded', function () {

    const todosCampos = [
        'documento', 'nombre', 'apellido', 'segundoApellido',
        'correo', 'telefono', 'password', 'confirmarPassword', 'codigoInvitacion'
    ];

    todosCampos.forEach(function (id) {
        const input = document.getElementById(id);
        if (!input) return;

        // Al salir del campo: valida
        input.addEventListener('blur', function () {
            const opcionales = ['segundoApellido', 'codigoInvitacion'];
            if (!input.value.trim() && opcionales.includes(id)) {
                limpiarEstado(id);
                return;
            }
            validarCampo(id);
        });

        // Al escribir: si ya hay error, re-valida para limpiarlo cuando el usuario lo corrija
        input.addEventListener('input', function () {
            if (input.classList.contains('input-error')) {
                validarCampo(id);
            }
            // Si cambia la contraseña, re-validar confirmación si ya tiene error
            if (id === 'password') {
                const confirmar = document.getElementById('confirmarPassword');
                if (confirmar && confirmar.classList.contains('input-error')) {
                    validarCampo('confirmarPassword');
                }
            }
        });
    });

    // ── Errores del servidor (parámetros en la URL) ───────────────────────────
    const params = new URLSearchParams(window.location.search);
    const error  = params.get('error');
    if (!error) return;

    const errores = {
        campos_vacios:    { icon: 'warning', titulo: 'Campos incompletos',   msg: 'Por favor completa todos los campos obligatorios.' },
        correo_existe:    { icon: 'info',    titulo: 'Correo registrado',     msg: 'Este correo electrónico ya está registrado.' },
        documento_existe: { icon: 'info',    titulo: 'Documento registrado',  msg: 'Este número de documento ya está registrado.' },
        codigo_invalido:  { icon: 'warning', titulo: 'Código inválido',       msg: 'El código de invitación no es válido o ha expirado.' },
        registro_fallido: { icon: 'error',   titulo: 'Error al registrar',    msg: 'No se pudo completar el registro. Intenta de nuevo.' },
        crear_hogar:      { icon: 'error',   titulo: 'Error interno',         msg: 'No se pudo crear el hogar. Intenta de nuevo.' },
        error_db:         { icon: 'error',   titulo: 'Error del sistema',     msg: 'Ocurrió un error interno. Inténtalo más tarde.' }
    };

    const e = errores[error] || { icon: 'error', titulo: 'Error', msg: 'Ocurrió un error inesperado.' };

    Swal.fire({
        icon:               e.icon,
        title:              e.titulo,
        text:               e.msg,
        confirmButtonText:  'Entendido',
        confirmButtonColor: '#1E88E5'
    }).then(function () {
        window.history.replaceState({}, document.title, window.location.pathname);
    });
});

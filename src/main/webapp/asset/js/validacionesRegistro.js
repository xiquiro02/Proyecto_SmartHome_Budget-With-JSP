function validarFormulario() {

    const nombre = document.getElementById("nombre").value;
    const apellido1 = document.getElementById("apellido").value;
    const apellido2 = document.getElementById("segundoApellido").value;
    const correo = document.getElementById("correo").value;
    const telefono = document.getElementById("telefono").value;
    const contrasena = document.getElementById("password").value;
    const confirmarContrasena = document.getElementById("confirmarPassword").value;
    const codigoInvitacion = document.getElementById("codigoInvitacion").value;

    if (!nombre || !apellido1 || !correo || !telefono || !contrasena || !confirmarContrasena) {
        Swal.fire({
            icon: 'error',
            title: 'Campos incompletos',
            text: '¡Todos los campos son obligatorios!. Por favor, completa todos los campos.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#007bff'
        });
        return false;
    }

    const reglasLetras = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/;
    if (!reglasLetras.test(nombre) || !reglasLetras.test(apellido1) || (apellido2 && !reglasLetras.test(apellido2))) {
        Swal.fire({
            icon: 'error',
            title: '¡Oops!',
            text: '¡Solo se permiten letras!. Por favor, verifica.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#007bff'
        });
        return false;
    }

    const reglasTelefono = /^[0-9]{8,15}$/;
    if (!reglasTelefono.test(telefono)) {
        Swal.fire({
            icon: 'error',
            title: 'Teléfono inválido',
            text: '¡Solo se permiten números!. Por favor, verifica.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#007bff'
        });
        return false;
    }

    const reglasContrasena = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
    if (!reglasContrasena.test(contrasena)) {
        Swal.fire({
            icon: 'error',
            title: '¡Oops!',
            text: '¡La contraseña debe tener al menos 8 caracteres, una letra y un número!. Por favor, verifica.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#007bff'
        });
        return false;
    }

    if (contrasena !== confirmarContrasena) {
        Swal.fire({
            icon: 'error',
            title: '¡Oops!',
            text: '¡Las contraseñas no coinciden!. Por favor, verifica.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#007bff'
        });
        return false;
    }

    // Validar código de invitación (opcional)
    if (!validarCodigoInvitacion(codigoInvitacion)) {
        Swal.fire({
            icon: 'error',
            title: 'Código inválido',
            text: 'El código de invitación debe tener entre 4 y 10 caracteres, solo letras y números.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#007bff'
        });
        return false;
    }

    return true;
}

// Función para validar código de invitación
function validarCodigoInvitacion(codigo) {
    // Si está vacío, es válido (campo opcional)
    if (!codigo || codigo.trim() === '') {
        return true;
    }
    
    // Si tiene contenido, validar formato
    const reglasCodigo = /^[A-Za-z0-9]{4,10}$/;
    return reglasCodigo.test(codigo.trim());
}

window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');

    if (!error) return;

    let icono = 'error';
    let titulo = '¡Oops!';
    let mensaje = '¡Algo salió mal!. Por favor, verifica.';

    switch (error) {
        case 'campos_vacios':
            icono = "warning";
            titulo = 'Campos incompletos';
            mensaje = '¡Todos los campos son obligatorios!. Por favor, completa todos los campos.';
            break;
        case 'contrasena_no_coincide':
            icono = "error";
            titulo = 'Contraseña';
            mensaje = '¡Las contraseñas no coinciden!. Por favor, verifica.';
            break;
        case 'correo_ya_registrado':
            icono = "info";
            titulo = "Correo duplicado";
            mensaje = '¡El correo electrónico ya está registrado!. Por favor, verifica.';
            break;
        case 'error_db':
            icono = "error";
            titulo = "Error técnico";
            mensaje = 'Ocurrió un error interno. Inténtalo más tarde.';
            break;
        case 'codigo_invalido':
            icono = "warning";
            titulo = 'Código inválido';
            mensaje = 'El código de invitación no es válido o ha expirado.';
            break;
        case 'correo_existe':
            icono = "info";
            titulo = "Correo duplicado";
            mensaje = '¡El correo electrónico ya está registrado!. Por favor, verifica.';
            break;
    }

    Swal.fire({
        icon: icono,
        title: titulo,
        text: mensaje,
        confirmButtonText: 'Entendido',
        confirmButtonColor: '#007bff'
    })
        .then(() => {
            window.history.replaceState({}, this.document.title, window.location.pathname);
        });
}
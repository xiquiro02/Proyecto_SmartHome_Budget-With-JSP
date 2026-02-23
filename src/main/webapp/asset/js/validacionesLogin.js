function validarCorreoContrasena() {
    const gmail = document.getElementById("email").value.trim();
    const contrasena = document.getElementById("password").value.trim();
    const recordar = document.getElementById("recordardatos").checked;

    if (gmail === "" || contrasena === "") {
        Swal.fire({
            icon: 'warning',
            title: 'Campos incompletos',
            text: 'Por favor, ingresa tanto el correo como la contraseña.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#007bff',
        });
        return false;
    }

    const reglasCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!reglasCorreo.test(gmail)) {
        Swal.fire({
            icon: 'error',
            title: 'Correo inválido',
            text: 'Por favor, ingresa un correo electrónico válido.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#d33',
        });
        return false;
    }

    const reglasContrasena = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
    if (!reglasContrasena.test(contrasena)) {
        Swal.fire({
            icon: 'error',
            title: 'Contraseña inválida',
            text: 'La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, una minúscula y un número.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#d33',
        });
        return false;
    }

    if (recordar) {
        localStorage.setItem("email", gmail);
    }
    else {
        localStorage.removeItem("email");
    }

    return true;

}

window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');


    const correoGuardado = localStorage.getItem("email");
    if (correoGuardado) {
        document.getElementById("email").value = correoGuardado;
        document.getElementById("recordardatos").checked = true;
    }

    if (!error) return;

    let icono = 'error';
    let titulo = "Error de acceso";
    let mensaje = "Ocurrió un problema al intentar ingresar.";

    switch (error) {
        case "invalido":
            icono = "error";
            titulo = "Error de acceso";
            mensaje = "Correo o contraseña incorrectos. Por favor, verifica tus datos.";
            break;
        case "campos_vacios":
            icono = "warning";
            titulo = "Campos incompletos";
            mensaje = "Debes completar todos los campos para ingresar.";
            break;
        case "error_db":
            icono = "error";
            titulo = "Error del sistema";
            mensaje = "Ocurrió un error interno. Inténtalo más tarde.";
            break;
    }

    Swal.fire({
        icon: icono,
        title: titulo,
        text: mensaje,
        confirmButtonText: 'Reintentar',
        confirmButtonColor: '#d33'
    })
        .then(() => {
            window.history.replaceState({}, this.document.title, window.location.pathname);
        });
};
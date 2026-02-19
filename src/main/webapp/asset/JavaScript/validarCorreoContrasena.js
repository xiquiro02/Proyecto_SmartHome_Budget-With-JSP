function validarCorreoContrasena() {
    const gmail = document.getElementById("email").value.trim();
    const contrasena = document.getElementById("password").value.trim();

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
}

window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');

    if (error === "invalido") {
        Swal.fire({
            icon: 'error',
            title: 'Error de acceso',
            text: 'Correo o contraseña incorrectos.',
            confirmButtonText: 'Reintentar',
            confirmButtonColor: '#d33',
        });
    }
};
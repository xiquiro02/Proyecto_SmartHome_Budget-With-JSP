function validarContrasenas() {
    const contrasena = document.getElementById("password").value;
    const confirmarContrasena = document.getElementById("confirmarPassword").value;
    if (contrasena !== confirmarContrasena) {
        Swal.fire({
            icon: 'error',
            title: '¡Oops!',
            text: '¡Las contraseñas no coinciden!. Por favor, verifica.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#007bff',
        });
        return false;
    }
    return true;
}
// Validación de contraseñas para formulario de Nueva Clave
document.addEventListener('DOMContentLoaded', function() {
    console.log("DOM cargado - Inicializando validación de Nueva Clave");
    
    const formulario = document.querySelector('.contenido__formulario');
    
    if (formulario) {
        formulario.addEventListener('submit', function(e) {
            console.log("=== FORMULARO NUEVA CLAVE ENVIADO ===");
            
            const nuevaClave = document.getElementById('nueva_clave').value;
            const confirmarClave = document.getElementById('confirmar_clave').value;
            
            console.log("Nueva clave: " + nuevaClave);
            console.log("Confirmar clave: " + confirmarClave);
            
            // Validar que las contraseñas coincidan
            if (nuevaClave !== confirmarClave) {
                console.log("ERROR: Las contraseñas no coinciden");
                e.preventDefault();
                alert('Las contraseñas no coinciden. Por favor, verifica los campos.');
                return false;
            }
            
            // Validar requisitos mínimos (coincidentes con expresión regular /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/)
            
            // Validar longitud mínima de 8 caracteres
            if (nuevaClave.length < 8) {
                console.log("ERROR: La contraseña debe tener mínimo 8 caracteres");
                e.preventDefault();
                alert('La contraseña debe tener mínimo 8 caracteres.');
                return false;
            }
            
            // Validar que tenga al menos una letra (mayúscula o minúscula)
            if (!/[A-Za-z]/.test(nuevaClave)) {
                console.log("ERROR: La contraseña debe incluir al menos una letra");
                e.preventDefault();
                alert('La contraseña debe incluir al menos una letra.');
                return false;
            }
            
            // Validar que tenga al menos un número
            if (!/\d/.test(nuevaClave)) {
                console.log("ERROR: La contraseña debe incluir al menos un número");
                e.preventDefault();
                alert('La contraseña debe incluir al menos un número.');
                return false;
            }
            
            console.log("VALIDACIÓN EXITOSA - Enviando formulario...");
            return true; // Permitir envío
        });
    } else {
        console.error("ERROR: No se encontró el formulario con la clase '.contenido__formulario'");
    }
});

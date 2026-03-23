document.addEventListener('DOMContentLoaded', function() {
    const switchNotificaciones = document.getElementById('switchNotificaciones');
    
    // Cargar preferencias guardadas
    cargarPreferenciasNotificaciones();
    
    // Event listener para el switch
    if (switchNotificaciones) {
        switchNotificaciones.addEventListener('change', function() {
            const estado = this.checked; // true = activado (verde), false = desactivado (gris)
            guardarPreferenciaNotificaciones('notificaciones', estado);
            
            if (estado) {
                // Si activa las notificaciones (switch en verde)
                mostrarNotificacion('Notificaciones activadas', 'Las notificaciones han sido activadas', 'success');
            } else {
                // Si desactiva las notificaciones (switch en gris)
                mostrarNotificacion('Notificaciones desactivadas', 'Las notificaciones han sido desactivadas', 'warning');
            }
        });
    }
});

// Guardar preferencias en el servidor
function guardarPreferenciaNotificaciones(tipo, estado) {
    fetch(`${window.location.origin}/Notificaciones`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `accion=guardarPreferencia&tipo=${tipo}&estado=${estado}`
    })
    .then(response => response.json())
    .then(data => {
        console.log('Preferencia guardada:', data);
    })
    .catch(error => {
        console.error('Error guardando preferencia:', error);
    });
}

// Cargar preferencias del servidor
function cargarPreferenciasNotificaciones() {
    fetch(`${window.location.origin}/Notificaciones?accion=cargarPreferencias`)
    .then(response => response.json())
    .then(data => {
        const switchNotificaciones = document.getElementById('switchNotificaciones');
        if (switchNotificaciones) {
            switchNotificaciones.checked = data.notificacionesActivadas || false;
        }
    })
    .catch(error => {
        console.error('Error cargando preferencias:', error);
    });
}

// Mostrar notificaciones reales
function mostrarNotificacion(titulo, mensaje, tipo = 'info') {
    // Usar SweetAlert si está disponible
    if (typeof Swal !== 'undefined') {
        Swal.fire({
            icon: tipo === 'success' ? 'success' : tipo === 'warning' ? 'warning' : 'info',
            title: titulo,
            text: mensaje,
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true
        });
    } else {
        // Fallback a notificaciones del navegador
        if ('Notification' in window && Notification.permission === 'granted') {
            new Notification(titulo, {
                body: mensaje,
                icon: '/asset/imagenes/notificacion-TODAS.png'
            });
        }
    }
}

// Solicitar permiso para notificaciones del navegador
function solicitarPermisoNotificaciones() {
    if ('Notification' in window && Notification.permission === 'default') {
        Notification.requestPermission().then(permission => {
            if (permission === 'granted') {
                console.log('Permiso de notificaciones concedido');
            }
        });
    }
}

// Solicitar permiso al cargar la página
solicitarPermisoNotificaciones();

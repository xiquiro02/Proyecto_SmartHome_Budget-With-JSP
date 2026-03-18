/*
 * cargando.js
 * Pantalla de carga inicial — redirige al principal después de 3 segundos.
 */
setTimeout(function () {
    // Detectar el contextPath desde la URL actual
    // Ejemplo: si la app está en /SmartHome_Budget, el path es /SmartHome_Budget/public/...
    var path = window.location.pathname;
    var contextPath = '';

    // Si hay un contexto (no raíz), extraerlo
    var segments = path.split('/');
    if (segments.length > 1 && segments[1] !== '') {
        contextPath = '/' + segments[1];
    }

    window.location.href = contextPath + '/public/modules/01_autenticacion/01_principal.jsp';
}, 3000);

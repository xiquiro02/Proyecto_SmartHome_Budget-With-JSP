<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Link Iconos -->
        <link rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=more_vert" />
        <!-- Link Fuentes -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <!-- Link estilos.css  -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
        <link rel="stylesheet"
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosPrincipal.css">
        <title>SmartHome Budget</title>

    </head>

    <body>
        <header class="encabezado">
            <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/05_necesitasAyuda.jsp">
                <span class="material-symbols-outlined"> more_vert </span>
            </a>
        </header>
        <main class="contenido">
            <div class="contenido__contenedorimagen">
                <img class="contenido__imagen"
                    src="${pageContext.request.contextPath}/asset/imagenes/icono-smarthome.png"
                    alt="Logo de SmartHome Budget">
            </div>
            <p class="contenido__parrafo">Selecciona una de las siguientes opciones:</p>
            <div class="contenido__contenedorbutton">
                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/04_iniciarSesion.jsp">
                    <button type="button" class="boton boton--iniciar">Iniciar Sesión</button>
                </a>
                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/02_registrarse.jsp">
                    <button type="button" class="boton boton--registrar">Registrarse</button>
                </a>
            </div>
        </main>
    </body>

    </html>
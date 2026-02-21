<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Link estilos.css  -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/estilos_index.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <script src="${pageContext.request.contextPath}/asset/js/cargando.js"></script>
        <main class="carga">
            <section class="carga__contenido">
                <div class="carga__logo-contenedor">
                    <img class="carga__logo" src="${pageContext.request.contextPath}/asset/imagenes/icono-smarthome.png"
                        alt="Logo de SmartHome Budget">
                </div>
                <div class="carga__gif-contenedor">
                    <img class="carga__gif" src="${pageContext.request.contextPath}/asset/imagenes/cargando-2.gif"
                        alt="Animación de carga">
                </div>
            </section>
        </main>
    </body>

    </html>
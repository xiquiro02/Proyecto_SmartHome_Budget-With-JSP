<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Link iconos  -->
        <link rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
        <!-- Link Fuentes -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <!-- Link estilos.css  -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
        <link rel="stylesheet"
            href="${pageContext.request.contextPath}/asset/css/modules/05_TiendasCercanas/estilosUbicacionGuardada-manualmente.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <main class="contenido">
            <div class="contenedor">
                <img class="contenido__icono-img"
                    src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png"
                    alt="Icono verificación">
                <h1 class="contenido__titulo">Ubicación guardada </h1>
                <p class="contenido__parrafo">La ubicación que ingresaste ha sido registrada. Puedes cambiarla más
                    adelante en Tiendas Cercanas cuando lo necesites.</p>
                <div class="contenido__grupo">
                    <a
                        href="${pageContext.request.contextPath}/public/modules/05_TiendasCercanas/01_TiendasCercanas.jsp">
                        <button type="submit" class="boton boton--registrar">Aceptar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
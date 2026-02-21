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
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosConfirmar-cambio.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/20_EditarPerfil.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
        </header>
        <main class="contenido">
            <div class="contenedor">
                <h1 class="contenido__titulo">Confirmar cambio</h1>
                <img class="contenido__icono-img"
                    src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png"
                    alt="Icono verificación">
                <p class="contenido__parrafo">Ahora puedes visualizar tus cambios en tu perfil.</p>
                <div class="contenido__grupo">
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/19_MiPerfil.jsp">
                        <button type="submit" class="boton boton--registrar">Aceptar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
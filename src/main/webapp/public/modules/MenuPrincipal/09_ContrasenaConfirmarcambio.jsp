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
            href="${pageContext.request.contextPath}/asset/css/modules/Menuprincipal/estilosContrasenaConfirmarcambio.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <main class="contenido">
            <div class="contenedor">
                <h1 class="contenido__titulo">Confirmar cambio</h1>
                <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/correo.png"
                    alt="Icono Correo">
                <p class="contenido__parrafo">Se ha enviado un correo de confirmación a</p>
                <p class="contenido__parrafos">maria0525perez@gmail.com</p>
                <p class="contenido__parrafo">Ingrese el código de verificación de 6 dígitos enviado a su dirección de
                    correo electrónico</p>
                <div class="contenido__grupo">
                    <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/10_CodigoVerificacion.jsp">
                        <button type="submit" class="boton boton--registrar">Confirmar</button>
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/08_Cambiarcontrasena.jsp">
                        <button type="submit" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
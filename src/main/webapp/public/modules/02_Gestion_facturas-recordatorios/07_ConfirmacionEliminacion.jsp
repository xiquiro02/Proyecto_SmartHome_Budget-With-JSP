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
            href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosConfirmacionEliminacion.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <main class="contenido">
            <div class="contenedor">
                <h1 class="contenido__titulo">Factura anulada correctamente</h1>
                <p class="contenido__parrafo">La factura seleccionada fue anulada y ya no aparecerá en tu listado activo.
                    Quedará guardada en la base de datos. Si fue un error, contacta al administrador del hogar.</p>
                <div class="contenido__grupo">
                    <a
                        href="${pageContext.request.contextPath}/public/modules/02_Gestion_facturas-recordatorios/03_Consultar-Facturas.jsp">
                        <button type="submit" class="boton boton--registrar">Aceptar</button>
                    </a>
                </div>
            </div>
        </main>
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                <script src="${pageContext.request.contextPath}/asset/js/confirmacionesFacturas.js"></script>
    </body>

    </html>
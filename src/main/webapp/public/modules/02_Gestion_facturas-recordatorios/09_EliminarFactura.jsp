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
            href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosEliminarFactura.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <main class="contenido">
            <div class="contenedor">
                <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/eliminar.png"
                    alt="Icono de Eliminar">
                <h1 class="contenido__titulo">Eliminar factura</h1>
                <p class="contenido__parrafo">¿Estás seguro de que deseas eliminar esta factura?</p>
                <p class="contenido__parrafos">Esta acción no se puede deshacer.</p>
                <div class="contenido__grupo">
                    <a
                        href="${pageContext.request.contextPath}/public/modules/02_Gestion_facturas-recordatorios/10_ConfirmacionEliminacion.jsp">
                        <button type="submit" class="boton boton--Eliminar">Sí, eliminar</button>
                    </a>
                    <a
                        href="${pageContext.request.contextPath}/public/modules/02_Gestion_facturas-recordatorios/03_Consultar-Facturas.jsp">
                        <button type="submit" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
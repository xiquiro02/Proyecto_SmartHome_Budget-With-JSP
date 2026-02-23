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
            href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosEliminarProducto.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <main class="contenido">
            <div class="contenedor">
                <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/eliminar.png"
                    alt="Icono de Eliminar">
                <h1 class="contenido__titulo">Eliminar producto</h1>
                <p class="contenido__parrafo">¿Deseas eliminar el producto <strong>“Arroz blanco”</strong> de la lista
                    <strong>“Mercado mensual”</strong> ?
                </p>
                <p class="contenido__parrafo">Esta acción no se puede deshacer.</p>
                <div class="contenido__grupo">
                    <a
                        href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/13_ConfirmarEliminacionProducto.jsp">
                        <button type="submit" class="boton boton--Eliminar">Sí, eliminar</button>
                    </a>
                    <a
                        href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/04_EditarListaCompras.jsp">
                        <button type="submit" class="boton boton--cancelar">No, conservar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
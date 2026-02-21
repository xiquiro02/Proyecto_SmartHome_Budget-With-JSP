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
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosRespuestas.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png"
                alt="Logo de SmartHome Budget">
            <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/05_necesitasAyuda.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
            <div class="encabezado__contenedorTitulo">
                <h1 class="encabezado__titulo">¿Necesitas Ayuda?</h1>
            </div>
        </header>
        <main class="contenido">
            <div class="contenido__pregunta">
                <img class="contenido__pregunta-imagen"
                    src="${pageContext.request.contextPath}/asset/imagenes/producto-agotado.png" alt="Producto agotado">
                <h2 class="contenido__pregunta-texto">¿Cómo sabe la aplicación que un producto está por agotarse?</h2>
            </div>
            <section class="contenido__detalle">
                <p class="contenido__detalle-parrafo">La aplicación monitorea tu inventario y te alerta cuando un
                    producto está próximo a agotarse:</p>
                <ul class="contenido__detalle-lista">
                    <li class="contenido__detalle-item">Define una cantidad mínima para cada producto (por defecto o
                        personalizada) al registrarlo.</li>
                    <li class="contenido__detalle-item">Cuando la cantidad disponible llegue a ese nivel bajo, recibirás
                        una notificación push.</li>
                    <li class="contenido__detalle-item">Los productos con baja cantidad se sugerirán automáticamente en
                        tu lista de compras, y si llega a cero, se moverán a la lista de compras.</li>
                    <li class="contenido__detalle-item">Puedes activar o desactivar estas alertas en configuración.
                        Requiere conexión a internet para notificaciones.</li>
                </ul>
                <p class="contenido__detalle-parrafo">Esto te ayuda a reponer productos a tiempo y evitar interrupciones
                    en tu hogar.</p>
            </section>
            <div class="contenido__boton">
                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/05_necesitasAyuda.jsp"
                    class="contenido__boton-enlace">
                    <button type="button" class="boton boton--registrar">Volver</button>
                </a>
            </div>
        </main>
    </body>

    </html>
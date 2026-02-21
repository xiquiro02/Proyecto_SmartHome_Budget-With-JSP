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
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo redondo.png"
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
                    src="${pageContext.request.contextPath}/asset/imagenes/factura-como-pagada.png"
                    alt="Factura pagada">
                <h2 class="contenido__pregunta-texto">¿Puedo marcar una factura como "pagada"?</h2>
            </div>
            <section class="contenido__detalle">
                <p class="contenido__detalle-parrafo">
                    Sí, puedes marcar una factura como "pagada" para actualizar su estado y registrar el historial:
                </p>
                <ol class="contenido__detalle-lista">
                    <li class="contenido__detalle-item">Ve al módulo "Facturas y Pagos".</li>
                    <li class="contenido__detalle-item">Selecciona "Consultar Facturas".</li>
                    <li class="contenido__detalle-item">Selecciona la factura pendiente de la lista.</li>
                    <li class="contenido__detalle-item">Haz clic en "Editar" y edita el estado a "Pagada".</li>
                    <li class="contenido__detalle-item">Ingresa la fecha de pago real y confirma.</li>
                    <li class="contenido__detalle-item">El sistema la moverá al historial de pagos, incluyendo fecha y
                        monto, para análisis financiero.</li>
                </ol>
                <p class="contenido__detalle-parrafo">
                    Solo se guardarán pagos confirmados como realizados.
                </p>
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
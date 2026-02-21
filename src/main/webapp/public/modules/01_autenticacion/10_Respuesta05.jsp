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
            <section class="contenido__pregunta">
                <img class="contenido__pregunta-imagen"
                    src="${pageContext.request.contextPath}/asset/imagenes/edito o elimino recordarios.png"
                    alt="Editar o eliminar recordatorios">
                <h2 class="contenido__pregunta-texto">¿Cómo edito o elimino un recordatorio?</h2>
            </section>
            <section class="contenido__detalle">
                <p class="contenido__detalle-parrafo"> Para modificar o eliminar un recordatorio de pago, sigue estos
                    pasos: </p>
                <ul class="contenido__detalle-lista" type="circle">
                    <li class="contenido__detalle-item">Editar un recordatorio:</li>
                    <ol class="contenido__detalle-lista">
                        <li class="contenido__detalle-item">Ve al módulo "Facturas y Pagos".</li>
                        <li class="contenido__detalle-item">Selecciona "Consultar Facturas".</li>
                        <li class="contenido__detalle-item">Selecciona la factura que deseas editar de la lista.</li>
                        <li class="contenido__detalle-item">Haz clic en "Editar" y modifica los datos (nombre, monto,
                            fecha de vencimiento).</li>
                        <li class="contenido__detalle-item">Valida los cambios y guarda. Las notificaciones asociadas se
                            actualizarán automáticamente.</li>
                    </ol>
                    <li class="contenido__detalle-item">Eliminar un recordatorio:</li>
                    <ol class="contenido__detalle-lista">
                        <li class="contenido__detalle-item">Ve al módulo "Facturas y Pagos".</li>
                        <li class="contenido__detalle-item">Selecciona "Consultar Facturas".</li>
                        <li class="contenido__detalle-item">Selecciona la factura en la lista.</li>
                        <li class="contenido__detalle-item">Haz clic en "Eliminar".</li>
                        <li class="contenido__detalle-item">Confirma la acción (solo se puede eliminar una factura a la
                            vez).</li>
                        <li class="contenido__detalle-item">La eliminación es permanente y no se puede revertir.</li>
                    </ol>
                </ul>
                <p class="contenido__detalle-parrafo"> Solo tú, como propietario, puedes modificar o eliminar tus
                    recordatorios.</p>
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
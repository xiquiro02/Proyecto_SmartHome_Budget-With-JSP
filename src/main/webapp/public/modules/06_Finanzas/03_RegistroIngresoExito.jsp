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
            href="${pageContext.request.contextPath}/asset/css/modules/06_Finanzas/estilosRegistroIngresoExito.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <main class="contenido">
            <div class="contenedor">
                <img class="contenido__icono-img"
                    src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png"
                    alt="Icono verificación">
                <h1 class="contenido__titulo">Ingreso registrado exitosamente</h1>
                <p class="contenido__parrafo">El ingreso fue agregado correctamente y ya se refleja en tu balance
                    financiero.</p>
                <div class="contenido__grupo">
                    <a href="${pageContext.request.contextPath}/public/modules/06_Finanzas/02_RegistrarIngresos.jsp"
                        class="edicion__botones">
                        <button type="button" class="boton boton--editar">Registrar otro ingreso</button>
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/06_Finanzas/08_ResumenFinanciero.jsp"
                        class="edicion__botones">
                        <button type="button" class="boton boton--resumen">Ver resumen financiero</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
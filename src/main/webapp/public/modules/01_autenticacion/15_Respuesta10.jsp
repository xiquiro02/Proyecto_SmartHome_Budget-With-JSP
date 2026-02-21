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
                    src="${pageContext.request.contextPath}/asset/imagenes/signo-de-interrogacion.png" alt="Soporte">
                <h2 class="contenido__pregunta-texto">¿No encontraste tu respuesta?</h2>
            </div>
            <section class="contenido__detalle">
                <p class="contenido__detalle-parrafo">
                    Si ninguna de las respuestas anteriores resolvió tu duda, estamos aquí para ayudarte. Envía un
                    mensaje a nuestro equipo de soporte a través de:
                </p>

                <ul class="contenido__detalle-lista">
                    <li class="contenido__detalle-item">Correo electrónico: "ximenaquiro02@gmail.com" o
                        "soporte@smarthomebudget.com"</li>
                    <li class="contenido__detalle-item">Dentro de la app: Ve a "Ajustes" > "Centro de ayuda y soporte" y
                        describe tu problema.</li>
                    <li class="contenido__detalle-item">Incluye detalles como tu dispositivo y una captura de pantalla
                        si es posible. Responderemos lo antes posible para mejorar tu experiencia.</li>
                </ul>

                <p class="contenido__detalle-parrafo">
                    ¡Gracias por usar SmartHome Budget! Si tienes más preguntas, revisa esta sección o actualiza la app
                    para nuevas funciones.
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
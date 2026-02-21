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
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosnecesitasAyuda.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png"
                alt="Logo de SmartHome Budget">
            <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/01_principal.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
            <div class="encabezado__contenedorTitulo">
                <h1 class="encabezado__titulo">¿Necesitas Ayuda?</h1>
            </div>
        </header>
        <main class="contenido">
            <h2 class="contenido__titulo">Preguntas Frecuentes</h2>
            <div class="contenido__lista-preguntas">

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/06_Respuesta01.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/agregar-usuario.png"
                        alt="Configurar cuenta">
                    <p class="pregunta__texto">¿Cómo configuro mi cuenta?</p>
                </a>

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/07_Respuesta02.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/Eliminar-cuenta.png"
                        alt="Eliminar cuenta">
                    <p class="pregunta__texto">¿Cómo elimino mi cuenta?</p>
                </a>

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/08_Respuesta03.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/recuperar-contrasena.png"
                        alt="Recuperar contraseña">
                    <p class="pregunta__texto">¿Cómo recupero mi contraseña?</p>
                </a>

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/09_Respuesta04.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/metodo-de-pago.png" alt="Pago">
                    <p class="pregunta__texto">¿Cómo agrego mi primer recordatorio de pago?</p>
                </a>

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/10_Respuesta05.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/edito-o-elimino-recordatorios.png"
                        alt="Editar o eliminar recordatorios">
                    <p class="pregunta__texto">¿Cómo edito o elimino un recordatorio?</p>
                </a>

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/11_Respuesta06.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/registro-producto.png"
                        alt="Registro de productos">
                    <p class="pregunta__texto">¿Cómo registro mi primer producto de despensa?</p>
                </a>

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/12_Respuesta07.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/Recordatorios-automaticos.png"
                        alt="Recordatorios automáticos">
                    <p class="pregunta__texto">¿Cómo funcionan los recordatorios automáticos?</p>
                </a>

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/13_Respuesta08.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/factura-como-pagada.png"
                        alt="Factura pagada">
                    <p class="pregunta__texto">¿Puedo marcar una factura como “pagada”?</p>
                </a>

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/14_Respuesta09.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/producto-agotado.png"
                        alt="Producto agotado">
                    <p class="pregunta__texto">¿Cómo sabe la aplicación que un producto está por agotarse?</p>
                </a>

                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/15_Respuesta10.jsp"
                    class="pregunta">
                    <img class="pregunta__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/signo-de-interrogacion.png"
                        alt="Soporte">
                    <p class="pregunta__texto">¿No encontraste tu respuesta? Presiona el botón de abajo para contactar
                        soporte.</p>
                </a>

                <div class="contenido__contenedor-boton">
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/15_Respuesta10.jsp">
                        <button class="boton boton--registrar">Contáctanos</button>
                    </a>
                </div>
            </div>
        </main>

    </body>

    </html>
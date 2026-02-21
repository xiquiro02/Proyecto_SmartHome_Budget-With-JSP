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
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosSolicitarEmail.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/01_principal.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
        </header>
        <main class="contenido">
            <div class="contenedor">
                <h1 class="contenido__titulo">Solicitar Restablecimiento</h1>
                <p class="contenido__parrafo">Introduce tu correo electrónico y te enviaremos un enlace (o código) para
                    restablecer tu contraseña. Sigue las instrucciones del mensaje para crear una nueva contraseña
                    segura.</p>
                <form class="contenido__formulario" method="get" enctype="multipart/form-data">
                    <div class="contenido__grupo">
                        <input type="email" class="contenido__input" id="email" name="email"
                            placeholder="tuemail@gmail.com" required />
                    </div>
                </form>
                <div class="contenido__grupo">
                    <a
                        href="${pageContext.request.contextPath}/public/modules/01_autenticacion/18_ConfirmacionEmail.jsp">
                        <button type="submit" class="boton boton--registrar">Email</button>
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/04_iniciarSesion.jsp">
                        <button type="submit" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
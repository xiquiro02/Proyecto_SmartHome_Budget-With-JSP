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
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosIniciarSesion.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
                <h1 class="contenido__titulo">Inicio Sesión</h1>
                <form class="contenido__formulario" method="post" action="${pageContext.request.contextPath}/Login"
                    onsubmit="return validarCorreoContrasena()">
                    <div class="contenido__grupo">
                        <label for="email" class="contenido__label">Correo</label>
                        <input type="email" class="contenido__input" id="email" name="correo"
                            placeholder="Ingresa tu correo" required />
                    </div>
                    <div class="contenido__grupo">
                        <label for="password" class="contenido__label">Contraseña</label>
                        <input type="password" class="contenido__input" id="password" name="contrasena"
                            placeholder="Ingresa tu contraseña" required />
                    </div>
                    <div class="contenido__grupo--fila">
                        <input type="checkbox" class="contenido__checkbox" id="recordardatos" name="recordardatos" />
                        <label for="recordardatos" class="contenido__label">Recordar los datos</label>
                    </div>

                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/16_OlvidoClave.jsp"
                        class="contenido__link">¿Olvidó su Clave?
                    </a>
                    <div class="contenido__grupo">
                        <button type="submit" class="boton boton--registrar">Ingresar</button>
                    </div>
                </form>
                <script src="${pageContext.request.contextPath}/asset/js/validarCorreoContrasena.js"></script>
            </div>
        </main>
    </body>

    </html>
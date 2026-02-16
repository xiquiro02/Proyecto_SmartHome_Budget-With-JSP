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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/utils/styles.css">
        <link rel="stylesheet"
            href="${pageContext.request.contextPath}/asset/modules/01_autenticacion/estilosIniciarSesion.css">
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
                <form class="contenido__formulario" method="get" enctype="multipart/form-data">
                    <div class="contenido__grupo">
                        <label for="email" class="contenido__label">Correo</label>
                        <input type="email" class="contenido__input" id="email" name="email"
                            placeholder="Ingresa tu correo" required />
                    </div>
                    <div class="contenido__grupo">
                        <label for="password" class="contenido__label">Contraseña</label>
                        <input type="password" class="contenido__input" id="password" name="password"
                            placeholder="Ingresa tu contraseña" required />
                    </div>
                    <div class="contenido__grupo--fila">
                        <input type="checkbox" class="contenido__checkbox" id="recordardatos" name="recordardatos" />
                        <label for="recordardatos" class="contenido__label">Recordar los datos</label>
                    </div>

                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/05_necesitasAyuda.jsp"
                        class="contenido__link">¿Olvidó su Clave?</a>
                </form>
                <div class="contenido__grupo">
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/01_principal.jsp">
                        <button type="submit" class="boton boton--registrar">Ingresar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
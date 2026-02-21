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
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosEliminarCuenta.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <main class="contenido">
            <div class="contenedor">
                <h1 class="contenido__titulo">Eliminar cuenta</h1>
                <p class="contenido__parrafo">Esta acción eliminará tu cuenta de forma permanente. No podrás recuperar
                    tus datos.</p>
                <p class="contenido__parrafos">Para continuar, ingresa tu contraseña:</p>
                <form class="contenido__formulario" method="get" enctype="multipart/form-data">
                    <div class="contenido__grupo">
                        <input type="password" class="contenido__input" id="password" name="password"
                            placeholder="*********************" required />
                    </div>
                </form>
                <p class="contenido__parrafos">¿Estás seguro de que deseas continuar?</p>
                <div class="contenido__grupo">
                    <a
                        href="${pageContext.request.contextPath}/public/modules/01_autenticacion/24_ConfirmarEliminacion.jsp">
                        <button type="submit" class="boton boton--Eliminar">Eliminar cuenta</button>
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp">
                        <button type="submit" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
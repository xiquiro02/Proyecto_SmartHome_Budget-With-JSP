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
            href="${pageContext.request.contextPath}/asset/css/modules/Menuprincipal/estilosCambiarContrasena.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <main class="contenido">
            <div class="contenedor">
                <h1 class="contenido__titulo">Cambiar Contraseña</h1>
                <p class="contenido__parrafo">Por seguridad, crea una nueva contraseña diferente a la anterior.</p>
                <form class="contenido__formulario" method="get" enctype="multipart/form-data">
                    <div class="contenido__grupo">
                        <label for="password" class="contenido__label"> Nueva Contraseña</label>
                        <input type="password" class="contenido__input" id="password" name="password"
                            placeholder="Ingresa tu contraseña" required />
                    </div>
                    <div class="contenido__grupo">
                        <label for="password2" class="contenido__label">Confirmar Contraseña</label>
                        <input type="password" class="contenido__input" id="password2" name="password2"
                            placeholder="Ingresa tu contraseña" required />
                    </div>
                </form>
                <p class="contenido__parrafo">Requisitos mínimos:</p>
                <div class="contenido__grupo--fila">
                    <img class="contenido__icono-img"
                        src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png"
                        alt="Icono de requisitos mínimos">
                    <p class="contenido__label">Debe tener mínimo 8 caracteres</p>
                </div>
                <div class="contenido__grupo--fila">
                    <img class="contenido__icono-img"
                        src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png"
                        alt="Icono de requisitos mínimos">
                    <p class="contenido__label">Incluir una letra mayúscula</p>
                </div>
                <div class="contenido__grupo--fila">
                    <img class="contenido__icono-img"
                        src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png"
                        alt="Icono de requisitos mínimos">
                    <p class="contenido__label">Incluir un símbolo </p>
                </div>
                <div class="contenido__grupo">
                    <a
                        href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/09_ContrasenaConfirmarcambio.jsp">
                        <button type="submit" class="boton boton--registrar">Guardar</button>
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp">
                        <button type="submit" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
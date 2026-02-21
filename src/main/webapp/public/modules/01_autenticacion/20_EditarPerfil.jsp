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
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosEditarPerfil.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <main class="contenido">
            <div class="contenedor">
                <h1 class="contenido__titulo">Editar perfil</h1>
                <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/Icono-de-perfil-de-usuario.png"
                    alt="Icono de Perfil">
                <div class="contenido__contenedor-img">
                    <label for="foto" class="contenido__label">
                        <img class="contenido__foto" src="${pageContext.request.contextPath}/asset/imagenes/Foto_Agregar.png" alt="Foto">
                    </label>
                    <input type="file" class="contenido__imagen" id="foto">
                </div>
                <div class="contenido__grupo">
                    <label for="correo" class="contenido__label">Correo</label>
                    <input type="email" class="contenido__input" id="correo" name="correo" />
                </div>
                <div class="contenido__grupo">
                    <label for="telefono" class="contenido__label">Número de teléfono</label>
                    <input type="tel" class="contenido__input" id="telefono" name="telefono" />
                </div>
                <div class="contenido__grupo">
                    <label for="nombre" class="contenido__label">Usuario</label>
                    <input type="text" class="contenido__input" id="Usuario" name="Usuario" />
                </div>
                <div class="contenido__grupo">
                    <label for="nombre" class="contenido__label">País</label>
                    <input type="text" class="contenido__input" id="País" name="País" />
                </div>
                <div class="contenido__grupo">
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/21_Confirmar-cambio.jsp">
                        <button type="submit" class="boton boton--registrar">Guardar</button>
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/19_MiPerfil.jsp">
                        <button type="submit" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
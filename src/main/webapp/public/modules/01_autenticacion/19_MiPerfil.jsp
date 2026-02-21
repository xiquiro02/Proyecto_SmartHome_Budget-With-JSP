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
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosMiPerfil.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
        </header>
        <main class="contenido">
            <div class="contenedor">
                <h1 class="contenido__titulo">Mi perfil</h1>
                <img class="contenido__icono-img"
                    src="${pageContext.request.contextPath}/asset/imagenes/Icono-de-perfil-de-usuario.png"
                    alt="Icono de Perfil">
                <p class="contenido__parrafo"><strong>Nombre:</strong>Maria Pérez López</p>
                <p class="contenido__parrafo"><strong>Correo:</strong>maria0525perez@gmail.com</p>
                <p class="contenido__parrafo"><strong>Usuario:</strong>@mariap</p>
                <p class="contenido__parrafo"><strong>País:</strong>Colombia</p>
                <p class="contenido__parrafo"><strong>Celular:</strong>+ 57 300 6598773</p>
                <div class="contenido__grupo">
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/20_EditarPerfil.jsp">
                        <button type="submit" class="boton boton--registrar">Editar Perfil</button>
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/22_Cerrar_Sesion.jsp">
                        <button type="submit" class="boton boton--cerrar-sesion">Cerrar sesión</button>
                    </a>
                </div>
            </div>
        </main>
    </body>

    </html>
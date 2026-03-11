<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet"
        href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/asset/css/modules/05_TiendasCercanas/estilosUbicacionManualmente.css">
    <title>SmartHome Budget</title>
</head>

<%
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
        return;
    }
    String errorUbicacion = request.getParameter("error");
%>

<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png"
            alt="Logo de SmartHome Budget">
        <a href="${pageContext.request.contextPath}/Tiendas">
            <span class="material-symbols-outlined"> arrow_back_ios_new </span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Ubicación</h1>
        </div>
    </header>

    <main class="confirmacion">
        <div class="confirmacion__contenedor">
            <div class="confirmacion__img">
                <img class="confirmacion__icono" src="${pageContext.request.contextPath}/asset/imagenes/lapiz.png"
                    alt="Icono de confirmacion">
            </div>
            <p class="confirmacion__titulo">Ingresar ubicación</p>

            <% if ("campos_vacios".equals(errorUbicacion)) { %>
            <p style="color: red; font-size: 0.9em;">Por favor completa todos los campos.</p>
            <% } %>

            <!-- CORRECCIÓN: formulario real que envía al servlet -->
            <form action="${pageContext.request.contextPath}/Tiendas" method="POST">
                <input type="hidden" name="accion" value="guardarUbicacion">

                <div class="resumen">
                    <div class="resumen__campo">
                        <img class="resumen__icono"
                            src="${pageContext.request.contextPath}/asset/imagenes/paseo-por-la-ciudad.png"
                            alt="País">
                        <div class="resumen__info">
                            <label class="resumen__etiqueta">País:</label>
                            <input type="text" name="pais" class="resumen__input" placeholder="Ej: Colombia" required>
                        </div>
                    </div>

                    <div class="resumen__campo">
                        <img class="resumen__icono" src="${pageContext.request.contextPath}/asset/imagenes/pueblo.png"
                            alt="Ciudad">
                        <div class="resumen__info">
                            <label class="resumen__etiqueta">Ciudad:</label>
                            <input type="text" name="ciudad" class="resumen__input" placeholder="Ej: Bucaramanga" required>
                        </div>
                    </div>

                    <div class="resumen__campo">
                        <img class="resumen__icono"
                            src="${pageContext.request.contextPath}/asset/imagenes/direccion.png" alt="Dirección">
                        <div class="resumen__info">
                            <label class="resumen__etiqueta">Dirección:</label>
                            <input type="text" name="direccion" class="resumen__input" placeholder="Ej: Cl. 45 #29-24" required>
                        </div>
                    </div>
                </div>

                <button type="submit" class="boton boton--registrar">Guardar ubicación</button>
            </form>

            <a href="${pageContext.request.contextPath}/Tiendas" class="confirmacion__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </div>
    </main>
</body>

</html>

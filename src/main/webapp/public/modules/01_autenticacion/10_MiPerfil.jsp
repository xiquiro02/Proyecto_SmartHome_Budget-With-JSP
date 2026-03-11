<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    com.smarthome.smarthome_budget.modelo.Usuario usuario =
        (com.smarthome.smarthome_budget.modelo.Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosMiPerfil.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
    </header>
    <main class="contenido">
        <div class="contenedor">
            <h1 class="contenido__titulo">Mi perfil</h1>

            <%-- Foto de perfil --%>
            <% if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) { %>
                <img class="contenido__icono-img" src="${pageContext.request.contextPath}<%= usuario.getFotoPerfil() %>" alt="Foto de perfil" style="border-radius:50%;object-fit:cover;">
            <% } else { %>
                <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/Icono-de-perfil-de-usuario.png" alt="Perfil">
            <% } %>

            <p class="contenido__parrafo"><strong>Nombre:</strong> <%= usuario.getNombreUsuario() %> <%= usuario.getPrimerApellido() %>
                <% if (usuario.getSegundoApellido() != null && !usuario.getSegundoApellido().isEmpty()) { %> <%= usuario.getSegundoApellido() %><% } %>
            </p>
            <p class="contenido__parrafo"><strong>Correo:</strong> <%= usuario.getCorreo() %></p>
            <p class="contenido__parrafo"><strong>Teléfono:</strong> <%= usuario.getTelefono() %></p>

            <div class="contenido__grupo">
                <a href="${pageContext.request.contextPath}/Perfil?accion=editar">
                    <button type="button" class="boton boton--registrar">Editar Perfil</button>
                </a>
                <a href="${pageContext.request.contextPath}/Perfil?accion=cerrarSesion">
                    <button type="button" class="boton boton--cerrar-sesion">Cerrar sesión</button>
                </a>
            </div>
        </div>
    </main>
</body>
</html>

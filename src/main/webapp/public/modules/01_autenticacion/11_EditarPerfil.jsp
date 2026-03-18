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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosEditarPerfil.css">
    <script src="${pageContext.request.contextPath}/asset/js/editarPerfil.js"></script>
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="editar-perfil-container">
            <h1 class="contenido__titulo">Editar perfil</h1>

            <%-- Foto actual --%>
            <div class="contenedor-foto">
                <% if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) { %>
                    <img class="contenido__icono-img" id="previewFoto" src="${pageContext.request.contextPath}<%= usuario.getFotoPerfil() %>" alt="Foto de perfil" data-src-original="${pageContext.request.contextPath}<%= usuario.getFotoPerfil() %>">
                <% } else { %>
                    <img class="contenido__icono-img" id="previewFoto" src="${pageContext.request.contextPath}/asset/imagenes/Icono-de-perfil-de-usuario.png" alt="Foto de perfil" data-src-original="${pageContext.request.contextPath}/asset/imagenes/Icono-de-perfil-de-usuario.png">
                <% } %>
                <button type="button" class="btn-agregar-foto" onclick="document.getElementById('foto').click()">+</button>
            </div>
            
            <div style="font-size:12px;color:#666;margin-bottom:20px;text-align:center;">Foto de perfil (PNG/JPG, máx 2MB)</div>

            <c:if test="${not empty error}">
                <div style="padding:10px;background:#ffe0e0;border-radius:8px;color:#c00;margin-bottom:12px;">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/Perfil" enctype="multipart/form-data">
                <input type="hidden" name="accion" value="guardarEdicion">
                <input type="file" class="contenido__imagen" id="foto" name="foto" accept="image/png,image/jpeg,image/jpg">

                <div class="contenido__grupo">
                    <label for="correo" class="contenido__label">Correo</label>
                    <input type="email" class="contenido__input" id="correo" name="correo"
                        value="<%= usuario.getCorreo() %>" required>
                </div>

                <div class="contenido__grupo">
                    <label for="telefono" class="contenido__label">Número de teléfono</label>
                    <input type="tel" class="contenido__input" id="telefono" name="telefono"
                        value="<%= usuario.getTelefono() %>">
                </div>

                <div class="contenido__grupo">
                    <label for="Usuario" class="contenido__label">Nombre de usuario</label>
                    <input type="text" class="contenido__input" id="Usuario" name="Usuario"
                        value="<%= usuario.getNombreUsuario() %>" required>
                </div>

                <div class="contenido__grupo botones-container">
                    <button type="submit" class="boton boton--registrar">Guardar</button>
                    <a href="${pageContext.request.contextPath}/Perfil" onclick="resetearPreview();">
                        <button type="button" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </form>
        </div>
    </main>
</body>
</html>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    if (session.getAttribute("usuario") == null) {
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosEliminarCuenta.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
            <h1 class="contenido__titulo">Eliminar cuenta</h1>
            <p class="contenido__parrafo">Esta acción eliminará tu cuenta de forma permanente. No podrás recuperar tus datos.</p>

            <c:if test="${not empty error}">
                <div style="padding:10px;background:#ffe0e0;border-radius:8px;color:#c00;margin:12px 0;">${error}</div>
            </c:if>

            <p class="contenido__parrafos">Para continuar, ingresa tu contraseña:</p>

            <form method="post" action="${pageContext.request.contextPath}/Perfil">
                <input type="hidden" name="accion" value="confirmarEliminar">
                <div class="contenido__grupo">
                    <input type="password" class="contenido__input" name="password" placeholder="Contraseña actual" required>
                </div>
                <p class="contenido__parrafos">¿Estás seguro de que deseas continuar?</p>
                <div class="contenido__grupo">
                    <button type="submit" class="boton boton--Eliminar">Eliminar cuenta</button>
                    <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp">
                        <button type="button" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </form>
        </div>
    </main>
</body>
</html>

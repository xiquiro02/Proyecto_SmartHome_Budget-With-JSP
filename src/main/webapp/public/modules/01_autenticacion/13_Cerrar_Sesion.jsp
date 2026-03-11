<%@ page contentType="text/html" pageEncoding="UTF-8" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosCerrar_Sesion.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
            <h1 class="contenido__titulo">Cerrar Sesión</h1>
            <p class="contenido__parrafo">¿Seguro que deseas salir?</p>
            <p class="contenido__parrafo">¡Gracias por visitarnos! Hasta pronto 👋</p>
            <form method="post" action="${pageContext.request.contextPath}/Perfil">
                <input type="hidden" name="accion" value="confirmarCerrarSesion">
                <div class="contenido__grupo">
                    <button type="submit" class="boton boton--registrar">Cerrar Sesión</button>
                    <a href="${pageContext.request.contextPath}/Perfil">
                        <button type="button" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </form>
        </div>
    </main>
</body>
</html>

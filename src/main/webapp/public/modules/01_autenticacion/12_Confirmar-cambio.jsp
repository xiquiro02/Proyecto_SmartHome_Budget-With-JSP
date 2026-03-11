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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosConfirmar-cambio.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
            <h1 class="contenido__titulo">Cambios guardados</h1>
            <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="Confirmación">
            <p class="contenido__parrafo">Tu perfil fue actualizado correctamente.</p>
            <div class="contenido__grupo">
                <a href="${pageContext.request.contextPath}/Perfil">
                    <button type="button" class="boton boton--registrar">Ver mi perfil</button>
                </a>
            </div>
        </div>
    </main>
</body>
</html>

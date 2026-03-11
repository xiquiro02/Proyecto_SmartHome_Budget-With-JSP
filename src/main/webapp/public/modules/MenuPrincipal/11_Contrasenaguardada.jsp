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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/Menuprincipal/estilosContrasenaguardada.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
            <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="Verificación">
            <h1 class="contenido__titulo">Contraseña guardada exitosamente</h1>
            <p class="contenido__parrafo">Ya puedes iniciar sesión con tu nueva contraseña.</p>
            <div class="contenido__grupo">
                <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp">
                    <button type="button" class="boton boton--registrar">Aceptar</button>
                </a>
            </div>
        </div>
    </main>
</body>
</html>

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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/05_Finanzas/estilosRegistroIngresoExito.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
            <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="Verificación">
            <h1 class="contenido__titulo">Ingreso registrado exitosamente</h1>
            <p class="contenido__parrafo">El ingreso fue agregado correctamente y ya se refleja en tu balance financiero.</p>
            <div class="contenido__grupo">
                <a href="${pageContext.request.contextPath}/Finanzas?accion=formIngreso" class="edicion__botones">
                    <button type="button" class="boton boton--editar">Registrar otro ingreso</button>
                </a>
                <a href="${pageContext.request.contextPath}/Finanzas?accion=resumen" class="edicion__botones">
                    <button type="button" class="boton boton--resumen">Ver resumen financiero</button>
                </a>
            </div>
        </div>
    </main>
</body>
</html>

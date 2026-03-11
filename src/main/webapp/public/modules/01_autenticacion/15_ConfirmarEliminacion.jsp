<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosConfirmarEliminacion.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
            <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/Eliminacion.png" alt="Eliminación">
            <h1 class="contenido__titulo">Cuenta eliminada correctamente</h1>
            <p class="contenido__parrafo">Tu cuenta y todos los datos asociados fueron eliminados de forma permanente.</p>
            <p class="contenido__parrafos">Gracias por haber usado SmartHome Budget. Esperamos verte nuevamente.</p>
            <div class="contenido__grupo">
                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/01_principal.jsp">
                    <button type="button" class="boton boton--registrar">Aceptar</button>
                </a>
            </div>
        </div>
    </main>
</body>
</html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosConfirmacionEdicionLista.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenido">
    <div class="contenedor">
        <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="OK">
        <h1 class="contenido__titulo">Cambios guardados</h1>
        <p class="contenido__parrafo">Los cambios en la lista <strong>"${nombreLista}"</strong> se guardaron correctamente.</p>
        <div class="contenido__grupo">
            <a href="${pageContext.request.contextPath}/Listas" class="edicion__botones">
                <button type="button" class="boton boton--volver">Ver mis listas</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

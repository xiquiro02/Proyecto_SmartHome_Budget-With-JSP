<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosCambiosGuardados.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenido">
    <div class="contenedor">
        <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="OK">
        <h1 class="contenido__titulo">Cambios guardados con éxito</h1>
        <p class="contenido__parrafo">El producto <strong>"${nombreProducto}"</strong> se actualizó correctamente. Tu inventario ahora está al día.</p>
        <div class="contenido__grupo">
            <a href="${pageContext.request.contextPath}/Inventario?accion=consultar">
                <button class="boton boton--registrar">Ver inventario</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

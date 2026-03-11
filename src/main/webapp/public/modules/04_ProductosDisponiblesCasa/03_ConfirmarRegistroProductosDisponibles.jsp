<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosConfirmarRegistroProductosDisponibles.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenido">
    <div class="contenedor">
        <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="OK">
        <h1 class="contenido__titulo">
            <c:choose>
                <c:when test="${fueActualizado}">Cantidad actualizada en el inventario</c:when>
                <c:otherwise>Producto registrado con éxito</c:otherwise>
            </c:choose>
        </h1>
        <p class="contenido__parrafo">
            <strong>"${nombreProducto}"</strong> fue
            <c:choose>
                <c:when test="${fueActualizado}">actualizado (cantidad: +${cantidad})</c:when>
                <c:otherwise>agregado al inventario (cantidad: ${cantidad})</c:otherwise>
            </c:choose>.
        </p>
        <p class="contenido__parrafo">Recuerda revisar tus productos con frecuencia.</p>
        <div class="contenido__grupo">
            <a href="${pageContext.request.contextPath}/Inventario?accion=registrar">
                <button class="boton boton--editar">Añadir otro producto</button>
            </a>
            <a href="${pageContext.request.contextPath}/Inventario">
                <button class="boton boton--volver">Volver</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

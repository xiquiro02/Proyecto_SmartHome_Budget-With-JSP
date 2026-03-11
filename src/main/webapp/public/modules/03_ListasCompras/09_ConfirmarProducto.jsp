<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosConfirmarProducto.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="confirmacion">
    <div class="confirmacion__contenedor">
        <div class="confirmacion__img">
            <img class="confirmacion__icono" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="OK">
        </div>
        <p class="confirmacion__titulo">
            <c:choose>
                <c:when test="${fueActualizado}">Cantidad actualizada en la lista</c:when>
                <c:otherwise>Producto agregado correctamente</c:otherwise>
            </c:choose>
        </p>
        <div class="resumen">
            <div class="resumen__campo">
                <img class="resumen__icono" src="${pageContext.request.contextPath}/asset/imagenes/elementos-de-la-lista.png" alt="">
                <div class="resumen__info">
                    <label class="resumen__etiqueta">Nombre:</label>
                    <input type="text" class="resumen__input" value="${nombreProducto}" readonly>
                </div>
            </div>
            <div class="resumen__campo">
                <img class="resumen__icono" src="${pageContext.request.contextPath}/asset/imagenes/compensacion.png" alt="">
                <div class="resumen__info">
                    <label class="resumen__etiqueta">Cantidad:</label>
                    <input type="text" class="resumen__input" value="${cantidad}" readonly>
                </div>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/Listas?accion=agregarProducto&id=${idLista}" class="confirmacion__botones">
            <button class="boton boton--registrar">Añadir otro producto</button>
        </a>
        <a href="${pageContext.request.contextPath}/Listas?accion=verDetalle&id=${idLista}" class="confirmacion__botones">
            <button class="boton boton--volver">Ver lista</button>
        </a>
    </div>
</main>
</body>
</html>

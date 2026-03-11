<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosEliminarProducto.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenido">
    <div class="contenedor">
        <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/eliminar.png" alt="Eliminar">
        <h1 class="contenido__titulo">Eliminar producto</h1>
        <p class="contenido__parrafo">¿Deseas eliminar <strong>"${detalle.nombreProducto}"</strong> de la lista?</p>
        <p class="contenido__parrafo">Esta acción no se puede deshacer.</p>
        <div class="contenido__grupo">
            <form action="${pageContext.request.contextPath}/Listas" method="post">
                <input type="hidden" name="accion" value="eliminarProducto">
                <input type="hidden" name="idDetalle" value="${detalle.idDetalleLista}">
                <input type="hidden" name="idLista" value="${idLista}">
                <button type="submit" class="boton boton--Eliminar">Sí, eliminar</button>
            </form>
            <a href="${pageContext.request.contextPath}/Listas?accion=editar&id=${idLista}">
                <button class="boton boton--cancelar">No, conservar</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

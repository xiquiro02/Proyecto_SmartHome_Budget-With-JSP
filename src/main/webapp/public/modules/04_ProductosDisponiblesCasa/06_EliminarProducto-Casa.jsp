<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosEliminarProductoCasa.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenido">
    <div class="contenedor">
        <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/eliminar.png" alt="Eliminar">
        <h1 class="contenido__titulo">Eliminar producto</h1>
        <p class="contenido__parrafo">Estás a punto de eliminar del inventario:</p>
        <p class="contenido__parrafo"><strong>Producto:</strong> ${inv.nombreProducto}</p>
        <p class="contenido__parrafo"><strong>Categoría:</strong> ${inv.nombreTipoProducto}</p>
        <p class="contenido__parrafo"><strong>Cantidad actual:</strong> ${inv.cantidad}</p>
        <p class="contenido__parrafo">❗ Esta acción no se puede deshacer.</p>
        <div class="contenido__grupo">
            <form action="${pageContext.request.contextPath}/Inventario" method="post">
                <input type="hidden" name="accion" value="eliminar">
                <input type="hidden" name="idInventario" value="${inv.idInventario}">
                <button type="submit" class="boton boton--Eliminar">Sí, eliminar</button>
            </form>
            <a href="${pageContext.request.contextPath}/Inventario?accion=consultar">
                <button class="boton boton--cancelar">No, conservar</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

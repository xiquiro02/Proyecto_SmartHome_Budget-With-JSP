<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosProductoEliminado.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenido">
    <div class="contenedor">
        <h1 class="contenido__titulo">Producto eliminado con éxito</h1>
        <p class="contenido__parrafo"><strong>"${nombreProducto}"</strong> fue eliminado del inventario. Tu lista de productos disponibles se ha actualizado.</p>
        <div class="contenido__grupo">
            <a href="${pageContext.request.contextPath}/Inventario?accion=consultar">
                <button class="boton boton--registrar">Ver inventario</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosProductosPendientes-Todos.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenido">
    <div class="contenedor">
        <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/ComprasPendientes.png" alt="">
        <h1 class="contenido__titulo">Todos los productos marcados como pendientes</h1>
        <p class="contenido__parrafo">La lista volvió al estado <strong>Pendiente</strong>.</p>
        <div class="contenido__grupo">
            <a href="${pageContext.request.contextPath}/Listas?accion=verDetalle&id=${idLista}">
                <button class="boton boton--editar">Ver lista</button>
            </a>
            <a href="${pageContext.request.contextPath}/Listas">
                <button class="boton boton--volver">Mis listas</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

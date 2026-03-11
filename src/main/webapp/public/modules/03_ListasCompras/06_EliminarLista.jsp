<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosEliminarLista.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenido">
    <div class="contenedor">
        <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/eliminar.png" alt="Eliminar">
        <h1 class="contenido__titulo">Eliminar lista</h1>
        <p class="contenido__parrafo">¿Estás seguro de que deseas eliminar <strong>"${lista.nombreLista}"</strong>?</p>
        <p class="contenido__parrafo">Esta acción eliminará también todos sus productos. No se puede deshacer.</p>
        <div class="contenido__grupo">
            <form action="${pageContext.request.contextPath}/Listas" method="post">
                <input type="hidden" name="accion" value="eliminar">
                <input type="hidden" name="idLista" value="${lista.idListaCompras}">
                <button type="submit" class="boton boton--Eliminar">Sí, eliminar</button>
            </form>
            <a href="${pageContext.request.contextPath}/Listas">
                <button class="boton boton--cancelar">No, conservar</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

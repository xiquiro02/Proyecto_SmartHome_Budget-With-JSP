<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosConfirmacionProducto.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenido">
    <div class="contenedor">
        <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="OK">
        <h1 class="contenido__titulo">Producto actualizado correctamente</h1>
        <p class="contenido__parrafo">Los cambios se guardaron exitosamente.</p>
        <div class="contenido__grupo">
            <a href="${pageContext.request.contextPath}/Listas?accion=editar&id=${idLista}" class="edicion__botones">
                <button class="boton boton--editar">Seguir editando</button>
            </a>
            <a href="${pageContext.request.contextPath}/Listas" class="edicion__botones">
                <button class="boton boton--volver">Volver</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

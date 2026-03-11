<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosEditarProducto.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Listas?accion=editar&id=${idLista}">
        <span class="material-symbols-outlined">arrow_back_ios_new</span>
    </a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Editar producto</h1></div>
</header>
<main class="formulario">
    <div class="formulario__contenedor">
        <form action="${pageContext.request.contextPath}/Listas" method="post">
            <input type="hidden" name="accion" value="actualizarProducto">
            <input type="hidden" name="idDetalle" value="${detalle.idDetalleLista}">
            <input type="hidden" name="idLista" value="${idLista}">

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Nombre del producto:</label>
                <input type="text" class="formulario__input" value="${detalle.nombreProducto}" readonly
                       style="background:#f0f0f0; color:#666">
                <small style="color:#888">El nombre no puede cambiarse; para cambiar el nombre, elimina y agrega de nuevo.</small>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Cantidad: * (solo enteros &gt; 0)</label>
                <input type="number" name="cantidad" class="formulario__input"
                       value="${detalle.cantidad}" min="1" max="999" required>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Tipo:</label>
                <input type="text" class="formulario__input" value="${detalle.tipoProducto}" readonly
                       style="background:#f0f0f0; color:#666">
            </div>

            <c:if test="${param.error == 'cantidad_invalida'}">
                <div class="mensaje mensaje--error">⚠️ La cantidad debe ser un entero mayor a 0.</div>
            </c:if>

            <div class="formulario__botones">
                <button type="submit" class="boton boton--registrar">Guardar cambios</button>
            </div>
            <a href="${pageContext.request.contextPath}/Listas?accion=editar&id=${idLista}" class="formulario__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </div>
</main>
</body>
</html>

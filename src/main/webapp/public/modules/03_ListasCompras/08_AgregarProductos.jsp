<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosAgregarProductos.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Listas?accion=verDetalle&id=${lista.idListaCompras}">
        <span class="material-symbols-outlined">arrow_back_ios_new</span>
    </a>
    <div class="encabezado__contenedorTitulo">
        <h1 class="encabezado__titulo">Agregar producto</h1>
        <p style="font-size:0.85rem; color:#666; margin:0;">Lista: <strong>${lista.nombreLista}</strong></p>
    </div>
</header>
<main class="formulario">
    <div class="formulario__contenedor">
        <c:if test="${not empty error}">
            <div class="mensaje mensaje--error">⚠️ ${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/Listas" method="post">
            <input type="hidden" name="accion" value="guardarProducto">
            <input type="hidden" name="idLista" value="${lista.idListaCompras}">

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Nombre del producto: *</label>
                <input type="text" name="nombreProducto" class="formulario__input"
                       placeholder="Ej: Pan integral" maxlength="100" required>
                <small style="color:#888">Si el producto ya existe en la lista, se sumará la cantidad.</small>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Cantidad: * (solo números enteros &gt; 0)</label>
                <input type="number" name="cantidad" class="formulario__input"
                       min="1" max="999" value="1" required>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Tipo de producto (opcional):</label>
                <select name="idTipoProducto" class="formulario__select">
                    <option value="">Seleccionar tipo</option>
                    <c:forEach var="tipo" items="${tiposProducto}">
                        <option value="${tipo.idTipoProducto}">${tipo.nombreTipoProducto}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="formulario__etiquetas">
                <div class="etiqueta etiqueta--aseo"><img src="${pageContext.request.contextPath}/asset/imagenes/Aseo.png" alt=""><span>Aseo</span></div>
                <div class="etiqueta etiqueta--alimentos"><img src="${pageContext.request.contextPath}/asset/imagenes/alimentos-saludables.png" alt=""><span>Alimentos</span></div>
                <div class="etiqueta etiqueta--personal"><img src="${pageContext.request.contextPath}/asset/imagenes/maquillaje.png" alt=""><span>Personal</span></div>
                <div class="etiqueta etiqueta--otros"><img src="${pageContext.request.contextPath}/asset/imagenes/cajas.png" alt=""><span>Otros</span></div>
                <div class="etiqueta etiqueta--ropa-calzado"><img src="${pageContext.request.contextPath}/asset/imagenes/Ropa-calzado.jpg" alt=""><span>Ropa</span></div>
            </div>

            <div class="formulario__botones">
                <button type="submit" class="boton boton--registrar">Guardar producto</button>
            </div>
            <a href="${pageContext.request.contextPath}/Listas" class="formulario__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </div>
</main>
</body>
</html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosRegistrarProductosDisponibles.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Inventario"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Registrar productos</h1></div>
</header>
<main class="formulario">
    <div class="formulario__contenedor">
        <c:if test="${not empty error}">
            <div class="mensaje mensaje--error">⚠️ ${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/Inventario" method="post">
            <input type="hidden" name="accion" value="guardar">

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Nombre del producto: *</label>
                <input type="text" name="nombreProducto" class="formulario__input"
                       placeholder="Ej: Leche, Pan, Huevos..." maxlength="100" required>
                <small style="color:#888">Si el producto ya está en el inventario, se sumará la cantidad.</small>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Cantidad disponible: * (entero &gt; 0)</label>
                <input type="number" name="cantidad" class="formulario__input"
                       min="1" max="9999" value="1" required>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Categoría:</label>
                <select name="idTipoProducto" class="formulario__select">
                    <option value="">Seleccionar Categoría</option>
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

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Descripción (opcional):</label>
                <textarea name="descripcion" class="formulario__textarea" rows="3"
                          placeholder="Agrega detalles sobre este producto..."></textarea>
            </div>

            <div class="formulario__botones">
                <button type="submit" class="boton boton--registrar">Registrar producto</button>
            </div>
            <a href="${pageContext.request.contextPath}/Inventario" class="formulario__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </div>
</main>
</body>
</html>

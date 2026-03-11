<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosMoverProductosAgotados.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Inventario"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Productos agotados</h1></div>
</header>
<main class="contenedorProductosAgotados">
    <div class="descripcion">
        <p class="descripcion__texto">Revisa los productos agotados y agrégalos a una lista de compras.</p>
    </div>

    <c:choose>
        <c:when test="${empty agotados}">
            <div style="text-align:center; padding:2rem; color:#666">
                <p>🎉 ¡No tienes productos agotados en este momento!</p>
                <a href="${pageContext.request.contextPath}/Inventario">
                    <button class="boton boton--volver">Volver</button>
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="gridProductos">
                <c:forEach var="inv" items="${agotados}">
                    <article class="tarjetaProducto tarjetaProducto--${inv.colorCSS}">
                        <img src="${pageContext.request.contextPath}/asset/imagenes/${inv.iconoProducto}"
                             alt="${inv.nombreProducto}" class="tarjetaProducto__icono">
                        <div class="tarjetaProducto__info">
                            <h3 class="tarjetaProducto__nombre">${inv.nombreProducto}</h3>
                            <p class="tarjetaProducto__detalle">Categoría: ${inv.nombreTipoProducto}</p>
                            <p class="tarjetaProducto__detalle">Cantidad: 0</p>
                        </div>
                        <div class="tarjetaProducto__acciones">
                            <a href="${pageContext.request.contextPath}/Inventario?accion=moverALista&id=${inv.idInventario}">
                                <button class="botonAccion botonAccion--mover">🛒 Mover a lista</button>
                            </a>
                        </div>
                    </article>
                </c:forEach>
            </div>

            <div class="resumen">
                <p class="resumen__numero">${agotados.size()} productos agotados</p>
                <p class="resumen__descripcion">Listos para agregar a tus listas</p>
            </div>

            <a href="${pageContext.request.contextPath}/Inventario" class="boton-NuevoProducto">
                <button class="boton boton--cancelar">Volver</button>
            </a>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>

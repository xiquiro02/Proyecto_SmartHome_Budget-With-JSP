<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosFiltroCategorias-ConsultarInventario.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Inventario?accion=consultar"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Consultar inventario</h1></div>
</header>
<main class="contenedorInventario">
    <div class="filtros">
        <p class="filtros__titulo">Filtrar por:</p>
        <div class="filtro-grupo">
            <a href="${pageContext.request.contextPath}/Inventario?accion=filtroTipo" class="filtro-enlace">
                Categoría <img class="filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
            </a>
            <a href="${pageContext.request.contextPath}/Inventario?accion=filtroCantidad" class="filtro-enlace">
                Cantidad <img class="filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
            </a>
        </div>
    </div>

    <div class="gridProductos">
        <c:forEach var="inv" items="${inventario}">
            <article class="tarjetaProducto tarjetaProducto--${inv.colorCSS}">
                <img src="${pageContext.request.contextPath}/asset/imagenes/${inv.iconoProducto}"
                     alt="${inv.nombreProducto}" class="tarjetaProducto__icono">
                <div class="tarjetaProducto__info">
                    <h3 class="tarjetaProducto__nombre">${inv.nombreProducto}</h3>
                    <p class="tarjetaProducto__detalle">Categoría: ${inv.nombreTipoProducto}</p>
                    <p class="tarjetaProducto__detalle">Cantidad: ${inv.cantidad}</p>
                    <p class="tarjetaProducto__detalle">
                        Fecha: <fmt:formatDate value="${inv.fechaRegistro}" pattern="dd MMM yyyy" type="date"/>
                    </p>
                    <c:if test="${inv.stockBajo && !inv.agotado}"><p class="tarjetaProducto__alerta">⚠️ Stock bajo</p></c:if>
                    <c:if test="${inv.agotado}"><p class="tarjetaProducto__alerta tarjetaProducto__alerta--rojo">🚨 Agotado</p></c:if>
                </div>
                <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
                <div class="tarjetaProducto__acciones">
                    <a href="${pageContext.request.contextPath}/Inventario?accion=editar&id=${inv.idInventario}">
                        <button class="botonAccion botonAccion--editar">✏️ Editar</button></a>
                    <a href="${pageContext.request.contextPath}/Inventario?accion=confirmarEliminar&id=${inv.idInventario}">
                        <button class="botonAccion botonAccion--eliminar">🗑️ Eliminar</button></a>
                </div>
                </c:if>
            </article>
        </c:forEach>
    </div>

    <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
    <a href="${pageContext.request.contextPath}/Inventario?accion=registrar" class="boton-NuevoProducto">
        <button class="boton boton--cancelar">+ Nuevo producto</button>
    </a>
    </c:if>

    <%-- Panel filtro categoría --%>
    <div class="panelFiltro">
        <div class="filtroCategoria">
            <h3 class="filtroCategoria__titulo">Seleccionar categoría</h3>
            <c:forEach var="tipo" items="${tiposProducto}">
                <label class="filtroCategoria__opcion">
                    <input type="radio" name="categoriaFiltro"
                           value="${tipo.idTipoProducto}"
                           onchange="window.location='${pageContext.request.contextPath}/Inventario?accion=filtroTipo&tipo=${tipo.idTipoProducto}'"
                           ${tipoSeleccionado == tipo.idTipoProducto.toString() ? 'checked' : ''}>
                    <span class="filtroCategoria__opcion-texto">${tipo.nombreTipoProducto}</span>
                </label>
            </c:forEach>
            <a href="${pageContext.request.contextPath}/Inventario?accion=consultar" class="filtroCategoria__boton">
                <button class="boton boton--cancelar">Ver todos</button>
            </a>
        </div>
    </div>
</main>
</body>
</html>

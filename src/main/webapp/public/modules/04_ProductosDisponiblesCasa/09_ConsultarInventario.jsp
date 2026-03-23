<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosConsultarInventario.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Inventario"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Consultar inventario</h1></div>
</header>

<main class="contenedorInventario">

    <%-- Mensajes de estado --%>
    <c:if test="${not empty param.exito_auto}">
        <div class="mensajeExitoAuto">✅ ${param.exito_auto}</div>
    </c:if>
    <c:if test="${param.error == 'sin_permiso'}">
        <div class="mensaje mensaje--error">⚠️ No tienes permiso para realizar esa acción.</div>
    </c:if>
    <c:if test="${param.error == 'no_encontrado'}">
        <div class="mensaje mensaje--error">⚠️ Producto no encontrado.</div>
    </c:if>

    <div class="filtros">
        <p class="filtros__titulo">Filtrar por:</p>
        <div class="filtro-grupo">
            <a href="${pageContext.request.contextPath}/Inventario?accion=filtroTipo"
               class="filtro-enlace ${param.accion == 'filtroTipo' ? 'filtro-enlace--activo' : ''}">
                Categoría <img class="filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
            </a>
            <a href="${pageContext.request.contextPath}/Inventario?accion=filtroCantidad"
               class="filtro-enlace ${param.accion == 'filtroCantidad' ? 'filtro-enlace--activo' : ''}">
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
                    <p class="tarjetaProducto__detalle">Cantidad: <fmt:formatNumber value="${inv.cantidad}" maxFractionDigits="2" groupingUsed="false"/></p>
                    <p class="tarjetaProducto__detalle">Fecha: ${inv.fechaActualizacionFormateada}</p>

                    <c:if test="${inv.stockBajo && !inv.agotado}">
                        <p class="tarjetaProducto__alerta">⚠️ Stock bajo</p>
                    </c:if>
                    <c:if test="${inv.agotado}">
                        <p class="tarjetaProducto__alerta tarjetaProducto__alerta--rojo">🚨 Agotado</p>
                    </c:if>
                </div>

                <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
                    <div class="tarjetaProducto__acciones">
                        <a href="${pageContext.request.contextPath}/Inventario?accion=editar&id=${inv.idInventario}">
                            <button class="botonAccion botonAccion--editar">✏️ Editar</button>
                        </a>
                        <a href="${pageContext.request.contextPath}/Inventario?accion=confirmarEliminar&id=${inv.idInventario}">
                            <button class="botonAccion botonAccion--eliminar">🗑️ Eliminar</button>
                        </a>
                        <c:if test="${inv.stockBajo || inv.agotado}">
                            <a href="${pageContext.request.contextPath}/Inventario?accion=autoAnadirALista&idProducto=${inv.idInventario}"
                               class="acciones__lista">
                                <button class="botonAccion botonAccion--lista">📋 Mover a lista de compras</button>
                            </a>
                        </c:if>
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

    <%-- Panel de filtro por categoría --%>
    <c:if test="${param.accion == 'filtroTipo'}">
        <div class="panelFiltro ${not empty tipoSeleccionado ? 'panelFiltro--oculto' : ''}">
            <form action="${pageContext.request.contextPath}/Inventario" method="get" class="filtroCategoria">
                <input type="hidden" name="accion" value="filtroTipo">
                <h3 class="filtroCategoria__titulo">Seleccionar categoría</h3>
                <div class="filtroCategoria__opciones">
                    <c:forEach var="tipo" items="${tiposProducto}">
                        <label class="filtroCategoria__opcion">
                            <input type="radio" name="tipo" value="${tipo.idTipoProducto}"
                                   ${tipoSeleccionado == tipo.idTipoProducto.toString() ? 'checked' : ''}>
                            <span class="filtroCategoria__opcion-texto">${tipo.nombreTipoProducto}</span>
                        </label>
                    </c:forEach>
                </div>
                <div class="filtroCategoria__acciones">
                    <button type="submit" class="boton boton--aplicar">Aplicar filtro</button>
                    <a href="${pageContext.request.contextPath}/Inventario?accion=consultar" class="enlace-ver-todas">Ver todos</a>
                </div>
            </form>
        </div>
    </c:if>

    <%-- Panel de filtro por cantidad --%>
    <c:if test="${param.accion == 'filtroCantidad'}">
        <div class="panelFiltro ${not empty ordenSeleccionado ? 'panelFiltro--oculto' : ''}">
            <form action="${pageContext.request.contextPath}/Inventario" method="get" class="filtroEstado">
                <input type="hidden" name="accion" value="filtroCantidad">
                <h3 class="filtroEstado__titulo">Filtrar por cantidad</h3>
                <div class="filtroEstado__opciones">
                    <label class="filtroEstado__opcion">
                        <input type="radio" name="orden" value="menorIgual2" ${ordenSeleccionado == 'menorIgual2' ? 'checked' : ''}>
                        <div class="filtroEstado__opcion-contenido"><span class="filtroEstado__opcion-titulo">Menor o igual a 2 (stock bajo)</span></div>
                    </label>
                    <label class="filtroEstado__opcion">
                        <input type="radio" name="orden" value="mayorIgual10" ${ordenSeleccionado == 'mayorIgual10' ? 'checked' : ''}>
                        <div class="filtroEstado__opcion-contenido"><span class="filtroEstado__opcion-titulo">Mayor o igual a 10</span></div>
                    </label>
                    <label class="filtroEstado__opcion">
                        <input type="radio" name="orden" value="asc" ${ordenSeleccionado == 'asc' ? 'checked' : ''}>
                        <div class="filtroEstado__opcion-contenido"><span class="filtroEstado__opcion-titulo">Ordenar de menor a mayor</span></div>
                    </label>
                    <label class="filtroEstado__opcion">
                        <input type="radio" name="orden" value="desc" ${ordenSeleccionado == 'desc' ? 'checked' : ''}>
                        <div class="filtroEstado__opcion-contenido"><span class="filtroEstado__opcion-titulo">Ordenar de mayor a menor</span></div>
                    </label>
                </div>
                <div class="filtroEstado__acciones">
                    <button type="submit" class="boton boton--aplicar">Aplicar filtro</button>
                    <a href="${pageContext.request.contextPath}/Inventario?accion=consultar" class="enlace-ver-todas">Ver todos</a>
                </div>
            </form>
        </div>
    </c:if>

</main>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
(function () {
    const params = new URLSearchParams(window.location.search);
    const msg = params.get('exito_auto');
    if (msg) {
        Swal.fire({
            icon: 'success', title: '¡Listo!',
            text: decodeURIComponent(msg),
            timer: 3000, showConfirmButton: false,
            toast: true, position: 'top-end'
        });
        const url = new URL(window.location.href);
        url.searchParams.delete('exito_auto');
        history.replaceState(null, '', url.toString());
    }
})();
</script>
</body>
</html>

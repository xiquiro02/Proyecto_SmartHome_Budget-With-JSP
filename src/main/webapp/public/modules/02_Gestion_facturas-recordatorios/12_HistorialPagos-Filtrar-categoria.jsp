<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosHistorialPagos-Filtrar-categoria.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas?accion=historial"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
        <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Historial de Pagos</h1></div>
    </header>
    <main class="consultarFacturas">
        <div class="consultarFacturas__filtros">
            <div class="consultarFacturas__filtro-grupo">
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialCategoria" class="consultarFacturas__filtro-enlace consultarFacturas__filtro-enlace--activo">Categoría</a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialFecha" class="consultarFacturas__filtro-enlace">Fecha</a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialMonto" class="consultarFacturas__filtro-enlace">Monto</a>
            </div>
        </div>
        <div class="facturaLista">
            <c:forEach var="f" items="${historial}">
                <div class="facturaCard facturaCard--verde">
                    <div class="facturaCard__borde"></div>
                    <div class="facturaCard__contenido">
                        <h3 class="facturaCard__titulo">${f.nombreFactura}</h3>
                        <p class="facturaCard__detalles">Monto: <fmt:formatNumber value="${f.monto}" type="currency" currencySymbol="$"/></p>
                        <p class="facturaCard__detalles">Pagado: <fmt:formatDate value="${f.fechaPago}" pattern="dd MMM yyyy" type="date"/></p>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty historial}"><p class="consultarFacturas__vacio">No hay pagos para esa categoría.</p></c:if>
        </div>
        <div class="totalPagado">
            <h3 class="totalPagado__titulo">Total pagado</h3>
            <p class="totalPagado__monto"><fmt:formatNumber value="${totalPagado}" type="currency" currencySymbol="$"/></p>
        </div>
        <div class="panelFiltro">
            <div class="filtroCategoria">
                <h3 class="filtroCategoria__titulo">Seleccionar categoría</h3>
                <c:forEach var="cat" items="${categorias}">
                    <a href="${pageContext.request.contextPath}/Facturas?accion=historialCategoria&idCategoria=${cat.IDCategoriaEgreso}">
                        <label class="filtroCategoria__opcion">
                            <input type="radio" ${categoriaFiltro == cat.IDCategoriaEgreso ? 'checked' : ''}>
                            <span class="filtroCategoria__opcion-texto">${cat.NombreCategoriaEgreso}</span>
                        </label>
                    </a>
                </c:forEach>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historial"><button class="boton boton--cancelar">Ver todas</button></a>
            </div>
        </div>
    </main>
</body>
</html>

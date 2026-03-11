<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosHistorialPagos-Filtrar-Monto.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas?accion=historial"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
        <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Historial de Pagos</h1></div>
    </header>
    <main class="consultarFacturas">
        <div class="facturaLista">
            <c:forEach var="f" items="${historial}">
                <div class="facturaCard facturaCard--verde"><div class="facturaCard__borde"></div>
                    <div class="facturaCard__contenido">
                        <h3 class="facturaCard__titulo">${f.nombreFactura}</h3>
                        <p class="facturaCard__detalles">Monto: <fmt:formatNumber value="${f.monto}" type="currency" currencySymbol="$"/></p>
                        <p class="facturaCard__detalles">Pagado: <fmt:formatDate value="${f.fechaPago}" pattern="dd MMM yyyy" type="date"/></p>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty historial}"><p class="consultarFacturas__vacio">No hay pagos en ese rango.</p></c:if>
        </div>
        <div class="totalPagado">
            <p class="totalPagado__monto"><fmt:formatNumber value="${totalPagado}" type="currency" currencySymbol="$"/></p>
        </div>
        <div class="panelEstadoFiltro">
            <div class="filtroEstado">
                <h3 class="filtroEstado__titulo">Filtrar por monto</h3>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialMonto&rango=menor50">
                    <label class="filtroEstado__opcion">
                        <input type="radio" ${rangoFiltro == 'menor50' ? 'checked' : ''}><span class="filtroEstado__opcion-titulo">Menor a $50.000</span>
                    </label>
                </a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialMonto&rango=50a150">
                    <label class="filtroEstado__opcion">
                        <input type="radio" ${rangoFiltro == '50a150' ? 'checked' : ''}><span class="filtroEstado__opcion-titulo">Entre $50.000 y $150.000</span>
                    </label>
                </a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialMonto&rango=mayor200">
                    <label class="filtroEstado__opcion">
                        <input type="radio" ${rangoFiltro == 'mayor200' ? 'checked' : ''}><span class="filtroEstado__opcion-titulo">Mayor a $200.000</span>
                    </label>
                </a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historial"><button class="boton boton--cancelar">Ver todas</button></a>
            </div>
        </div>
    </main>
</body>
</html>

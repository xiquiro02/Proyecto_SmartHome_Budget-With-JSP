<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosHistorialPagos.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
        <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Historial de Pagos</h1></div>
    </header>
    <main class="consultarFacturas">
        <div class="consultarFacturas__descripcion">
            <p class="consultarFacturas__parrafo">Aquí puedes ver todas las facturas marcadas como pagadas, con su fecha y monto correspondiente.</p>
        </div>
        <div class="consultarFacturas__filtros">
            <p class="consultarFacturas__filtro-label">Filtrar por:</p>
            <div class="consultarFacturas__filtro-grupo">
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialCategoria" class="consultarFacturas__filtro-enlace">Categoría <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialFecha" class="consultarFacturas__filtro-enlace">Fecha <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialMonto" class="consultarFacturas__filtro-enlace">Monto <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
            </div>
        </div>

        <div class="facturaLista">
            <c:choose>
                <c:when test="${empty historial}">
                    <p class="consultarFacturas__vacio">No tienes facturas pagadas aún.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="f" items="${historial}">
                        <div class="facturaCard facturaCard--verde">
                            <div class="facturaCard__borde"></div>
                            <div class="facturaCard__contenido">
                                <div class="facturaCard__encabezado">
                                    <h3 class="facturaCard__titulo">${f.nombreFactura}</h3>
                                    <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">Pagada</span>
                                </div>
                                <p class="facturaCard__detalles">Categoría: ${f.nombreCategoria}</p>
                                <p class="facturaCard__detalles">Monto: <fmt:formatNumber value="${f.monto}" type="currency" currencySymbol="$"/></p>
                                <p class="facturaCard__detalles">
                                    Pagado el: <fmt:formatDate value="${f.fechaPago}" pattern="dd MMM yyyy" type="date"/>
                                </p>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="totalPagado">
            <h3 class="totalPagado__titulo">Total pagado</h3>
            <p class="totalPagado__monto"><fmt:formatNumber value="${totalPagado}" type="currency" currencySymbol="$"/></p>
            <p class="totalPagado__texto">${cantidadPagadas} factura(s) procesada(s)</p>
        </div>

        <a href="${pageContext.request.contextPath}/public/modules/06_Finanzas/08_ResumenFinanciero.jsp" class="consultarFacturas__boton">
            <button class="boton boton--resumen">Ver resumen financiero</button>
        </a>
        <a href="${pageContext.request.contextPath}/Facturas" class="consultarFacturas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>

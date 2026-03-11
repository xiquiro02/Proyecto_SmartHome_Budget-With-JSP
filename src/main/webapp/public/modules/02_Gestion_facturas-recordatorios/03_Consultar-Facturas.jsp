<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosConsultar-Facturas.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Consultar Facturas</h1>
        </div>
    </header>

    <main class="consultarFacturas">
        <div class="consultarFacturas__filtros">
            <p class="consultarFacturas__filtro-label">Filtrar por:</p>
            <div class="consultarFacturas__filtro-grupo">
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroCategoria" class="consultarFacturas__filtro-enlace">
                    Categoría <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
                </a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroFecha" class="consultarFacturas__filtro-enlace">
                    Fecha <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
                </a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroEstado" class="consultarFacturas__filtro-enlace">
                    Estado <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
                </a>
            </div>
        </div>

        <c:if test="${param.exito == 'registrada'}">
            <div class="mensaje mensaje--exito">✅ Factura registrada correctamente.</div>
        </c:if>
        <c:if test="${param.exito == 'pagada'}">
            <div class="mensaje mensaje--exito">✅ Factura marcada como pagada.</div>
        </c:if>
        <c:if test="${param.exito == 'eliminada'}">
            <div class="mensaje mensaje--exito">✅ Factura eliminada.</div>
        </c:if>

        <div class="facturaLista">
            <c:choose>
                <c:when test="${empty facturas}">
                    <p class="consultarFacturas__vacio">No hay facturas registradas aún.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="f" items="${facturas}">
                        <%-- Color de la tarjeta según estado --%>
                        <c:set var="color" value="naranja"/>
                        <c:if test="${f.estadoPago == 'Pagada'}"><c:set var="color" value="verde"/></c:if>
                        <c:if test="${f.estadoPago == 'Vencida'}"><c:set var="color" value="rojo"/></c:if>

                        <div class="facturaCard facturaCard--${color}">
                            <div class="facturaCard__borde"></div>
                            <div class="facturaCard__contenido">
                                <div class="facturaCard__encabezado">
                                    <h3 class="facturaCard__titulo">
                                        ${f.nombreFactura} -
                                        <fmt:formatNumber value="${f.monto}" type="currency" currencySymbol="$"/>
                                    </h3>
                                </div>
                                <p class="facturaCard__fecha">
                                    Categoría: ${f.nombreCategoria}
                                </p>
                                <p class="facturaCard__fecha">
                                    Vence: ${f.fechaVencimientoFormateada}
                                </p>
                                <div class="facturaCard__estadoAcciones">
                                    <span class="facturaCard__etiqueta facturaCard__etiqueta--${f.estadoPago == 'Pagada' ? 'pagada' : f.estadoPago == 'Vencida' ? 'vencida' : 'pendiente'}">
                                        ${f.estadoPago}
                                    </span>
                                    <div class="facturaCard__acciones">
                                        <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
                                            <a href="${pageContext.request.contextPath}/Facturas?accion=editar&id=${f.idEgresos}">
                                                <button class="boton boton--editar">✏️ Editar</button>
                                            </a>
                                            <c:if test="${f.estadoPago != 'Pagada'}">
                                            <form action="${pageContext.request.contextPath}/Facturas" method="post" style="display:inline">
                                                <input type="hidden" name="accion" value="marcarPagada">
                                                <input type="hidden" name="idEgreso" value="${f.idEgresos}">
                                                <button type="submit" class="boton boton--registrar">✅ Pagar</button>
                                            </form>
                                            </c:if>
                                            <a href="${pageContext.request.contextPath}/Facturas?accion=confirmarEliminar&id=${f.idEgresos}">
                                                <button class="boton boton--Eliminar">🗑️ Eliminar</button>
                                            </a>
                                        </c:if>
                                        <%-- INVITADO solo puede ver --%>
                                        <c:if test="${sessionScope.idRol == 3}">
                                            <span class="facturaCard__readonly">Solo lectura</span>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
        <a href="${pageContext.request.contextPath}/Facturas?accion=form" class="consultarFacturas__boton">
            <button class="boton boton--cancelar">+ Nueva factura</button>
        </a>
        </c:if>
    </main>
</body>
</html>

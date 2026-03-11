<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosHistorialPagos-Filtrar-Fecha.css">
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
            <c:if test="${empty historial}"><p class="consultarFacturas__vacio">No hay pagos para ese período.</p></c:if>
        </div>
        <div class="totalPagado">
            <p class="totalPagado__monto"><fmt:formatNumber value="${totalPagado}" type="currency" currencySymbol="$"/></p>
        </div>
        <div class="panelMesFiltro">
            <form action="${pageContext.request.contextPath}/Facturas" method="get">
                <input type="hidden" name="accion" value="historialFecha">
                <div class="filtroMes">
                    <h3 class="filtroMes__titulo">Seleccionar mes</h3>
                    <div class="filtroMes__gridMeses">
                        <c:forEach begin="1" end="12" var="m">
                            <button type="submit" name="mes" value="${m}" class="filtroMes__mes">
                                <c:choose>
                                    <c:when test="${m==1}">Enero</c:when><c:when test="${m==2}">Febrero</c:when>
                                    <c:when test="${m==3}">Marzo</c:when><c:when test="${m==4}">Abril</c:when>
                                    <c:when test="${m==5}">Mayo</c:when><c:when test="${m==6}">Junio</c:when>
                                    <c:when test="${m==7}">Julio</c:when><c:when test="${m==8}">Agosto</c:when>
                                    <c:when test="${m==9}">Septiembre</c:when><c:when test="${m==10}">Octubre</c:when>
                                    <c:when test="${m==11}">Noviembre</c:when><c:otherwise>Diciembre</c:otherwise>
                                </c:choose>
                            </button>
                        </c:forEach>
                    </div>
                    <select name="anio" class="filtroMes__anio-select" onchange="this.form.submit()">
                        <c:forEach begin="2020" end="2030" var="y">
                            <option value="${y}" ${anioFiltro == y ? 'selected' : ''}>${y}</option>
                        </c:forEach>
                    </select>
                </div>
            </form>
        </div>
    </main>
</body>
</html>

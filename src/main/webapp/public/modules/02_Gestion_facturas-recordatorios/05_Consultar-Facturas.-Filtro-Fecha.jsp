<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosConsultar-Facturas.-Filtro-Fecha.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas?accion=lista"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
        <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Consultar Facturas</h1></div>
    </header>
    <main class="consultarFacturas">
        <div class="consultarFacturas__filtros">
            <p class="consultarFacturas__filtro-label">Filtrar por:</p>
            <div class="consultarFacturas__filtro-grupo">
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroCategoria" class="consultarFacturas__filtro-enlace">Categoría <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroFecha" class="consultarFacturas__filtro-enlace consultarFacturas__filtro-enlace--activo">Fecha <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroEstado" class="consultarFacturas__filtro-enlace">Estado <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
            </div>
        </div>
        <div class="facturaLista">
            <c:forEach var="f" items="${facturas}">
                <c:set var="color" value="naranja"/>
                <c:if test="${f.estadoPago == 'Pagada'}"><c:set var="color" value="verde"/></c:if>
                <c:if test="${f.estadoPago == 'Vencida'}"><c:set var="color" value="rojo"/></c:if>
                <div class="facturaCard facturaCard--${color}">
                    <div class="facturaCard__borde"></div>
                    <div class="facturaCard__contenido">
                        <h3 class="facturaCard__titulo">${f.nombreFactura} - <fmt:formatNumber value="${f.monto}" type="currency" currencySymbol="$"/></h3>
                        <p class="facturaCard__fecha">Vence: <fmt:formatDate value="${f.fechaVencimiento}" pattern="dd MMM yyyy" type="date"/></p>
                        <span class="facturaCard__etiqueta facturaCard__etiqueta--${f.estadoPago == 'Pagada' ? 'pagada' : f.estadoPago == 'Vencida' ? 'vencida' : 'pendiente'}">${f.estadoPago}</span>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty facturas}"><p class="consultarFacturas__vacio">No hay facturas para el mes seleccionado.</p></c:if>
        </div>
        <div class="panelMesFiltro">
            <form action="${pageContext.request.contextPath}/Facturas" method="get">
                <input type="hidden" name="accion" value="filtroFecha">
                <div class="filtroMes">
                    <h3 class="filtroMes__titulo">Seleccionar mes</h3>
                    <div class="filtroMes__gridMeses">
                        <c:forEach begin="1" end="12" var="m">
                            <button type="submit" name="mes" value="${m}"
                                class="filtroMes__mes ${mesFiltro == m ? 'filtroMes__mes--activo' : ''}">
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
                    <div class="filtroMes__anio">
                        <label class="filtroMes__anio-label">Año</label>
                        <select name="anio" class="filtroMes__anio-select" onchange="this.form.submit()">
                            <c:forEach begin="2020" end="2030" var="y">
                                <option value="${y}" ${anioFiltro == y ? 'selected' : ''}>${y}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </form>
        </div>
    </main>
</body>
</html>

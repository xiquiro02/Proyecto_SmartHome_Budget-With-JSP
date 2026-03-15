<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
        return;
    }
    Integer idRol = (Integer) session.getAttribute("idRol");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/05_Finanzas/estilosDetalleEgresos.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Finanzas">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Detalle de Egresos</h1>
        </div>
    </header>

    <main class="consultarFacturas">

        <c:choose>
            <c:when test="${empty egresos}">
                <div style="text-align:center;padding:40px 20px;color:#888;">
                    <p>No hay egresos registrados.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="egreso" items="${egresos}">
                <div class="facturaLista">
                    <div class="facturaCard facturaCard--rojo">
                        <div class="facturaCard__borde"></div>
                        <div class="facturaCard__contenido">
                            <div class="facturaCard__encabezado">
                                <h3 class="facturaCard__titulo">
                                    <c:choose>
                                        <c:when test="${not empty egreso.nombreFactura}">${egreso.nombreFactura}</c:when>
                                        <c:otherwise>${egreso.nombreCategoria}</c:otherwise>
                                    </c:choose>
                                </h3>
                                <span class="facturaCard__etiqueta facturaCard__etiqueta--pendiente">
                                    $ <fmt:formatNumber value="${egreso.monto}" pattern="#,##0.00"/>
                                </span>
                            </div>
                            <p class="facturaCard__detalles">
                                <fmt:formatDate value="${egreso.fechaVencimiento}" pattern="dd MMM yyyy" type="date"/>
                                — ${egreso.nombreCategoria}
                            </p>
                        </div>
                    </div>
                </div>
                </c:forEach>

                <div class="totalPagado">
                    <h3 class="totalPagado__titulo">Total egresos</h3>
                    <p class="totalPagado__monto">$ <fmt:formatNumber value="${totalEgresos}" pattern="#,##0.00"/></p>
                </div>
            </c:otherwise>
        </c:choose>

        <% if (idRol == null || idRol != 3) { %>
        <a href="${pageContext.request.contextPath}/Finanzas?accion=formEgreso" class="consultarFacturas__boton">
            <button class="boton boton--registrar">+ Registrar nuevo egreso</button>
        </a>
        <% } %>
        <a href="${pageContext.request.contextPath}/Finanzas" class="consultarFacturas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/05_Finanzas/estilosDetalleIngresos.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Finanzas">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Detalle de Ingresos</h1>
        </div>
    </header>

    <main class="consultarFacturas">

        <c:choose>
            <c:when test="${empty ingresos}">
                <div style="text-align:center;padding:40px 20px;color:#888;">
                    <p>No hay ingresos registrados este mes.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="ingreso" items="${ingresos}">
                <div class="facturaLista">
                    <div class="facturaCard facturaCard--verde">
                        <div class="facturaCard__borde"></div>
                        <div class="facturaCard__contenido">
                            <div class="facturaCard__encabezado">
                                <h3 class="facturaCard__titulo">${ingreso.nombreCategoria}</h3>
                                <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">
                                    $ <fmt:formatNumber value="${ingreso.monto}" pattern="#,##0.00"/>
                                </span>
                            </div>
                            <p class="facturaCard__detalles">
                                <fmt:formatDate value="${ingreso.fechaIngreso}" pattern="dd MMM yyyy" type="date"/>
                                <c:if test="${not empty ingreso.descripcion}"> — ${ingreso.descripcion}</c:if>
                            </p>
                        </div>
                    </div>
                </div>
                </c:forEach>

                <div class="totalPagado">
                    <h3 class="totalPagado__titulo">Total ingresos</h3>
                    <p class="totalPagado__monto">$ <fmt:formatNumber value="${totalIngresos}" pattern="#,##0.00"/></p>
                </div>
            </c:otherwise>
        </c:choose>

        <a href="${pageContext.request.contextPath}/Finanzas?accion=formIngreso" class="consultarFacturas__boton">
            <button class="boton boton--registrar">+ Registrar nuevo ingreso</button>
        </a>
        <a href="${pageContext.request.contextPath}/Finanzas" class="consultarFacturas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>

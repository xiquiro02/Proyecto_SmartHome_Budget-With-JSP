<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
        return;
    }
    Integer idRol = (Integer) session.getAttribute("idRol");
    if (idRol != null && idRol == 3) {
        response.sendRedirect(request.getContextPath() + "/Menu?error=sin_permiso_finanzas");
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

        <%-- Mensajes de resultado --%>
        <c:if test="${param.exito == 'ingreso_eliminado'}">
            <div class="mensaje mensaje--exito" style="margin-bottom:10px">✅ Ingreso eliminado correctamente.</div>
        </c:if>
        <c:if test="${param.error == 'sin_permiso'}">
            <div class="mensaje mensaje--error" style="margin-bottom:10px">⚠️ No tienes permiso para esa acción.</div>
        </c:if>

        <c:choose>
            <c:when test="${empty ingresos}">
                <div style="text-align:center;padding:40px 20px;color:#888;">
                    <p>No hay ingresos registrados.</p>
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
                                ${ingreso.fechaIngresoFormateada}
                                <c:if test="${not empty ingreso.descripcion}"> — ${ingreso.descripcion}</c:if>
                            </p>

                            <%--
                                CONTROL DE ROLES:
                                Rol 1 (Admin) → puede editar Y eliminar
                                Rol 2 (Adulto) → solo puede ver (ni editar ni eliminar)
                            --%>
                            <% if (idRol != null && idRol == 1) { %>
                            <div style="display:flex;gap:8px;margin-top:6px;">
                                <a href="${pageContext.request.contextPath}/Finanzas?accion=editarIngreso&id=${ingreso.idIngresos}">
                                    <button class="botonAccion botonAccion--editar" style="font-size:12px;padding:4px 10px">✏️ Editar</button>
                                </a>
                                <form action="${pageContext.request.contextPath}/Finanzas" method="post"
                                      style="display:inline"
                                      onsubmit="return confirm('¿Eliminar este ingreso? Esta acción no se puede revertir.')">
                                    <input type="hidden" name="accion"    value="eliminarIngreso">
                                    <input type="hidden" name="idIngreso" value="${ingreso.idIngresos}">
                                    <button type="submit" class="botonAccion botonAccion--eliminar" style="font-size:12px;padding:4px 10px">🗑️ Eliminar</button>
                                </form>
                            </div>
                            <% } %>
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

        <%--
            Registro de ingreso: accesible desde aquí para Rol 1 y Rol 2.
            Rol 2 puede crear pero no editar/eliminar.
        --%>
        <a href="${pageContext.request.contextPath}/Finanzas?accion=formIngreso" class="consultarFacturas__boton">
            <button class="boton boton--registrar">+ Registrar nuevo ingreso</button>
        </a>
        <a href="${pageContext.request.contextPath}/Finanzas" class="consultarFacturas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>

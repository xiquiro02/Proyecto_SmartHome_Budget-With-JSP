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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/05_Finanzas/estilosFinanzas.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <div class="encabezado__contenedor">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/finanzas.png" alt="Finanzas">
            <h1 class="encabezado__titulo">Finanzas</h1>
        </div>
        <div class="encabezado__contenido">
            <p class="encabezado__descripcion">Administra tus ingresos, gastos y presupuesto mensual en un solo lugar.</p>
        </div>
    </header>

    <c:if test="${param.error == 'sin_permiso'}">
        <div style="margin:10px 20px;padding:10px;background:#ffe0e0;border-radius:8px;color:#c00;text-align:center;">
            No tienes permiso para realizar esa acción.
        </div>
    </c:if>

    <main class="finanzas">

        <%-- Resumen rápido si viene del servlet --%>
        <c:if test="${not empty totalIngresos}">
        <div style="background:#f0f4ff;border-radius:12px;padding:14px 18px;margin-bottom:12px;display:flex;gap:16px;justify-content:space-around;flex-wrap:wrap;">
            <div style="text-align:center;">
                <p style="margin:0;font-size:12px;color:#666;">Ingresos del mes</p>
                <p style="margin:0;font-weight:bold;color:#2e7d32;font-size:15px;">$ <fmt:formatNumber value="${totalIngresos}" pattern="#,##0.00"/></p>
            </div>
            <div style="text-align:center;">
                <p style="margin:0;font-size:12px;color:#666;">Egresos del mes</p>
                <p style="margin:0;font-weight:bold;color:#c62828;font-size:15px;">$ <fmt:formatNumber value="${totalEgresos}" pattern="#,##0.00"/></p>
            </div>
            <c:if test="${not empty presupuesto}">
            <div style="text-align:center;">
                <p style="margin:0;font-size:12px;color:#666;">Presupuesto disponible</p>
                <p style="margin:0;font-weight:bold;color:#1565c0;font-size:15px;">$ <fmt:formatNumber value="${presupuesto.disponible}" pattern="#,##0.00"/></p>
            </div>
            </c:if>
        </div>
        </c:if>

        <div class="tarjetaAccion tarjetaAccion--azul">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono" src="${pageContext.request.contextPath}/asset/imagenes/Resumen-financiero.png" alt="Resumen">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Resumen financiero</h3>
                    <p class="tarjetaAccion__subtitulo">Visualiza ingresos, gastos y el dinero disponible.</p>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/Finanzas?accion=resumen">
                <button class="boton boton--editar">Ver resumen</button>
            </a>
        </div>

        <div class="tarjetaAccion tarjetaAccion--verde">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono tarjetaAccion__icono--verde" src="${pageContext.request.contextPath}/asset/imagenes/ingresos-pasivos.png" alt="Ingresos">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Registrar ingresos</h3>
                    <p class="tarjetaAccion__subtitulo">Añade el dinero que recibes y mantén actualizado tu balance.</p>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/Finanzas?accion=detalleIngresos">
                <button class="boton boton--agregar">Ver ingresos</button>
            </a>
        </div>

        <div class="tarjetaAccion tarjetaAccion--rojo">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono tarjetaAccion__icono--rojo" src="${pageContext.request.contextPath}/asset/imagenes/Registrar-egresos.png" alt="Egresos">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Registrar egresos</h3>
                    <p class="tarjetaAccion__subtitulo">Registra tus gastos y controla en qué se va tu dinero.</p>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/Finanzas?accion=detalleEgresos">
                <button class="boton boton--Eliminar">Ver egresos</button>
            </a>
        </div>

        <% if (idRol == null || idRol != 3) { %>
        <div class="tarjetaAccion tarjetaAccion--morado">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono tarjetaAccion__icono--morado" src="${pageContext.request.contextPath}/asset/imagenes/Presupuesto-mensual.png" alt="Presupuesto">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Presupuesto mensual</h3>
                    <p class="tarjetaAccion__subtitulo">Define un límite de gasto y recibe alertas si te acercas o lo superas.</p>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/Finanzas?accion=formPresupuesto">
                <button class="boton boton--resumen">Configurar presupuesto</button>
            </a>
        </div>
        <% } %>

        <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp" class="finanzas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>

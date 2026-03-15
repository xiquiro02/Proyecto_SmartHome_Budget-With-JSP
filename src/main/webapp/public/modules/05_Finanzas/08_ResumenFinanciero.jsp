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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/05_Finanzas/estilosResumenFinanciero.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Finanzas">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Resumen Financiero</h1>
        </div>
    </header>

    <main class="resumenFinanciero">
        <div class="resumenFinanciero__introduccion">
            <img class="introduccion__icono" src="${pageContext.request.contextPath}/asset/imagenes/estado-financiero.png" alt="Finanzas">
            <div class="introduccion__contenido">
                <h2 class="introduccion__titulo">Resumen Financiero</h2>
                <p class="introduccion__texto">Revisa de forma rápida cómo está tu dinero este mes.</p>
            </div>
        </div>

        <%-- Alerta de presupuesto si aplica --%>
        <c:if test="${not empty presupuesto}">
            <c:if test="${presupuesto.superado}">
            <div style="background:#ffebee;border-left:4px solid #c62828;padding:12px 16px;border-radius:8px;margin-bottom:12px;">
                ⚠️ <strong>¡Presupuesto superado!</strong> Has gastado más del 100% del límite del mes.
            </div>
            </c:if>
            <c:if test="${!presupuesto.superado && presupuesto.cercaDelLimite}">
            <div style="background:#fff8e1;border-left:4px solid #f57f17;padding:12px 16px;border-radius:8px;margin-bottom:12px;">
                ⚠️ <strong>Atención:</strong> Has alcanzado el ${presupuesto.porcentajeUsado}% de tu presupuesto mensual.
            </div>
            </c:if>
        </c:if>

        <div class="tarjetasResumen">
            <div class="resumenCard resumenCard--verde">
                <div class="resumenCard__borde"></div>
                <div class="resumenCard__contenido">
                    <div class="resumenCard__contenedor">
                        <div class="resumenCard__encabezado">
                            <span class="resumenCard__icono">📈</span>
                            <h3 class="resumenCard__titulo">Ingresos totales</h3>
                        </div>
                        <p class="resumenCard__monto">$ <fmt:formatNumber value="${totalIngresos}" pattern="#,##0.00"/></p>
                    </div>
                    <div class="resumenCard__detalles">
                        <a href="${pageContext.request.contextPath}/Finanzas?accion=detalleIngresos" class="resumenCard__enlace">👁️ Ver detalles</a>
                    </div>
                </div>
            </div>

            <div class="resumenCard resumenCard--azul">
                <div class="resumenCard__borde"></div>
                <div class="resumenCard__contenedor">
                    <div class="resumenCard__encabezado">
                        <span class="resumenCard__icono">💵</span>
                        <h3 class="resumenCard__titulo">Dinero disponible</h3>
                    </div>
                    <p class="resumenCard__monto">$ <fmt:formatNumber value="${disponible}" pattern="#,##0.00"/></p>
                </div>
            </div>

            <div class="resumenCard resumenCard--rojo">
                <div class="resumenCard__borde"></div>
                <div class="resumenCard__contenido">
                    <div class="resumenCard__contenedor">
                        <div class="resumenCard__encabezado">
                            <span class="resumenCard__icono">📉</span>
                            <h3 class="resumenCard__titulo">Egresos totales</h3>
                        </div>
                        <p class="resumenCard__monto">$ <fmt:formatNumber value="${totalEgresos}" pattern="#,##0.00"/></p>
                    </div>
                    <div class="resumenCard__detalles">
                        <a href="${pageContext.request.contextPath}/Finanzas?accion=detalleEgresos" class="resumenCard__enlace">👁️ Ver detalles</a>
                    </div>
                </div>
            </div>
        </div>

        <%-- Gráfico de barras dinámico --%>
        <c:set var="maxVal" value="${totalIngresos > totalEgresos ? totalIngresos : totalEgresos}"/>
        <c:if test="${maxVal > 0}">
        <div class="graficoFinanciero">
            <div class="graficoFinanciero__encabezado">
                <span class="graficoFinanciero__icono">📊</span>
                <h3 class="graficoFinanciero__titulo">Gráfico Financiero</h3>
            </div>
            <div class="graficoFinanciero__contenedor">
                <div class="grafico">
                    <div class="grafico__columna">
                        <c:set var="hIng" value="${(totalIngresos / maxVal) * 150}"/>
                        <div class="grafico__barra grafico__barra--verde" style="height:${hIng}px;">
                            <span class="grafico__monto">$ <fmt:formatNumber value="${totalIngresos}" pattern="#,##0"/></span>
                        </div>
                    </div>
                    <div class="grafico__columna">
                        <c:set var="hEgr" value="${(totalEgresos / maxVal) * 150}"/>
                        <div class="grafico__barra grafico__barra--rojo" style="height:${hEgr}px;">
                            <span class="grafico__monto">$ <fmt:formatNumber value="${totalEgresos}" pattern="#,##0"/></span>
                        </div>
                    </div>
                    <c:if test="${disponible > 0}">
                    <div class="grafico__columna">
                        <c:set var="hDis" value="${(disponible / maxVal) * 150}"/>
                        <div class="grafico__barra grafico__barra--azul" style="height:${hDis}px;">
                            <span class="grafico__monto">$ <fmt:formatNumber value="${disponible}" pattern="#,##0"/></span>
                        </div>
                    </div>
                    </c:if>
                </div>
                <div class="grafico__etiquetas">
                    <span class="grafico__etiqueta">Ingresos</span>
                    <span class="grafico__etiqueta">Egresos</span>
                    <c:if test="${disponible > 0}"><span class="grafico__etiqueta">Disponible</span></c:if>
                </div>
            </div>
        </div>
        </c:if>

        <%-- Presupuesto mensual si existe --%>
        <c:if test="${not empty presupuesto}">
        <div style="background:#f8f9ff;border-radius:12px;padding:16px;margin-top:12px;">
            <h3 style="margin:0 0 10px;font-size:15px;">Presupuesto de ${presupuesto.nombreMes}</h3>
            <div style="background:#e0e0e0;border-radius:8px;height:12px;overflow:hidden;">
                <c:set var="pct" value="${presupuesto.porcentajeUsado > 100 ? 100 : presupuesto.porcentajeUsado}"/>
                <div style="height:100%;width:${pct}%;background:${presupuesto.superado ? '#c62828' : (presupuesto.cercaDelLimite ? '#f57f17' : '#2e7d32')};border-radius:8px;"></div>
            </div>
            <p style="margin:6px 0 0;font-size:13px;color:#555;">
                ${presupuesto.porcentajeUsado}% usado —
                Límite: $ <fmt:formatNumber value="${presupuesto.montoMax}" pattern="#,##0.00"/>
            </p>
        </div>
        </c:if>

        <a href="${pageContext.request.contextPath}/Finanzas" class="resumenFinanciero__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>

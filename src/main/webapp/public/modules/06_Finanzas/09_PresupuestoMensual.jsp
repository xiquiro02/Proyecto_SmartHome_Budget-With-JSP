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
        response.sendRedirect(request.getContextPath() + "/Finanzas?error=sin_permiso");
        return;
    }
    java.time.LocalDate hoy = java.time.LocalDate.now();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/06_Finanzas/estilosPresupuestoMensual.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Finanzas">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Presupuesto Mensual</h1>
        </div>
    </header>

    <main class="presupuestoMensual">
        <div class="presupuestoMensual__introduccion">
            <img class="introduccion__icono" src="${pageContext.request.contextPath}/asset/imagenes/PresupuestoMES.png" alt="Presupuesto">
            <div class="introduccion__contenido">
                <h2 class="introduccion__titulo">Presupuesto Mensual</h2>
                <p class="introduccion__texto">Define cuánto deseas gastar al mes para mantener el control de tu dinero.</p>
            </div>
        </div>

        <c:if test="${not empty error}">
            <div style="margin:10px 0;padding:10px;background:#ffe0e0;border-radius:8px;color:#c00;">${error}</div>
        </c:if>

        <form class="formularioPresupuesto" method="post" action="${pageContext.request.contextPath}/Finanzas">
            <input type="hidden" name="accion" value="guardarPresupuesto">

            <div class="formularioPresupuesto__campo">
                <label class="formularioPresupuesto__label" for="montoMax">Presupuesto máximo del mes</label>
                <input type="number" id="montoMax" name="montoMax" class="formularioPresupuesto__input"
                    placeholder="0.00" step="0.01" min="0" required
                    value="${not empty presupuesto ? presupuesto.montoMax : ''}">
            </div>

            <div class="formularioPresupuesto__campo">
                <label class="formularioPresupuesto__label" for="mes">Mes a aplicar</label>
                <select id="mes" name="mes" class="formularioPresupuesto__select" required>
                    <option value="">Seleccionar mes</option>
                    <c:forEach begin="1" end="12" var="m">
                        <c:set var="meses" value="Enero,Febrero,Marzo,Abril,Mayo,Junio,Julio,Agosto,Septiembre,Octubre,Noviembre,Diciembre"/>
                        <option value="${m}"
                            <c:if test="${(not empty presupuesto && presupuesto.mes == m) || (empty presupuesto && m == pageContext.request.getAttribute('mesActual'))}">selected</c:if>>
                            ${fn:split(meses, ',')[m-1]}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="formularioPresupuesto__campo">
                <label class="formularioPresupuesto__label">Alertas de presupuesto</label>
                <div class="formularioPresupuesto__opciones">
                    <label class="checkboxOpcion">
                        <input type="checkbox" name="alerta80" class="checkboxOpcion__input"
                            ${empty presupuesto || presupuesto.alerta80 ? 'checked' : ''}>
                        <span class="checkboxOpcion__marca">✓</span>
                        <span class="checkboxOpcion__texto">Avisarme cuando llegue al 80%</span>
                    </label>
                    <label class="checkboxOpcion">
                        <input type="checkbox" name="alertaSuperado" class="checkboxOpcion__input"
                            ${empty presupuesto || presupuesto.alertaSuperePresupuesto ? 'checked' : ''}>
                        <span class="checkboxOpcion__marca">✓</span>
                        <span class="checkboxOpcion__texto">Avisarme cuando supere el presupuesto</span>
                    </label>
                </div>
            </div>

            <%-- Vista previa con datos reales --%>
            <c:if test="${not empty presupuesto}">
            <div class="vistaPrevia">
                <div class="vistaPrevia__encabezado">
                    <span class="vistaPrevia__icono">📄</span>
                    <h3 class="vistaPrevia__titulo">Estado actual de ${presupuesto.nombreMes}</h3>
                </div>
                <div class="vistaPrevia__contenido">
                    <div class="vistaPrevia__item">
                        <span class="vistaPrevia__label">Presupuesto:</span>
                        <span class="vistaPrevia__valor">$ <fmt:formatNumber value="${presupuesto.montoMax}" pattern="#,##0.00"/></span>
                    </div>
                    <div class="vistaPrevia__item">
                        <span class="vistaPrevia__label">Gastos actuales:</span>
                        <span class="vistaPrevia__valor">$ <fmt:formatNumber value="${totalEgresos}" pattern="#,##0.00"/></span>
                    </div>
                    <div class="vistaPrevia__item">
                        <span class="vistaPrevia__label">Disponible:</span>
                        <span class="vistaPrevia__valor vistaPrevia__valor--${presupuesto.superado ? 'rojo' : 'verde'}">
                            $ <fmt:formatNumber value="${presupuesto.disponible}" pattern="#,##0.00"/>
                        </span>
                    </div>
                </div>
            </div>
            </c:if>

            <div class="presupuestoMensual__boton">
                <button type="submit" class="boton boton--registrar">Guardar presupuesto</button>
            </div>
            <a href="${pageContext.request.contextPath}/Finanzas" class="presupuestoMensual__boton">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </main>
</body>
</html>

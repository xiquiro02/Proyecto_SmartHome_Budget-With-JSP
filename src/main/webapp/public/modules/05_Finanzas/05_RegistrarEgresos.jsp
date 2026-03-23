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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/05_Finanzas/estilosRegistrarEgresos.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Finanzas?accion=detalleEgresos">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">
                <c:choose>
                    <c:when test="${modoEdicion == true}">Editar Egreso</c:when>
                    <c:otherwise>Registrar Egreso</c:otherwise>
                </c:choose>
            </h1>
        </div>
    </header>

    <main class="formulario">
        <c:if test="${not empty error}">
            <div style="margin:10px 20px;padding:10px;background:#ffe0e0;border-radius:8px;color:#c00;">${error}</div>
        </c:if>

        <form class="formulario__contenedor" id="formEgreso" method="post"
              action="${pageContext.request.contextPath}/Finanzas" novalidate>
            <input type="hidden" name="accion" value="guardarEgreso">
            <c:if test="${modoEdicion == true}">
                <input type="hidden" name="idEgreso" value="${egreso.idEgresos}">
            </c:if>

            <%-- Tipo de gasto --%>
            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="idCategoriaEgreso">Tipo de gasto</label>
                <select id="idCategoriaEgreso" name="idCategoriaEgreso" class="formulario__select">
                    <option value="">Seleccionar tipo</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat[0]}"
                            <c:if test="${modoEdicion == true && egreso.idCategoriaEgreso == cat[0]}">selected</c:if>>
                            ${cat[1]}
                        </option>
                    </c:forEach>
                </select>
                <span id="errCategoria" style="color:#c00;font-size:12px;display:none;margin-top:4px;"></span>
            </div>

            <%-- Monto --%>
            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="monto">Monto del egreso:</label>
                <input type="number" id="monto" name="monto" class="formulario__input"
                       placeholder="0.00" step="0.01" min="0.01" max="9999999.99"
                       value="${modoEdicion == true ? egreso.monto : ''}">
                <small style="color:#888">Valor positivo. Máximo 9,999,999.99</small>
                <span id="errMonto" style="color:#c00;font-size:12px;display:none;margin-top:4px;"></span>
            </div>

            <%-- Fecha --%>
            <div class="formulario__campo">
                <label class="formulario__etiqueta">Fecha del gasto:</label>
                <label class="formulario__fecha">
                    <input type="radio" name="fecha" checked>
                    <span>
                        <c:choose>
                            <c:when test="${modoEdicion == true}">Se mantiene la fecha original</c:when>
                            <c:otherwise>Hoy (automática)</c:otherwise>
                        </c:choose>
                    </span>
                </label>
            </div>

            <%-- Descripción --%>
            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="descripcion">Descripción (opcional):</label>
                <textarea id="descripcion" name="descripcion" class="formulario__textarea" rows="4"
                    maxlength="200"
                    placeholder="Ej: Pago de luz, compra mercado, cine..."><c:if test="${modoEdicion == true}">${egreso.descripcionPago}</c:if></textarea>
                <small style="color:#888">Máximo 200 caracteres (opcional).</small>
                <span id="errDescripcion" style="color:#c00;font-size:12px;display:none;margin-top:4px;"></span>
            </div>

            <div class="formulario__botones">
                <button type="submit" class="boton boton--registrar">
                    <c:choose>
                        <c:when test="${modoEdicion == true}">Guardar cambios</c:when>
                        <c:otherwise>Guardar egreso</c:otherwise>
                    </c:choose>
                </button>
            </div>
            <a href="${pageContext.request.contextPath}/Finanzas?accion=detalleEgresos" class="formulario__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </main>
<script>
(function () {
    const form     = document.getElementById('formEgreso');
    const selCat   = document.getElementById('idCategoriaEgreso');
    const inMonto  = document.getElementById('monto');
    const txDesc   = document.getElementById('descripcion');
    const errCat   = document.getElementById('errCategoria');
    const errMonto = document.getElementById('errMonto');
    const errDesc  = document.getElementById('errDescripcion');

    function mostrar(span, msg, input) {
        span.textContent = msg;
        span.style.display = 'block';
        if (input) input.style.borderColor = '#c00';
    }
    function limpiar(span, input) {
        span.style.display = 'none';
        if (input) input.style.borderColor = '';
    }

    function validarCategoria() {
        if (!selCat.value) { mostrar(errCat, 'Debes seleccionar un tipo de gasto.', selCat); return false; }
        limpiar(errCat, selCat); return true;
    }
    function validarMonto() {
        const v = parseFloat(inMonto.value);
        if (!inMonto.value.trim())  { mostrar(errMonto, 'El monto es obligatorio.', inMonto); return false; }
        if (isNaN(v) || v <= 0)     { mostrar(errMonto, 'Ingresa un valor mayor a 0.', inMonto); return false; }
        if (v > 9999999.99)         { mostrar(errMonto, 'El valor máximo permitido es 9,999,999.99.', inMonto); return false; }
        limpiar(errMonto, inMonto); return true;
    }
    function validarDescripcion() {
        const v = txDesc.value;
        if (v.length > 200) { mostrar(errDesc, 'Máximo 200 caracteres.', txDesc); return false; }
        limpiar(errDesc, txDesc); return true;
    }

    selCat.addEventListener('change', validarCategoria);
    inMonto.addEventListener('input', validarMonto);
    txDesc.addEventListener('input', validarDescripcion);

    form.addEventListener('submit', function (e) {
        const okCat  = validarCategoria();
        const okMon  = validarMonto();
        const okDesc = validarDescripcion();
        if (!okCat || !okMon || !okDesc) e.preventDefault();
    });
})();
</script>
</body>
</html>

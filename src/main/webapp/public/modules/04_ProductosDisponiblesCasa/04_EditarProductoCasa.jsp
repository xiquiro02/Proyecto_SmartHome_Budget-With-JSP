<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosEditarProductoCasa.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Inventario?accion=consultar"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Editar producto</h1></div>
</header>
<main class="formulario">
    <div class="formulario__contenedor">

        <c:if test="${param.error == 'cantidad_invalida'}">
            <div class="mensaje mensaje--error">⚠️ La cantidad no es válida. Debe ser entre 0 y 999.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/Inventario" method="post" id="formEditarInv" novalidate>
            <input type="hidden" name="accion"       value="actualizar">
            <input type="hidden" name="idInventario" value="${inv.idInventario}">

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Nombre del producto:</label>
                <input type="text" class="formulario__input" value="${inv.nombreProducto}" readonly
                       style="background:#f0f0f0;color:#666">
                <small style="color:#888">Para cambiar el nombre, elimina y registra de nuevo.</small>
            </div>

            <%-- Cantidad DECIMAL: muestra el valor actual correctamente --%>
            <fmt:formatNumber var="cantidadFmt" value="${inv.cantidad}" maxFractionDigits="2" groupingUsed="false"/>
            <div class="formulario__campo">
                <label class="formulario__etiqueta">Cantidad disponible: * (0 = agotado)</label>
                <input type="number" name="cantidad" id="cantidad" class="formulario__input"
                       value="${cantidadFmt}" min="0" max="999" step="0.01" required>
                <small style="color:#888">
                    Puedes usar decimales para peso o volumen (ej: 1.5 para kilos o litros).
                </small>
                <span id="errCantidad" style="color:#c00;font-size:12px;display:none"></span>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Categoría:</label>
                <input type="text" class="formulario__input" value="${inv.nombreTipoProducto}" readonly
                       style="background:#f0f0f0;color:#666">
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Última actualización:</label>
                <input type="text" class="formulario__input"
                       value="${inv.fechaActualizacionFormateada}" readonly
                       style="background:#f0f0f0;color:#666">
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Descripción:</label>
                <input type="text" class="formulario__input"
                       value="${not empty inv.descripcion ? inv.descripcion : 'Sin descripción'}" readonly
                       style="background:#f0f0f0;color:#666">
            </div>

            <div class="formulario__botones">
                <button type="submit" class="boton boton--registrar">Guardar cambios</button>
            </div>
            <a href="${pageContext.request.contextPath}/Inventario?accion=consultar" class="formulario__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/asset/js/confirmacionesInventario.js"></script>
<script>
(function () {
    const form   = document.getElementById('formEditarInv');
    const inCant = document.getElementById('cantidad');
    const errSpan = document.getElementById('errCantidad');

    function validar() {
        const v = parseFloat(inCant.value);
        if (isNaN(v) || v < 0) { errSpan.textContent = 'La cantidad no puede ser negativa.'; errSpan.style.display = 'block'; return false; }
        if (v > 999)           { errSpan.textContent = 'La cantidad máxima es 999.';         errSpan.style.display = 'block'; return false; }
        errSpan.style.display = 'none'; return true;
    }

    inCant.addEventListener('input', validar);
    form.addEventListener('submit', function (e) { if (!validar()) e.preventDefault(); });
})();
</script>
</body>
</html>

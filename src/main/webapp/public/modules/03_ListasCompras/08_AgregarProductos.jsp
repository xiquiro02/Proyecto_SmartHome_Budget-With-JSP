<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosAgregarProductos.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Listas?accion=verDetalle&id=${lista.idListaCompras}">
        <span class="material-symbols-outlined">arrow_back_ios_new</span>
    </a>
    <div class="encabezado__contenedorTitulo">
        <h1 class="encabezado__titulo">Agregar producto</h1>
    </div>
</header>
<main class="formulario">
    <div class="formulario__contenedor">
        <c:if test="${not empty error}">
            <div class="mensaje mensaje--error">⚠️ ${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/Listas" method="post" id="formAgregarProd" novalidate>
            <input type="hidden" name="accion"  value="guardarProducto">
            <input type="hidden" name="idLista" value="${lista.idListaCompras}">

            <%-- Nombre del producto --%>
            <div class="formulario__campo">
                <label class="formulario__etiqueta">Nombre del producto: *</label>
                <input type="text" name="nombreProducto" id="nombreProducto" class="formulario__input"
                       placeholder="Ej: Leche entera" maxlength="50" required>
                <small style="color:#888">Mínimo 5 caracteres. Letras, números, espacios y: . , # -</small>
                <small style="color:#888">Si el producto ya existe en la lista, se sumará la cantidad.</small>
                <span id="errNombre" style="color:#c00;font-size:12px;display:none"></span>
            </div>

            <%-- Cantidad DECIMAL --%>
            <div class="formulario__campo">
                <label class="formulario__etiqueta">Cantidad: *</label>
                <input type="number" name="cantidad" id="cantidad" class="formulario__input"
                       min="0.01" max="999" step="0.01" value="1" required>
                <%-- HINT requerido --%>
                <small style="color:#888">
                    Puedes usar números decimales para peso o volumen (ej: 1.5 para kilos o litros).
                </small>
                <span id="errCantidad" style="color:#c00;font-size:12px;display:none"></span>
            </div>

            <%-- Tipo producto --%>
            <div class="formulario__campo">
                <label class="formulario__etiqueta">Tipo de producto (opcional):</label>
                <select name="idTipoProducto" class="formulario__select">
                    <option value="">Seleccionar tipo</option>
                    <c:forEach var="tipo" items="${tiposProducto}">
                        <option value="${tipo.idTipoProducto}">${tipo.nombreTipoProducto}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="formulario__etiquetas">
                <div class="etiqueta etiqueta--aseo"><img src="${pageContext.request.contextPath}/asset/imagenes/Aseo.png" alt=""><span>Aseo</span></div>
                <div class="etiqueta etiqueta--alimentos"><img src="${pageContext.request.contextPath}/asset/imagenes/alimentos-saludables.png" alt=""><span>Alimentos</span></div>
                <div class="etiqueta etiqueta--personal"><img src="${pageContext.request.contextPath}/asset/imagenes/maquillaje.png" alt=""><span>Personal</span></div>
                <div class="etiqueta etiqueta--otros"><img src="${pageContext.request.contextPath}/asset/imagenes/cajas.png" alt=""><span>Otros</span></div>
                <div class="etiqueta etiqueta--ropa-calzado"><img src="${pageContext.request.contextPath}/asset/imagenes/Ropa-calzado.jpg" alt=""><span>Ropa</span></div>
            </div>

            <div class="formulario__botones">
                <button type="submit" class="boton boton--registrar">Guardar producto</button>
            </div>
            <a href="${pageContext.request.contextPath}/Listas" class="formulario__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </div>
</main>
<script>
(function () {
    const form      = document.getElementById('formAgregarProd');
    const inNombre  = document.getElementById('nombreProducto');
    const inCant    = document.getElementById('cantidad');
    const errNombre = document.getElementById('errNombre');
    const errCant   = document.getElementById('errCantidad');
    const PATRON    = /^[\p{L}\p{N} .,#\-]+$/u;

    function validarNombre() {
        const v = inNombre.value.trim();
        if (!v)              { errNombre.textContent = 'El nombre es obligatorio.'; errNombre.style.display = 'block'; return false; }
        if (v.length < 5)    { errNombre.textContent = 'Mínimo 5 caracteres.';     errNombre.style.display = 'block'; return false; }
        if (v.length > 50)   { errNombre.textContent = 'Máximo 50 caracteres.';    errNombre.style.display = 'block'; return false; }
        if (!PATRON.test(v)) { errNombre.textContent = 'Solo letras, números, espacios y: . , # -'; errNombre.style.display = 'block'; return false; }
        errNombre.style.display = 'none'; return true;
    }

    function validarCantidad() {
        const v = parseFloat(inCant.value);
        if (isNaN(v) || v <= 0)  { errCant.textContent = 'La cantidad debe ser mayor a 0.'; errCant.style.display = 'block'; return false; }
        if (v > 999)             { errCant.textContent = 'La cantidad máxima es 999.';      errCant.style.display = 'block'; return false; }
        errCant.style.display = 'none'; return true;
    }

    inNombre.addEventListener('input', validarNombre);
    inCant.addEventListener('input', validarCantidad);

    form.addEventListener('submit', function (e) {
        inNombre.value = inNombre.value.trim();
        const okN = validarNombre();
        const okC = validarCantidad();
        if (!okN || !okC) e.preventDefault();
    });
})();
</script>
</body>
</html>

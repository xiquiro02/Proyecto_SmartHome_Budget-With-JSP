<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosCrearLista.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Listas"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Crear lista</h1></div>
</header>
<main class="formulario">
    <div class="formulario__contenedor">
        <c:if test="${not empty error}">
            <div class="mensaje mensaje--error">⚠️ ${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/Listas" method="post" id="formCrearLista" novalidate>
            <input type="hidden" name="accion" value="registrar">

            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="nombreLista">Nombre de la lista: *</label>
                <input type="text" id="nombreLista" name="nombreLista" class="formulario__input"
                       placeholder="Ej: Mercado mensual"
                       minlength="5" maxlength="50" required
                       value="${not empty valorNombre ? valorNombre : ''}">
                <small style="color:#888">Mínimo 5 y máximo 50 caracteres. Se permiten letras, números, espacios, puntos, guiones y guion bajo.</small>
                <span id="errorNombre" style="color:#c00;font-size:12px;display:none;margin-top:10px;display:none"></span>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Fecha de creación:</label>
                <label class="formulario__fecha">
                    <input type="radio" name="fecha" checked><span>Hoy (automática)</span>
                </label>
            </div>

            <div class="formulario__botones">
                <button type="submit" class="boton boton--registrar">Siguiente</button>
            </div>
            <a href="${pageContext.request.contextPath}/Listas" class="formulario__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </div>
</main>
<script>
(function () {
    const form    = document.getElementById('formCrearLista');
    const input   = document.getElementById('nombreLista');
    const errSpan = document.getElementById('errorNombre');
    // Letras (con tildes y ñ), números, espacios, . - _
    const PATRON  = /^[\p{L}\p{N} .\-_]+$/u;

    function mostrarError(msg) {
        errSpan.textContent = msg;
        errSpan.style.display = 'block';
        input.style.borderColor = '#c00';
    }
    function limpiarError() {
        errSpan.style.display = 'none';
        input.style.borderColor = '';
    }

    function validar() {
        const val = input.value.trim();
        if (!val)              { mostrarError('El nombre es obligatorio.'); return false; }
        if (val.length < 5)    { mostrarError('Mínimo 5 caracteres.'); return false; }
        if (val.length > 50)   { mostrarError('Máximo 50 caracteres.'); return false; }
        if (!PATRON.test(val)) { mostrarError('Solo letras, números, espacios, puntos, guiones y guion bajo.'); return false; }
        limpiarError();
        return true;
    }

    input.addEventListener('input', validar);
    form.addEventListener('submit', function (e) {
        input.value = input.value.trim();
        if (!validar()) e.preventDefault();
    });
})();
</script>
</body>
</html>

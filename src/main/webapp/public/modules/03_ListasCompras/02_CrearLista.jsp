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
            <div class="mensaje mensaje--error">️${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/Listas" method="post">
            <input type="hidden" name="accion" value="registrar">

            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="nombreLista">Nombre de la lista: *</label>
                <input type="text" id="nombreLista" name="nombreLista" class="formulario__input"
                       placeholder="Ej: Mercado mensual" maxlength="100" required>
            </div>

            <div class="formulario__etiquetas">
                <div class="etiqueta etiqueta--aseo">
                    <img src="${pageContext.request.contextPath}/asset/imagenes/Aseo.png" alt="Aseo"><span>Aseo</span>
                </div>
                <div class="etiqueta etiqueta--alimentos">
                    <img src="${pageContext.request.contextPath}/asset/imagenes/alimentos-saludables.png" alt="Alimentos"><span>Alimentos</span>
                </div>
                <div class="etiqueta etiqueta--personal">
                    <img src="${pageContext.request.contextPath}/asset/imagenes/maquillaje.png" alt="Personal"><span>Personal</span>
                </div>
                <div class="etiqueta etiqueta--otros">
                    <img src="${pageContext.request.contextPath}/asset/imagenes/cajas.png" alt="Otros"><span>Otros</span>
                </div>
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
</body>
</html>

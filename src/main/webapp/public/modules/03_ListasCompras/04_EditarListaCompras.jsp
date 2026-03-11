<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosEditarListaCompras.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Listas"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Editar lista</h1></div>
</header>
<main class="edicion">
    <div class="edicion__contenedor">
        <c:if test="${not empty param.error}">
            <div class="mensaje mensaje--error">⚠️ Revisa los campos e intenta de nuevo.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/Listas" method="post">
            <input type="hidden" name="accion" value="actualizar">
            <input type="hidden" name="idLista" value="${lista.idListaCompras}">

            <div class="edicion__campo">
                <label class="edicion__etiqueta">Nombre de la lista: *</label>
                <input type="text" name="nombreLista" class="edicion__input"
                       value="${lista.nombreLista}" maxlength="100" required>
            </div>

            <div class="edicion__campo">
                <label class="edicion__etiqueta">Estado:</label>
                <select name="estadoLista" class="edicion__select">
                    <option value="Pendiente"    ${lista.estadoLista == 'Pendiente'    ? 'selected' : ''}>Pendiente</option>
                    <option value="En progreso"  ${lista.estadoLista == 'En progreso'  ? 'selected' : ''}>En progreso</option>
                    <option value="Completa"     ${lista.estadoLista == 'Completa'     ? 'selected' : ''}>Completa</option>
                </select>
            </div>

            <%-- Productos en la lista --%>
            <div class="edicion__productos">
                <h3 class="edicion__subtitulo">Productos en esta lista (${lista.totalProductos}):</h3>
                <c:choose>
                    <c:when test="${empty detalles}">
                        <p class="edicion__vacio">Esta lista no tiene productos aún.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="d" items="${detalles}">
                            <div class="producto">
                                <div class="producto__info">
                                    <span class="producto__nombre">${d.nombreProducto}</span>
                                    <span class="producto__cantidad">Cantidad: ${d.cantidad}</span>
                                    <c:if test="${d.comprado}">
                                        <span class="producto__comprado">✅ Comprado</span>
                                    </c:if>
                                </div>
                                <div class="producto__botones">
                                    <a href="${pageContext.request.contextPath}/Listas?accion=editarProducto&idDetalle=${d.idDetalleLista}&idLista=${lista.idListaCompras}">
                                        <button type="button" class="producto__boton producto__boton--editar">✏️ Editar</button>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/Listas?accion=confirmarEliminarProd&idDetalle=${d.idDetalleLista}&idLista=${lista.idListaCompras}">
                                        <button type="button" class="producto__boton producto__boton--eliminar">🗑️ Eliminar</button>
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                <a href="${pageContext.request.contextPath}/Listas?accion=agregarProducto&id=${lista.idListaCompras}">
                    <button type="button" class="boton boton--agregar">➕ Agregar producto</button>
                </a>
            </div>

            <div class="edicion__botones">
                <button type="submit" class="boton boton--registrar">Guardar cambios</button>
            </div>
            <a href="${pageContext.request.contextPath}/Listas" class="edicion__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </div>
</main>
</body>
</html>

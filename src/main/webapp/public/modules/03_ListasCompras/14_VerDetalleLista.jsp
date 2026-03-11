<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosVerDetallesMercado-mensual.css">
    <title>SmartHome Budget</title>
</head>
<body>
<main class="contenedor-lista">

    <a href="${pageContext.request.contextPath}/Listas" style="display:flex;align-items:center;gap:4px;text-decoration:none;color:inherit;margin-bottom:0.5rem">
        <span class="material-symbols-outlined">arrow_back_ios_new</span> Mis listas
    </a>

    <article class="tarjetaMercado">
        <div class="tarjetaMercado__encabezado">
            <div class="tarjetaMercado__icono">
                <img src="${pageContext.request.contextPath}/asset/imagenes/lista-compra.png" alt="Lista">
            </div>
            <div class="tarjetaMercado__info">
                <h2 class="tarjetaMercado__nombre">${lista.nombreLista}</h2>
            </div>
        </div>
        <div class="tarjetaMercado__etiquetas">
            <c:set var="tagClass" value="pendiente"/>
            <c:if test="${lista.estadoLista == 'En progreso'}"><c:set var="tagClass" value="proceso"/></c:if>
            <c:if test="${lista.estadoLista == 'Completa'}"><c:set var="tagClass" value="completa"/></c:if>
            <span class="etiqueta etiqueta--${tagClass}">${lista.estadoLista}</span>
            <span class="etiqueta etiqueta--total">${lista.totalProductos} productos</span>
        </div>
    </article>

    <section class="estadisticas">
        <div class="estadisticas__item">
            <div class="estadisticas__numero estadisticas__numero--comprado">${lista.totalComprados}</div>
            <p class="estadisticas__texto">Comprados</p>
        </div>
        <div class="estadisticas__item">
            <div class="estadisticas__numero estadisticas__numero--pendiente">${lista.totalPendientes}</div>
            <p class="estadisticas__texto">Pendientes</p>
        </div>
    </section>

    <p class="fecha">
        Creada: <fmt:formatDate value="${lista.fechaCreacion}" pattern="dd MMM yyyy" type="date"/>
    </p>

    <c:if test="${param.exito == 'prod_eliminado'}">
        <div class="mensaje mensaje--exito">✅ Producto eliminado de la lista.</div>
    </c:if>

    <h3 class="tituloSeccion">Productos de la lista</h3>

    <c:choose>
        <c:when test="${empty detalles}">
            <p style="text-align:center;color:#999;margin:1rem 0">Esta lista no tiene productos aún.</p>
            <a href="${pageContext.request.contextPath}/Listas?accion=agregarProducto&id=${lista.idListaCompras}">
                <button class="boton boton--agregar">➕ Agregar primer producto</button>
            </a>
        </c:when>
        <c:otherwise>
            <ul class="listaProductos">
                <c:forEach var="d" items="${detalles}">
                    <li class="producto ${d.comprado ? 'producto--completado' : ''}">
                        <label class="producto__label">
                            <%-- Checkbox que envía POST al servlet para toggle --%>
                            <form action="${pageContext.request.contextPath}/Listas" method="post" style="display:inline">
                                <input type="hidden" name="accion" value="toggleComprado">
                                <input type="hidden" name="idDetalle" value="${d.idDetalleLista}">
                                <input type="hidden" name="idLista" value="${lista.idListaCompras}">
                                <input type="hidden" name="comprado" value="${d.comprado ? 'false' : 'true'}">
                                <input type="checkbox" class="producto__input" ${d.comprado ? 'checked' : ''}
                                       onchange="this.form.submit()">
                            </form>
                            <span class="producto__circulo"></span>
                            <div class="producto__info">
                                <h4 class="producto__nombre">${d.nombreProducto}</h4>
                                <p class="producto__cantidad">${d.cantidad} unidades · ${d.tipoProducto}</p>
                            </div>
                        </label>
                    </li>
                </c:forEach>
            </ul>

            <%-- Botones marcar/desmarcar todos --%>
            <div class="acciones">
                <form action="${pageContext.request.contextPath}/Listas" method="post" style="display:contents">
                    <input type="hidden" name="accion" value="marcarTodos">
                    <input type="hidden" name="idLista" value="${lista.idListaCompras}">
                    <input type="hidden" name="comprado" value="true">
                    <button type="submit" class="boton boton--marcar">✅ Marcar todo</button>
                </form>
                <form action="${pageContext.request.contextPath}/Listas" method="post" style="display:contents">
                    <input type="hidden" name="accion" value="marcarTodos">
                    <input type="hidden" name="idLista" value="${lista.idListaCompras}">
                    <input type="hidden" name="comprado" value="false">
                    <button type="submit" class="boton boton--marcar">↩️ Desmarcar todo</button>
                </form>
            </div>
        </c:otherwise>
    </c:choose>

    <%-- Acciones de gestión --%>
    <div class="acciones" style="margin-top:1rem">
        <a href="${pageContext.request.contextPath}/Listas?accion=agregarProducto&id=${lista.idListaCompras}">
            <button class="boton boton--agregar">➕ Agregar producto</button>
        </a>
        <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
            <a href="${pageContext.request.contextPath}/Listas?accion=editar&id=${lista.idListaCompras}">
                <button class="boton boton--editar">✏️ Editar lista</button>
            </a>
        </c:if>
    </div>

    <a href="${pageContext.request.contextPath}/Listas" class="acciones__volver">
        <button class="boton boton--volver">Volver</button>
    </a>
</main>
</body>
</html>

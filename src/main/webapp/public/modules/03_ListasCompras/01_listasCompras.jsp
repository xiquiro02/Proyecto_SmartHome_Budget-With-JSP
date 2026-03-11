<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_forward"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilos_listasCompras.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <div class="encabezado__contenedor">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/lista-compra.png" alt="Listas">
        <h1 class="encabezado__titulo">Listas de Compras</h1>
    </div>
    <c:if test="${param.exito == 'lista_eliminada'}">
        <div class="mensaje mensaje--exito">Lista eliminada correctamente.</div>
    </c:if>
    <c:if test="${param.error == 'sin_permiso'}">
        <div class="mensaje mensaje--error"> No tienes permiso para esa acción.</div>
    </c:if>
</header>
<main class="listas">
    <h2 class="listas__titulo">Tus Listas</h2>
    <div class="listas__contenido">
        <c:choose>
            <c:when test="${empty listas}">
                <p class="listas__vacio">No tienes listas creadas aún.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="lista" items="${listas}">
                    <%-- Color por estado --%>
                    <c:set var="color" value="rojo"/>
                    <c:if test="${lista.estadoLista == 'En progreso'}"><c:set var="color" value="naranja"/></c:if>
                    <c:if test="${lista.estadoLista == 'Completa'}"><c:set var="color" value="verde"/></c:if>

                    <div class="lista-card lista-card--${color}">
                        <div class="lista-card__header">
                            <div class="lista-card__info-box">
                                <h3 class="lista-card__titulo lista-card__titulo--${color}">${lista.nombreLista}</h3>
                                <p class="lista-card__info">Productos: ${lista.totalProductos}</p>
                                <p class="lista-card__info">
                                    Creada: <fmt:formatDate value="${lista.fechaCreacion}" pattern="dd MMM yyyy" type="date"/>
                                </p>
                            </div>
                            <div class="lista-card__estado">
                                <c:set var="tagClass" value="pendiente"/>
                                <c:if test="${lista.estadoLista == 'En progreso'}"><c:set var="tagClass" value="proceso"/></c:if>
                                <c:if test="${lista.estadoLista == 'Completa'}"><c:set var="tagClass" value="completa"/></c:if>
                                <span class="lista-card__tag lista-card__tag--${tagClass}">${lista.estadoLista}</span>
                                <a href="${pageContext.request.contextPath}/Listas?accion=verDetalle&id=${lista.idListaCompras}"
                                   class="lista-card__ver-detalles">👁 Ver detalles</a>
                            </div>
                        </div>
                        <div class="lista-card__acciones">
                            <%-- Todos los roles pueden agregar productos --%>
                            <a href="${pageContext.request.contextPath}/Listas?accion=agregarProducto&id=${lista.idListaCompras}">
                                <button class="boton boton--agregar">➕ Agregar producto</button>
                            </a>
                            <%-- Solo ADMIN y COTITULAR pueden editar y eliminar --%>
                            <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
                                <a href="${pageContext.request.contextPath}/Listas?accion=editar&id=${lista.idListaCompras}">
                                    <button class="boton boton--editar">✏️ Editar</button>
                                </a>
                                <a href="${pageContext.request.contextPath}/Listas?accion=confirmarEliminar&id=${lista.idListaCompras}">
                                    <button class="boton boton--Eliminar">🗑️ Eliminar</button>
                                </a>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="listas__botones">
        <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
        <a href="${pageContext.request.contextPath}/Listas?accion=crear" class="listas__boton">
            <button class="boton boton--registrar">➕ Crear nueva lista</button>
        </a>
        </c:if>
        <%-- INVITADO también puede crear listas según RF --%>
        <c:if test="${sessionScope.idRol == 3}">
        <a href="${pageContext.request.contextPath}/Listas?accion=crear" class="listas__boton">
            <button class="boton boton--registrar">➕ Crear lista</button>
        </a>
        </c:if>
        <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp" class="listas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </div>
</main>
</body>
</html>

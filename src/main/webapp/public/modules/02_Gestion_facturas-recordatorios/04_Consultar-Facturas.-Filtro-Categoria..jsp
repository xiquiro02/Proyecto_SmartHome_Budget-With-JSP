<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosConsultar-Facturas.-Filtro-Categoria.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas?accion=lista"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
        <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Consultar Facturas</h1></div>
    </header>
    <main class="consultarFacturas">
        <div class="consultarFacturas__filtros">
            <p class="consultarFacturas__filtro-label">Filtrar por:</p>
            <div class="consultarFacturas__filtro-grupo">
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroCategoria" class="consultarFacturas__filtro-enlace consultarFacturas__filtro-enlace--activo">Categoría <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroFecha" class="consultarFacturas__filtro-enlace">Fecha <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroEstado" class="consultarFacturas__filtro-enlace">Estado <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
            </div>
        </div>
        <div class="facturaLista">
            <c:forEach var="f" items="${facturas}">
                <c:set var="color" value="naranja"/>
                <c:if test="${f.estadoPago == 'Pagada'}"><c:set var="color" value="verde"/></c:if>
                <c:if test="${f.estadoPago == 'Vencida'}"><c:set var="color" value="rojo"/></c:if>
                <div class="facturaCard facturaCard--${color}">
                    <div class="facturaCard__borde"></div>
                    <div class="facturaCard__contenido">
                        <h3 class="facturaCard__titulo">${f.nombreFactura} - <fmt:formatNumber value="${f.monto}" type="currency" currencySymbol="$"/></h3>
                        <p class="facturaCard__fecha">Vence: <fmt:formatDate value="${f.fechaVencimiento}" pattern="dd MMM yyyy" type="date"/></p>
                        <div class="facturaCard__estadoAcciones">
                            <span class="facturaCard__etiqueta facturaCard__etiqueta--${f.estadoPago == 'Pagada' ? 'pagada' : f.estadoPago == 'Vencida' ? 'vencida' : 'pendiente'}">${f.estadoPago}</span>
                            <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
                            <div class="facturaCard__acciones">
                                <a href="${pageContext.request.contextPath}/Facturas?accion=editar&id=${f.idEgresos}"><button class="boton boton--editar">✏️ Editar</button></a>
                                <a href="${pageContext.request.contextPath}/Facturas?accion=confirmarEliminar&id=${f.idEgresos}"><button class="boton boton--Eliminar">🗑️ Eliminar</button></a>
                            </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty facturas}"><p class="consultarFacturas__vacio">No hay facturas para esta categoría.</p></c:if>
        </div>

        <%-- Panel de filtro por categoría --%>
        <div class="panelFiltro">
            <div class="filtroCategoria">
                <h3 class="filtroCategoria__titulo">Seleccionar categoría</h3>
                <c:forEach var="cat" items="${categorias}">
                    <form action="${pageContext.request.contextPath}/Facturas" method="get" style="display:contents">
                        <input type="hidden" name="accion" value="filtroCategoria">
                        <label class="filtroCategoria__opcion">
                            <input type="radio" name="idCategoria" value="${cat.IDCategoriaEgreso}"
                                   ${categoriaFiltro == cat.IDCategoriaEgreso ? 'checked' : ''}
                                   onchange="this.form.submit()">
                            <span class="filtroCategoria__opcion-texto">${cat.NombreCategoriaEgreso}</span>
                        </label>
                    </form>
                </c:forEach>
                <a href="${pageContext.request.contextPath}/Facturas?accion=lista" class="filtroCategoria__boton">
                    <button class="boton boton--cancelar">Ver todas</button>
                </a>
            </div>
        </div>
    </main>
</body>
</html>

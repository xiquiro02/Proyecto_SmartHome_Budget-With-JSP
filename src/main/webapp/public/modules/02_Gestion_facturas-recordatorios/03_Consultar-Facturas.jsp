<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosConsultar-Facturas.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Consultar Facturas</h1>
        </div>
    </header>

    <main class="consultarFacturas">
        <div class="consultarFacturas__filtros">
            <p class="consultarFacturas__filtro-label">Filtrar por:</p>
            <div class="consultarFacturas__filtro-grupo">
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroCategoria" 
                   class="consultarFacturas__filtro-enlace ${param.accion == 'filtroCategoria' ? 'consultarFacturas__filtro-enlace--activo' : ''}">
                    Categoría <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
                </a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroFecha" 
                   class="consultarFacturas__filtro-enlace ${param.accion == 'filtroFecha' ? 'consultarFacturas__filtro-enlace--activo' : ''}">
                    Fecha <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
                </a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=filtroEstado" 
                   class="consultarFacturas__filtro-enlace ${param.accion == 'filtroEstado' ? 'consultarFacturas__filtro-enlace--activo' : ''}">
                    Estado <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
                </a>
            </div>
        </div>

        <c:if test="${param.exito == 'registrada'}">
            <div class="mensaje mensaje--exito">✅ Factura registrada correctamente.</div>
        </c:if>
        <c:if test="${param.exito == 'pagada'}">
            <div class="mensaje mensaje--exito">✅ Factura marcada como pagada.</div>
        </c:if>
        <c:if test="${param.exito == 'eliminada'}">
            <div class="mensaje mensaje--exito">🗑 Factura eliminada.</div>
        </c:if>

        <div class="facturaLista">
            <c:choose>
                <c:when test="${empty facturas}">
                    <c:choose>
                        <c:when test="${not empty categoriaFiltro}">No hay facturas para esta categoría.</c:when>
                        <c:when test="${not empty mesFiltro}">No hay facturas para el mes seleccionado.</c:when>
                        <c:when test="${not empty estadoFiltro}">No hay facturas con ese estado.</c:when>
                        <c:otherwise>No hay facturas registradas aún.</c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:forEach var="f" items="${facturas}">
                        <%-- Color de la tarjeta según estado --%>
                        <c:set var="color" value="naranja"/>
                        <c:if test="${f.estadoPago == 'Pagada'}"><c:set var="color" value="verde"/></c:if>
                        <c:if test="${f.estadoPago == 'Vencida'}"><c:set var="color" value="rojo"/></c:if>

                        <div class="facturaCard facturaCard--${color}">
                            <div class="facturaCard__borde"></div>
                            <div class="facturaCard__contenido">
                                <div class="facturaCard__encabezado">
                                    <h3 class="facturaCard__titulo">
                                        ${f.nombreFactura} -
                                        <fmt:formatNumber value="${f.monto}" type="currency" currencySymbol="$"/>
                                    </h3>
                                </div>
                                <p class="facturaCard__fecha">
                                    Categoría: ${f.nombreCategoria}
                                </p>
                                <p class="facturaCard__fecha">
                                    Vence: ${f.fechaVencimientoFormateada}
                                </p>
                                <div class="facturaCard__estadoAcciones">
                                    <span class="facturaCard__etiqueta facturaCard__etiqueta--${f.estadoPago == 'Pagada' ? 'pagada' : f.estadoPago == 'Vencida' ? 'vencida' : 'pendiente'}">
                                        ${f.estadoPago}
                                    </span>
                                    <div class="facturaCard__acciones">
                                        <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
                                            <a href="${pageContext.request.contextPath}/Facturas?accion=editar&id=${f.idEgresos}">
                                                <button class="boton boton--editar">✏️ Editar</button>
                                            </a>
                                            <c:if test="${f.estadoPago != 'Pagada'}">
                                            <form action="${pageContext.request.contextPath}/Facturas" method="post" style="display:inline">
                                                <input type="hidden" name="accion" value="marcarPagada">
                                                <input type="hidden" name="idEgreso" value="${f.idEgresos}">
                                                <button type="submit" class="boton boton--registrar">✅ Pagar</button>
                                            </form>
                                            </c:if>
                                            <a href="${pageContext.request.contextPath}/Facturas?accion=confirmarEliminar&id=${f.idEgresos}">
                                                <button class="boton boton--Eliminar">🗑️ Eliminar</button>
                                            </a>
                                        </c:if>
                                        <%-- INVITADO solo puede ver --%>
                                        <c:if test="${sessionScope.idRol == 3}">
                                            <span class="facturaCard__readonly">Solo lectura</span>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- Paneles de filtros dinámicos --%>
        
        <%-- Panel de filtro por categoría --%>
        <c:if test="${param.accion == 'filtroCategoria'}">
            <div class="panelFiltro ${not empty categoriaFiltro ? 'panelFiltro--oculto' : ''}">
                <form action="${pageContext.request.contextPath}/Facturas" method="get" class="filtroCategoria">
                    <input type="hidden" name="accion" value="filtroCategoria">

                    <h3 class="filtroCategoria__titulo">Seleccionar categoría</h3>

                    <div class="filtroCategoria__opciones">
                        <c:forEach var="cat" items="${categorias}">
                            <label class="filtroCategoria__opcion">
                                <input type="radio" name="idCategoria" value="${cat.idCategoriaEgreso}"
                                       ${categoriaFiltro == cat.idCategoriaEgreso ? 'checked' : ''} required>
                                <span class="filtroCategoria__opcion-texto">${cat.nombreCategoriaEgreso}</span>
                            </label>
                        </c:forEach>
                    </div>

                    <div class="filtroCategoria__acciones">
                        <button type="submit" class="boton boton--aplicar">Aplicar filtro</button>
                        <a href="${pageContext.request.contextPath}/Facturas?accion=lista" class="enlace-ver-todas">Ver todas</a>
                    </div>
                </form>
            </div>
        </c:if>

        <%-- Panel de filtro por fecha --%>
        <c:if test="${param.accion == 'filtroFecha'}">
            <div class="panelFiltro ${not empty mesFiltro ? 'panelFiltro--oculto' : ''}">
                <form action="${pageContext.request.contextPath}/Facturas" method="get" class="filtroFecha">
                    <input type="hidden" name="accion" value="filtroFecha">
                    
                    <div class="filtroMes">
                        <h3 class="filtroMes__titulo">Seleccionar mes</h3>
                        <div class="filtroMes__gridMeses">
                            <c:forEach begin="1" end="12" var="m">
                                <label class="filtroMes__mes ${mesFiltro == m ? 'filtroMes__mes--activo' : ''}">
                                    <input type="radio" name="mes" value="${m}" ${mesFiltro == m ? 'checked' : ''} required>
                                    <c:choose>
                                        <c:when test="${m==1}">Enero</c:when><c:when test="${m==2}">Febrero</c:when>
                                        <c:when test="${m==3}">Marzo</c:when><c:when test="${m==4}">Abril</c:when>
                                        <c:when test="${m==5}">Mayo</c:when><c:when test="${m==6}">Junio</c:when>
                                        <c:when test="${m==7}">Julio</c:when><c:when test="${m==8}">Agosto</c:when>
                                        <c:when test="${m==9}">Septiembre</c:when><c:when test="${m==10}">Octubre</c:when>
                                        <c:when test="${m==11}">Noviembre</c:when><c:otherwise>Diciembre</c:otherwise>
                                    </c:choose>
                                </label>
                            </c:forEach>
                        </div>
                        <div class="filtroMes__anio">
                            <label class="filtroMes__anio-label">Año</label>
                            <select name="anio" class="filtroMes__anio-select">
                                <c:forEach begin="2020" end="2030" var="y">
                                    <option value="${y}" ${anioFiltro == y ? 'selected' : ''}>${y}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="filtroFecha__acciones">
                            <button type="submit" class="boton boton--aplicar">Aplicar filtro</button>
                            <a href="${pageContext.request.contextPath}/Facturas?accion=lista" class="enlace-ver-todas">Ver todas</a>
                        </div>
                    </div>
                </form>
            </div>
        </c:if>

        <%-- Panel de filtro por estado --%>
        <c:if test="${param.accion == 'filtroEstado'}">
            <div class="panelFiltro ${not empty estadoFiltro ? 'panelFiltro--oculto' : ''}">
                <form action="${pageContext.request.contextPath}/Facturas" method="get" class="filtroEstado">
                    <input type="hidden" name="accion" value="filtroEstado">
                    
                    <h3 class="filtroEstado__titulo">Seleccionar estado</h3>
                    
                    <div class="filtroEstado__opciones">
                        <c:forEach var="est" items="${['Pendiente','Pagada','Vencida']}">
                            <label class="filtroEstado__opcion">
                                <input type="radio" name="estado" value="${est}" ${estadoFiltro == est ? 'checked' : ''} required>
                                <div class="filtroEstado__opcion-contenido">
                                    <span class="filtroEstado__opcion-titulo">${est}</span>
                                </div>
                            </label>
                        </c:forEach>
                    </div>
                    
                    <div class="filtroEstado__acciones">
                        <button type="submit" class="boton boton--aplicar">Aplicar filtro</button>
                        <a href="${pageContext.request.contextPath}/Facturas?accion=lista" class="enlace-ver-todas">Ver todas</a>
                    </div>
                </form>
            </div>
        </c:if>

        <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
        <a href="${pageContext.request.contextPath}/Facturas?accion=form" class="consultarFacturas__boton">
            <button class="boton boton--cancelar">+ Nueva factura</button>
        </a>
        </c:if>
    </main>
</body>
</html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosHistorialPagos.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
        <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Historial de Pagos</h1></div>
    </header>
    <main class="consultarFacturas">
        <div class="consultarFacturas__descripcion">
            <p class="consultarFacturas__parrafo">Aquí puedes ver todas las facturas marcadas como pagadas, con su fecha y monto correspondiente.</p>
        </div>
        <div class="consultarFacturas__filtros">
            <p class="consultarFacturas__filtro-label">Filtrar por:</p>
            <div class="consultarFacturas__filtro-grupo">
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialCategoria" 
                   class="consultarFacturas__filtro-enlace ${param.accion == 'historialCategoria' ? 'consultarFacturas__filtro-enlace--activo' : ''}">Categoría <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialFecha" 
                   class="consultarFacturas__filtro-enlace ${param.accion == 'historialFecha' ? 'consultarFacturas__filtro-enlace--activo' : ''}">Fecha <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
                <a href="${pageContext.request.contextPath}/Facturas?accion=historialMonto" 
                   class="consultarFacturas__filtro-enlace ${param.accion == 'historialMonto' ? 'consultarFacturas__filtro-enlace--activo' : ''}">Monto <img class="consultarFacturas__filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt=""></a>
            </div>
        </div>

        <div class="facturaLista">
            <c:choose>
                <c:when test="${empty historial}">
                    <c:choose>
                        <c:when test="${not empty categoriaFiltro}">No hay pagos para esa categoría.</c:when>
                        <c:when test="${not empty mesFiltro and not empty anioFiltro}">No hay pagos para ese período.</c:when>
                        <c:when test="${not empty rangoFiltro}">No hay pagos en ese rango.</c:when>
                        <c:otherwise>No tienes facturas pagadas aún.</c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:forEach var="f" items="${historial}">
                        <div class="facturaCard facturaCard--verde">
                            <div class="facturaCard__borde"></div>
                            <div class="facturaCard__contenido">
                                <div class="facturaCard__encabezado">
                                    <h3 class="facturaCard__titulo">${f.nombreFactura}</h3>
                                    <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">Pagada</span>
                                </div>
                                <p class="facturaCard__detalles">Categoría: ${f.nombreCategoria}</p>
                                <p class="facturaCard__detalles">Monto: <fmt:formatNumber value="${f.monto}" type="currency" currencySymbol="$"/></p>
                                <p class="facturaCard__detalles">
                                    Pagado el: ${f.fechaVencimientoFormateada}
                                </p>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- Paneles de filtros dinámicos --%>
        
        <%-- Panel de filtro por categoría --%>
        <c:if test="${param.accion == 'historialCategoria'}">
            <div class="panelFiltro ${not empty categoriaFiltro ? 'panelFiltro--oculto' : ''}">
                <form action="${pageContext.request.contextPath}/Facturas" method="get" class="filtroCategoria">
                    <input type="hidden" name="accion" value="historialCategoria">

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
                        <a href="${pageContext.request.contextPath}/Facturas?accion=historial" class="enlace-ver-todas">Ver todas</a>
                    </div>
                </form>
            </div>
        </c:if>

        <%-- Panel de filtro por fecha --%>
        <c:if test="${param.accion == 'historialFecha'}">
            <div class="panelFiltro ${not empty mesFiltro ? 'panelFiltro--oculto' : ''}">
                <form action="${pageContext.request.contextPath}/Facturas" method="get" class="filtroFecha">
                    <input type="hidden" name="accion" value="historialFecha">
                    
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
                            <a href="${pageContext.request.contextPath}/Facturas?accion=historial" class="enlace-ver-todas">Ver todas</a>
                        </div>
                    </div>
                </form>
            </div>
        </c:if>

        <%-- Panel de filtro por monto --%>
        <c:if test="${param.accion == 'historialMonto'}">
            <div class="panelFiltro ${not empty rangoFiltro ? 'panelFiltro--oculto' : ''}">
                <form action="${pageContext.request.contextPath}/Facturas" method="get" class="filtroEstado">
                    <input type="hidden" name="accion" value="historialMonto">
                    
                    <h3 class="filtroEstado__titulo">Filtrar por monto</h3>
                    
                    <div class="filtroEstado__opciones">
                        <label class="filtroEstado__opcion">
                            <input type="radio" name="rango" value="menor50" ${rangoFiltro == 'menor50' ? 'checked' : ''} required>
                            <div class="filtroEstado__opcion-contenido">
                                <span class="filtroEstado__opcion-titulo">Menor a $50.000</span>
                            </div>
                        </label>
                        <label class="filtroEstado__opcion">
                            <input type="radio" name="rango" value="50a150" ${rangoFiltro == '50a150' ? 'checked' : ''}>
                            <div class="filtroEstado__opcion-contenido">
                                <span class="filtroEstado__opcion-titulo">Entre $50.000 y $150.000</span>
                            </div>
                        </label>
                        <label class="filtroEstado__opcion">
                            <input type="radio" name="rango" value="mayor200" ${rangoFiltro == 'mayor200' ? 'checked' : ''}>
                            <div class="filtroEstado__opcion-contenido">
                                <span class="filtroEstado__opcion-titulo">Mayor a $200.000</span>
                            </div>
                        </label>
                    </div>
                    
                    <div class="filtroEstado__acciones">
                        <button type="submit" class="boton boton--aplicar">Aplicar filtro</button>
                        <a href="${pageContext.request.contextPath}/Facturas?accion=historial" class="enlace-ver-todas">Ver todas</a>
                    </div>
                </form>
            </div>
        </c:if>

        <div class="totalPagado">
            <h3 class="totalPagado__titulo">Total pagado</h3>
            <p class="totalPagado__monto"><fmt:formatNumber value="${totalPagado}" type="currency" currencySymbol="$"/></p>
            <p class="totalPagado__texto">${cantidadPagadas} factura(s) procesada(s)</p>
        </div>

        <a href="${pageContext.request.contextPath}/public/modules/05_Finanzas/08_ResumenFinanciero.jsp" class="consultarFacturas__boton">
            <button class="boton boton--resumen">Ver resumen financiero</button>
        </a>
        <a href="${pageContext.request.contextPath}/Facturas" class="consultarFacturas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>

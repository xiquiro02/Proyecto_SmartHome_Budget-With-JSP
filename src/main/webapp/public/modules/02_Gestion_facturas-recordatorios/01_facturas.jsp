<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_forward"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilos-facturas.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <div class="encabezado__contenedor">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/facturas.png" alt="Facturas">
            <h1 class="encabezado__titulo">Facturas y Pagos</h1>
        </div>
        <div class="encabezado__contenido">
            <p class="encabezado__descripcion">Gestiona tus facturas y controla tus pagos fácilmente.</p>
        </div>
        <%-- Mensajes de éxito --%>
        <c:if test="${param.exito == 'registrada'}">
            <div class="mensaje mensaje--exito">✅ Factura registrada correctamente.</div>
        </c:if>
        <c:if test="${param.exito == 'eliminada'}">
            <div class="mensaje mensaje--exito">🗑️ Factura eliminada correctamente.</div>
        </c:if>
        <c:if test="${param.exito == 'pagada'}">
            <div class="mensaje mensaje--exito">✅ Factura marcada como pagada.</div>
        </c:if>
        <c:if test="${param.error == 'sin_permiso'}">
            <div class="mensaje mensaje--error">⚠️ No tienes permiso para realizar esa acción.</div>
        </c:if>
    </header>
    <main class="facturas">
        <%-- Solo ADMINISTRADOR y COTITULAR pueden registrar --%>
        <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
        <div class="tarjetaAccion tarjetaAccion--azul">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono" src="${pageContext.request.contextPath}/asset/imagenes/agregar_factura.png" alt="Agregar">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Registrar Facturas</h3>
                    <p class="tarjetaAccion__subtitulo">Agrega una nueva factura</p>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/Facturas?accion=form">
                <button class="boton boton--registrar">Crear</button>
            </a>
        </div>
        </c:if>

        <div class="tarjetaAccion tarjetaAccion--verde">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono tarjetaAccion__icono--verde" src="${pageContext.request.contextPath}/asset/imagenes/barra-grafica.png" alt="Consultar">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Consultar Facturas</h3>
                    <p class="tarjetaAccion__subtitulo">Busca y filtra por estado:</p>
                    <div class="contenedorEtiquetas">
                        <span class="etiqueta etiqueta--pendiente">Pendientes</span>
                        <span class="etiqueta etiqueta--pagada">Pagadas</span>
                        <span class="etiqueta etiqueta--vencida">Vencidas</span>
                    </div>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/Facturas?accion=lista">
                <button class="boton boton--cancelar">Ver todas</button>
            </a>
        </div>

        <div class="tarjetaAccion tarjetaAccion--morado">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono tarjetaAccion__icono--morado" src="${pageContext.request.contextPath}/asset/imagenes/reloj.png" alt="Historial">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Historial de Pagos</h3>
                    <p class="tarjetaAccion__subtitulo">Facturas ya pagadas</p>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/Facturas?accion=historial">
                <span class="material-symbols-outlined tarjetaAccion__flecha">arrow_forward</span>
            </a>
        </div>

        <div class="tarjetaAccion tarjetaAccion--gris">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono tarjetaAccion__icono--gris" src="${pageContext.request.contextPath}/asset/imagenes/resumen_rapido.png" alt="Resumen">
                <h3 class="tarjetaAccion__titulo">Resumen rápido</h3>
            </div>
            <div class="resumenPagos">
                <div class="resumenPagos__item">
                    <p class="resumenPagos__numero">${pendientes}</p>
                    <p class="resumenPagos__texto">Pendientes</p>
                </div>
                <div class="resumenPagos__item">
                    <p class="resumenPagos__numero resumenPagos__numero--verde">${pagadas}</p>
                    <p class="resumenPagos__texto">Pagadas</p>
                </div>
                <div class="resumenPagos__item">
                    <p class="resumenPagos__numero resumenPagos__numero--azul">${vencidas}</p>
                    <p class="resumenPagos__texto">Vencidas</p>
                </div>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp" class="tarjetaAccion__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>

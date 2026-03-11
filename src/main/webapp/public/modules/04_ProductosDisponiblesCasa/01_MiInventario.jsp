<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_forward"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/MiInventario.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <div class="encabezado__contenedor">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/mi-inventario.png" alt="Inventario">
        <h1 class="encabezado__titulo">Mi Inventario</h1>
    </div>
    <div class="encabezado__contenido">
        <p class="encabezado__descripcion">Controla tu inventario en casa y actualiza tus listas de compras fácilmente.</p>
    </div>
    <c:if test="${param.error == 'sin_permiso'}">
        <div class="mensaje mensaje--error">⚠️ No tienes permiso para esa acción.</div>
    </c:if>
</header>
<main class="inventario">
    <div class="inventario__acciones">
        <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
        <a href="${pageContext.request.contextPath}/Inventario?accion=registrar" class="inventario__opcion">
            <img class="inventario__opcion-icono" src="${pageContext.request.contextPath}/asset/imagenes/agregar_factura.png" alt="Registrar">
            <h3 class="inventario__opcion-titulo">Registrar productos</h3>
            <p class="inventario__opcion-descripcion">Agrega nuevos productos disponibles</p>
        </a>
        </c:if>
        <a href="${pageContext.request.contextPath}/Inventario?accion=consultar" class="inventario__opcion">
            <img class="inventario__opcion-icono" src="${pageContext.request.contextPath}/asset/imagenes/barra-grafica.png" alt="Consultar">
            <h3 class="inventario__opcion-titulo">Consultar inventario</h3>
            <p class="inventario__opcion-descripcion">Ver y filtrar tus productos</p>
        </a>
    </div>

    <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
    <div class="inventario__tarjeta inventario__tarjeta--principal">
        <img class="inventario__tarjeta-icono" src="${pageContext.request.contextPath}/asset/imagenes/mover-archivo.png" alt="Mover">
        <div class="inventario__tarjeta-contenido">
            <h3 class="inventario__tarjeta-titulo">Mover productos agotados</h3>
            <p class="inventario__tarjeta-descripcion">Envía automáticamente a la lista de compras</p>
        </div>
        <a href="${pageContext.request.contextPath}/Inventario?accion=agotados" class="inventario__boton">
            <button class="boton boton--mover">Mover</button>
        </a>
    </div>
    </c:if>

    <div class="inventario__tarjeta inventario__tarjeta--gris">
        <div class="inventario__tarjeta-encabezado">
            <img class="inventario__tarjeta-icono inventario__tarjeta-icono--gris"
                 src="${pageContext.request.contextPath}/asset/imagenes/resumen_rapido.png" alt="Resumen">
            <h3 class="inventario__tarjeta-titulo">Resumen rápido</h3>
        </div>
        <div class="inventario__resumen">
            <div class="inventario__resumen-item">
                <p class="inventario__resumen-numero">${disponibles}</p>
                <p class="inventario__resumen-texto">Disponibles</p>
            </div>
            <div class="inventario__resumen-item">
                <p class="inventario__resumen-numero inventario__resumen-numero--rojo">${porAgotar}</p>
                <p class="inventario__resumen-texto">Por agotar</p>
            </div>
            <div class="inventario__resumen-item">
                <p class="inventario__resumen-numero inventario__resumen-numero--azul">${categorias}</p>
                <p class="inventario__resumen-texto">Categorías</p>
            </div>
        </div>
    </div>

    <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp" class="inventario__boton">
        <button class="boton boton--volver">Volver</button>
    </a>
</main>
</body>
</html>

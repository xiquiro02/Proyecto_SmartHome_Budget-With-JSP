<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosConsultarInventario.css">
    <title>SmartHome Budget</title>
    <style>
        /* Botón + A Lista estándar */
        .botonALista {
            display: inline-block;
            width: 100%;
            margin-top: 6px;
            padding: 6px 10px;
            border: none;
            border-radius: 8px;
            font-size: 13px;
            font-weight: 600;
            cursor: pointer;
            background: #1565c0;
            color: #fff;
            text-align: center;
            text-decoration: none;
            transition: opacity .15s;
        }
        .botonALista:hover { opacity: .85; }
        /* Resaltado naranja para stock bajo */
        .botonALista--alerta {
            background: #e65100;
            animation: pulsoAlerta 1.6s infinite;
        }
        @keyframes pulsoAlerta {
            0%, 100% { box-shadow: 0 0 0 0 rgba(230,81,0,.4); }
            50%       { box-shadow: 0 0 0 6px rgba(230,81,0,0); }
        }
        .mensajeExitoAuto {
            margin: 0 0 12px;
            padding: 10px 14px;
            background: #e8f5e9;
            border-left: 4px solid #2e7d32;
            border-radius: 8px;
            color: #1b5e20;
            font-size: 14px;
        }
    </style>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Inventario"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Consultar inventario</h1></div>
</header>

<main class="contenedorInventario">

    <%-- Mensaje de éxito tras autoAnadirALista --%>
    <c:if test="${not empty param.exito_auto}">
        <div class="mensajeExitoAuto">✅ ${param.exito_auto}</div>
    </c:if>
    <c:if test="${param.error == 'sin_permiso'}">
        <div class="mensaje mensaje--error">⚠️ No tienes permiso para realizar esa acción.</div>
    </c:if>
    <c:if test="${param.error == 'no_encontrado'}">
        <div class="mensaje mensaje--error">⚠️ Producto no encontrado.</div>
    </c:if>

    <div class="filtros">
        <p class="filtros__titulo">Filtrar por:</p>
        <div class="filtro-grupo">
            <a href="${pageContext.request.contextPath}/Inventario?accion=filtroTipo" class="filtro-enlace">
                Categoría <img class="filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
            </a>
            <a href="${pageContext.request.contextPath}/Inventario?accion=filtroCantidad" class="filtro-enlace">
                Cantidad <img class="filtro-imagen" src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="">
            </a>
        </div>
    </div>

    <div class="gridProductos">
        <c:forEach var="inv" items="${inventario}">
            <article class="tarjetaProducto tarjetaProducto--${inv.colorCSS}">
                <img src="${pageContext.request.contextPath}/asset/imagenes/${inv.iconoProducto}"
                     alt="${inv.nombreProducto}" class="tarjetaProducto__icono">

                <div class="tarjetaProducto__info">
                    <h3 class="tarjetaProducto__nombre">${inv.nombreProducto}</h3>
                    <p class="tarjetaProducto__detalle">Categoría: ${inv.nombreTipoProducto}</p>
                    <p class="tarjetaProducto__detalle">Cantidad: ${inv.cantidad}</p>
                    <p class="tarjetaProducto__detalle">Fecha: ${inv.fechaActualizacionFormateada}</p>

                    <c:if test="${inv.stockBajo && !inv.agotado}">
                        <p class="tarjetaProducto__alerta">⚠️ Stock bajo</p>
                    </c:if>
                    <c:if test="${inv.agotado}">
                        <p class="tarjetaProducto__alerta tarjetaProducto__alerta--rojo">🚨 Agotado</p>
                    </c:if>
                </div>

                <%-- Botón + A Lista: naranja/pulsante si stock bajo, azul normal en otro caso --%>
                <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
                    <a href="${pageContext.request.contextPath}/Inventario?accion=autoAnadirALista&idProducto=${inv.idInventario}"
                       class="botonALista ${inv.stockBajo ? 'botonALista--alerta' : ''}">
                        + A Lista
                    </a>
                </c:if>

                <%-- Acciones editar / eliminar (solo admin/cotitular) --%>
                <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
                    <div class="tarjetaProducto__acciones">
                        <a href="${pageContext.request.contextPath}/Inventario?accion=editar&id=${inv.idInventario}">
                            <button class="botonAccion botonAccion--editar">✏️ Editar</button>
                        </a>
                        <a href="${pageContext.request.contextPath}/Inventario?accion=confirmarEliminar&id=${inv.idInventario}">
                            <button class="botonAccion botonAccion--eliminar">🗑️ Eliminar</button>
                        </a>
                    </div>
                </c:if>
            </article>
        </c:forEach>
    </div>

    <c:if test="${sessionScope.idRol == 1 || sessionScope.idRol == 2}">
        <a href="${pageContext.request.contextPath}/Inventario?accion=registrar" class="boton-NuevoProducto">
            <button class="boton boton--cancelar">+ Nuevo producto</button>
        </a>
    </c:if>
</main>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
// Muestra alerta visual SweetAlert si vienen con exito_auto en la URL
(function () {
    const params = new URLSearchParams(window.location.search);
    const msg = params.get('exito_auto');
    if (msg) {
        Swal.fire({
            icon: 'success',
            title: '¡Listo!',
            text: decodeURIComponent(msg),
            timer: 3000,
            showConfirmButton: false,
            toast: true,
            position: 'top-end'
        });
        // Limpiar parámetro de la URL sin recargar
        const url = new URL(window.location.href);
        url.searchParams.delete('exito_auto');
        history.replaceState(null, '', url.toString());
    }
})();
</script>
</body>
</html>

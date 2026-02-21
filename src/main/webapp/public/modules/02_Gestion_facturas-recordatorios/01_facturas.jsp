<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Link iconos  -->
        <link rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_forward" />
        <link rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_forward" />
        <!-- Link Fuentes -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <!-- Link estilos.css  -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
        <link rel="stylesheet"
            href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilos-facturas.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <div class="encabezado__contenedor">
                <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/facturas.png"
                    alt="Logo de SmartHome Budget">
                <h1 class="encabezado__titulo">Facturas y Pagos</h1>
            </div>
            <div class="encabezado__contenido">
                <p class="encabezado__descripcion">Gestiona tus facturas y controla tus pagos fácilmente.</p>
            </div>
        </header>
        <main class="facturas">
            <div class="tarjetaAccion tarjetaAccion--azul">
                <div class="tarjetaAccion__encabezado">
                    <img class="tarjetaAccion__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/agregar_factura.png" alt="Agregar">
                    <div class="tarjetaAccion__contenido">
                        <h3 class="tarjetaAccion__titulo">Registrar Facturas</h3>
                        <p class="tarjetaAccion__subtitulo">Agrega una nueva factura</p>
                    </div>
                </div>
                <a
                    href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/02_formularioRegistrar-facturas.jsp">
                    <button class="boton boton--registrar">Crear</button>
                </a>
            </div>
            <div class="tarjetaAccion tarjetaAccion--verde">
                <div class="tarjetaAccion__encabezado">
                    <img class="tarjetaAccion__icono tarjetaAccion__icono--verde"
                        src="${pageContext.request.contextPath}/asset/imagenes/barra-grafica.png" alt="Consultar">
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
                <a
                    href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/03_Consultar-Facturas.jsp">
                    <button class="boton boton--cancelar">Ver todas</button>
                </a>
            </div>
            <div class="tarjetaAccion tarjetaAccion--morado">
                <div class="tarjetaAccion__encabezado">
                    <img class="tarjetaAccion__icono tarjetaAccion__icono--morado"
                        src="${pageContext.request.contextPath}/asset/imagenes/reloj.png" alt="Historial">
                    <div class="tarjetaAccion__contenido">
                        <h3 class="tarjetaAccion__titulo">Historial de Pagos</h3>
                        <p class="tarjetaAccion__subtitulo">Facturas ya pagadas</p>
                    </div>
                </div>
                <a
                    href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/11_HistorialPagos.jsp"><span
                        class="material-symbols-outlined tarjetaAccion__flecha">arrow_forward</span></a>
            </div>
            <div class="tarjetaAccion tarjetaAccion--gris">
                <div class="tarjetaAccion__encabezado">
                    <img class="tarjetaAccion__icono tarjetaAccion__icono--gris"
                        src="${pageContext.request.contextPath}/asset/imagenes/resumen_rapido.png" alt="Resumen">
                    <h3 class="tarjetaAccion__titulo">Resumen rápido</h3>
                </div>
                <div class="resumenPagos">
                    <div class="resumenPagos__item">
                        <p class="resumenPagos__numero">2</p>
                        <p class="resumenPagos__texto">Pendientes</p>
                    </div>
                    <div class="resumenPagos__item">
                        <p class="resumenPagos__numero resumenPagos__numero--verde">2</p>
                        <p class="resumenPagos__texto">Pagadas</p>
                    </div>
                    <div class="resumenPagos__item">
                        <p class="resumenPagos__numero resumenPagos__numero--azul">1</p>
                        <p class="resumenPagos__texto">Vencidas</p>
                    </div>
                </div>
            </div>
            <a href="${ pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp"
                class="tarjetaAccion__boton">
                <button class="boton boton--volver">Volver</button>
            </a>
        </main>
    </body>

    </html>
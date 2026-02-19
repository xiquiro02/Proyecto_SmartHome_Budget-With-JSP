<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Link iconos  -->
        <link rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
        <!-- Link Fuentes -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <!-- Link estilos.css  -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/utils/styles.css">
        <link rel="stylesheet"
            href="${pageContext.request.contextPath}/asset/modules/MenuPrincipal/estilosMenuprincipal.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/icono-smarthome.png"
                alt="Logo SmartHome Budget">
            <h1 class="encabezado__titulo">SmartHome Budget</h1>
        </header>
        <main class="menuPrincipal">
            <div class="tarjetasResumen">
                <div class="tarjetaResumen tarjetaResumen--verde">
                    <img class="tarjetaResumen__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/icono-luz.png" alt="Luz">
                    <div class="tarjetaResumen__contenido">
                        <div class="tarjetaResumen__fila">
                            <p class="tarjetaResumen__titulo">Próximo pago:</p>
                            <p class="tarjetaResumen__valor">Factura Luz</p>
                        </div>
                        <div class="tarjetaResumen__fila">
                            <p class="tarjetaResumen__titulo">Vence:</p>
                            <p class="tarjetaResumen__valor--fecha">2 Nov</p>
                        </div>
                    </div>
                </div>
                <div class="tarjetaResumen tarjetaResumen--azul">
                    <img class="tarjetaResumen__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/Presupuesto.png" alt="Presupuesto">
                    <div class="tarjetaResumen__contenido">
                        <p class="tarjetaResumen__titulo">Presupuesto disponible:</p>
                        <p class="tarjetaResumen__valor">$ 200,000</p>
                    </div>
                </div>
            </div>
            <div class="bloqueSaludo">
                <h2 class="bloqueSaludo__titulo">Hola, ???</h2>
                <p class="bloqueSaludo__subtitulo">¿Qué deseas hacer hoy?</p>
            </div>
            <div class="gridOpciones">
                <a href="${pageContext.request.contextPath}/public/modules/02_Gestion_facturas-recordatorios/01_facturas.jsp"
                    class="tarjetaOpcion">
                    <img class="tarjetaOpcion__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/facturas.png" alt="Facturas">
                    <p class="tarjetaOpcion__titulo">Facturas y Pagos</p>
                </a>
                <a href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/01_listasCompras.jsp"
                    class="tarjetaOpcion">
                    <img class="tarjetaOpcion__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/listas-deCompras.png" alt="Listas">
                    <p class="tarjetaOpcion__titulo">Listas de Compras</p>
                </a>
                <a href="${pageContext.request.contextPath}/public/modules/04_ProductosDisponiblesCasa/01_MiInventario.jsp"
                    class="tarjetaOpcion">
                    <img class="tarjetaOpcion__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/mi-inventario.png" alt="Inventario">
                    <p class="tarjetaOpcion__titulo">Mi Inventario</p>
                </a>
                <a href="${pageContext.request.contextPath}/public/modules/06_Finanzas/01_Finanzas.jsp"
                    class="tarjetaOpcion">
                    <img class="tarjetaOpcion__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/finanzas.png" alt="Finanzas">
                    <p class="tarjetaOpcion__titulo">Finanzas</p>
                </a>
                <a href="${pageContext.request.contextPath}/public/modules/05_TiendasCercanas/01_TiendasCercanas.jsp"
                    class="tarjetaOpcion">
                    <img class="tarjetaOpcion__icono" src="${pageContext.request.contextPath}/asset/imagenes/tienda.png"
                        alt="Tiendas">
                    <p class="tarjetaOpcion__titulo">Tiendas Cercanas</p>
                </a>
                <a href="${pageContext.request.contextPath}/public/modules/06_Ajustes/01_ajustes.jsp"
                    class="tarjetaOpcion">
                    <img class="tarjetaOpcion__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/ajustes.png" alt="Ajustes">
                    <p class="tarjetaOpcion__titulo">Ajustes</p>
                </a>
            </div>
        </main>
        <nav class="navInferior">
            <a href="#inicio" class="navInferior__item navInferior__item--activo">
                <img class="navInferior__icono" src="${pageContext.request.contextPath}/asset/imagenes/hogar.png"
                    alt="Inicio">
                <p>Inicio</p>
            </a>
            <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/02_notificacionesResumen-Filtro-Todas.jsp"
                class="navInferior__item">
                <img class="navInferior__icono"
                    src="${pageContext.request.contextPath}/asset/imagenes/notificaciones.png" alt="Recordatorios">
                <p>Recordatorios</p>
            </a>
            <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/19_MiPerfil.jsp"
                class="navInferior__item">
                <img class="navInferior__icono" src="${pageContext.request.contextPath}/asset/imagenes/usuario.png"
                    alt="Perfil">
                <p>Perfil</p>
            </a>
        </nav>
    </body>

    </html>
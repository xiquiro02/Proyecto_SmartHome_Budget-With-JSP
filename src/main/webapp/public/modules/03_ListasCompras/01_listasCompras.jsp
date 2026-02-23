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
            href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilos_listasCompras.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <div class="encabezado__contenedor">
                <img class="encabezado__imagen"
                    src="${pageContext.request.contextPath}/asset/imagenes/lista-compra.png"
                    alt="Icono de Lista de compras">
                <h1 class="encabezado__titulo">Listas de Compras</h1>
            </div>
            <div class="listas__busqueda">
                <input type="search" class="listas__barra" placeholder=" 🔍 Buscar lista por nombre o categoría">
            </div>
        </header>
        <main class="listas">
            <h2 class="listas__titulo">Tus Listas</h2>
            <div class="listas__contenido">
                <div class="lista-card lista-card--verde">
                    <div class="lista-card__header">
                        <div class="lista-card__info-box">
                            <h3 class="lista-card__titulo lista-card__titulo--verde">Mercado mensual</h3>
                            <p class="lista-card__info">Categoría: Alimentos</p>
                            <p class="lista-card__info">Productos: 12</p>
                            <p class="lista-card__info">Creada: 10 Feb 2025</p>
                        </div>
                        <div class="lista-card__estado">
                            <span class="lista-card__tag lista-card__tag--completa">Completa</span>
                            <a href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/14_VerDetallesMercado-mensual.jsp"
                                class="lista-card__ver-detalles">👁 Ver
                                detalles</a>
                        </div>
                    </div>
                    <div class="lista-card__acciones">
                        <a
                            href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/08_AgregarProductos.jsp"><button
                                class="boton boton--agregar">➕ Agregar
                                producto</button></a>
                        <a
                            href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/04_EditarListaCompras.jsp"><button
                                class="boton boton--editar">✏️ Editar</button></a>
                        <a
                            href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/06_EliminarLista.jsp"><button
                                class="boton boton--Eliminar">🗑️ Eliminar</button></a>
                    </div>
                </div>
                <div class="lista-card lista-card--naranja">
                    <div class="lista-card__header">
                        <div class="lista-card__info-box">
                            <h3 class="lista-card__titulo lista-card__titulo--naranja">Aseo del hogar</h3>
                            <p class="lista-card__info">Categoría: Aseo</p>
                            <p class="lista-card__info">Productos: 8</p>
                            <p class="lista-card__info">Creada: 12 Feb 2025</p>
                        </div>
                        <div class="lista-card__estado">
                            <span class="lista-card__tag lista-card__tag--proceso">En proceso</span>
                            <a href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/15_VerDetallesAseo-hogar.jsp"
                                class="lista-card__ver-detalles">👁 Ver detalles</a>
                        </div>
                    </div>
                    <div class="lista-card__acciones">
                        <a
                            href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/08_AgregarProductos.jsp"><button
                                class="boton boton--agregar">➕ Agregar
                                producto</button></a>
                        <a
                            href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/04_EditarListaCompras.jsp"><button
                                class="boton boton--editar">✏️ Editar</button></a>
                        <a
                            href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/06_EliminarLista.jsp"><button
                                class="boton boton--Eliminar">🗑️ Eliminar</button></a>
                    </div>
                </div>
                <div class="lista-card lista-card--rojo">
                    <div class="lista-card__header">
                        <div class="lista-card__info-box">
                            <h3 class="lista-card__titulo lista-card__titulo--rojo">Útiles escolares</h3>
                            <p class="lista-card__info">Categoría: Otros</p>
                            <p class="lista-card__info">Productos: 5</p>
                            <p class="lista-card__info">Creada: 20 Ene 2025</p>
                        </div>
                        <div class="lista-card__estado">
                            <span class="lista-card__tag lista-card__tag--pendiente">Pendiente</span>
                            <a href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/16_VerDetallesUtiles-escolares.jsp"
                                class="lista-card__ver-detalles">👁 Ver
                                detalles</a>
                        </div>
                    </div>
                    <div class="lista-card__acciones">
                        <a
                            href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/08_AgregarProductos.jsp"><button
                                class="boton boton--agregar">➕ Agregar
                                producto</button></a>
                        <a
                            href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/04_EditarListaCompras.jsp"><button
                                class="boton boton--editar">✏️ Editar</button></a>
                        <a
                            href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/06_EliminarLista.jsp"><button
                                class="boton boton--Eliminar">🗑️ Eliminar</button></a>
                    </div>
                </div>
            </div>
            <div class="listas__botones">
                <a href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/02_CrearLista.jsp"
                    class="listas__boton">
                    <button class="boton boton--registrar">➕ Crear nueva lista</button>
                </a>
                <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp"
                    class="listas__boton">
                    <button class="boton boton--volver">Volver</button>
                </a>
            </div>
        </main>
    </body>

    </html>
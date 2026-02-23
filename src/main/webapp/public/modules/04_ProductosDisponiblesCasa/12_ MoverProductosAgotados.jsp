<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Link iconos  -->
        <link rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
        <!-- Link Fuentes -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <!-- Link estilos.css  -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
        <link rel="stylesheet"
            href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosMoverProductosAgotados.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png"
                alt="Logo de SmartHome Budget">
            <a href="${pageContext.request.contextPath}/public/modules/04_ProductosDisponiblesCasa/01_MiInventario.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
            <div class="encabezado__contenedorTitulo">
                <h1 class="encabezado__titulo">Productos agotados</h1>
            </div>
        </header>
        <main class="contenedorProductosAgotados">
            <div class="descripcion">
                <p class="descripcion__texto">
                    Revisa los productos que ya se agotaron y agrégalos fácilmente a una de tus listas de compras.
                </p>
            </div>

            <div class="gridProductos">
                <article class="tarjetaProducto tarjetaProducto--morado">
                    <img src="${pageContext.request.contextPath}/asset/imagenes/maquillaje.png" alt="Crema"
                        class="tarjetaProducto__icono">
                    <div class="tarjetaProducto__info">
                        <h3 class="tarjetaProducto__nombre">Shampoo</h3>
                        <p class="tarjetaProducto__detalle">Categoría: Personal</p>
                        <p class="tarjetaProducto__detalle">Cantidad: 0 unidad</p>
                    </div>
                    <div class="tarjetaProducto__acciones">
                        <a
                            href="${pageContext.request.contextPath}/public/modules/04_ProductosDisponiblesCasa/13_MoverA-Lista.jsp"><button
                                class="botonAccion botonAccion--mover">🛒 Mover a
                                lista</button></a>
                    </div>
                </article>

                <article class="tarjetaProducto tarjetaProducto--azul">
                    <img src="${pageContext.request.contextPath}/asset/imagenes/Aseo.png" alt="Jabón"
                        class="tarjetaProducto__icono">
                    <div class="tarjetaProducto__info">
                        <h3 class="tarjetaProducto__nombre">Jabón</h3>
                        <p class="tarjetaProducto__detalle">Categoría: Aseo</p>
                        <p class="tarjetaProducto__detalle">Cantidad: 0 unidad</p>
                    </div>
                    <div class="tarjetaProducto__acciones">
                        <a
                            href="${pageContext.request.contextPath}/public/modules/04_ProductosDisponiblesCasa/13_MoverA-Lista.jsp"><button
                                class="botonAccion botonAccion--mover">🛒 Mover a
                                lista</button></a>
                    </div>
                </article>

                <article class="tarjetaProducto tarjetaProducto--gris">
                    <img src="${pageContext.request.contextPath}/asset/imagenes/cajas.png" alt="Pilas AAA"
                        class="tarjetaProducto__icono">
                    <div class="tarjetaProducto__info">
                        <h3 class="tarjetaProducto__nombre">Pilas AAA</h3>
                        <p class="tarjetaProducto__detalle">Categoría: Otros</p>
                        <p class="tarjetaProducto__detalle">Cantidad: 0 unidad</p>
                    </div>
                    <div class="tarjetaProducto__acciones">
                        <a
                            href="${pageContext.request.contextPath}/public/modules/04_ProductosDisponiblesCasa/13_MoverA-Lista.jsp"><button
                                class="botonAccion botonAccion--mover">🛒 Mover a
                                lista</button></a>
                    </div>
                </article>

                <article class="tarjetaProducto tarjetaProducto--verde">
                    <img src="${pageContext.request.contextPath}/asset/imagenes/alimentos-saludables.png" alt="Sal"
                        class="tarjetaProducto__icono">
                    <div class="tarjetaProducto__info">
                        <h3 class="tarjetaProducto__nombre">Sal</h3>
                        <p class="tarjetaProducto__detalle">Categoría: Alimentos</p>
                        <p class="tarjetaProducto__detalle">Cantidad: 0 unidad</p>
                    </div>
                    <div class="tarjetaProducto__acciones">
                        <a
                            href="${pageContext.request.contextPath}/public/modules/04_ProductosDisponiblesCasa/13_MoverA-Lista.jsp"><button
                                class="botonAccion botonAccion--mover">🛒 Mover a
                                lista</button></a>
                    </div>
                </article>
            </div>

            <div class="resumen">
                <p class="resumen__numero">4 Productos agotados</p>
                <p class="resumen__descripcion">Listos para agregar a tus listas</p>
            </div>

            <a href="${pageContext.request.contextPath}/public/modules/04_ProductosDisponiblesCasa/01_MiInventario.jsp"
                class="boton-NuevoProducto">
                <button class="boton boton--cancelar">Volver</button>
            </a>
        </main>
    </body>

    </html>
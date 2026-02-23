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
            href="${pageContext.request.contextPath}/asset/css/modules/05_TiendasCercanas/estilosLista-tiendasCercanas.css">
        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo redondo.png"
                alt="Logo de SmartHome Budget">
            <a href="${pageContext.request.contextPath}/public/modules/05_TiendasCercanas/01_TiendasCercanas.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
            <div class="encabezado__contenedorTitulo">
                <h1 class="encabezado__titulo">Tiendas cercanas</h1>
            </div>
        </header>
        <main class="tiendasCercanas">
            <div class="tiendasCercanas__ubicacion">
                <div class="ubicacion">
                    <span class="ubicacion__icono">📍</span>
                    <span class="ubicacion__texto">Ubicación: Cabecera</span>
                </div>
                <a href="${pageContext.request.contextPath}/public/modules/05_TiendasCercanas/02_Ubicacion.jsp"><button
                        class="ubicacion__boton">🔁 Actualizar ubicación</button></a>
            </div>
            <div class="tiendasCercanas__filtros">
                <p class="tiendasCercanas__filtro-label">Filtrar por:</p>
                <div class="tiendasCercanas__filtro-grupo">
                    <a href="##" class="tiendasCercanas__filtro-enlace">
                        Supermercados
                        <img class="tiendasCercanas__filtro-imagen"
                            src="${pageContext.request.contextPath}/asset/imagenes/supermercado.png"
                            alt="Icono filtros">
                    </a>
                    <a href="##" class="tiendasCercanas__filtro-enlace" alt="Icono filtros">
                        Tiendas
                        <img class="tiendasCercanas__filtro-imagen"
                            src="${pageContext.request.contextPath}/asset/imagenes/tienda.png" alt="Icono filtros">
                    </a>
                    <a href="##" class="tiendasCercanas__filtro-enlace" alt="Icono filtros">
                        Restaurantes
                        <img class="tiendasCercanas__filtro-imagen"
                            src="${pageContext.request.contextPath}/asset/imagenes/mercado.png" alt="Icono filtros">
                    </a>
                </div>
            </div>

            <div class="tiendaLista">
                <a href="${pageContext.request.contextPath}/public/modules/05_TiendasCercanas/07_InformacionTienda.jsp"
                    class="tiendaInformacion">
                    <div class="tiendaCard tiendaCard--azul">
                        <div class="tiendaCard__borde"></div>
                        <div class="tiendaCard__contenido">
                            <div class="tiendaCard__encabezado">
                                <h3 class="tiendaCard__titulo">Carrullo Cabecera</h3>
                                <span class="tiendaCard__distancia">📍 2.3 km</span>
                            </div>
                            <div class="tiendaCard__info">
                                <p class="tiendaCard__horario">⏰ Abierto hasta 9:00 PM</p>
                                <div class="tiendaCard__calificacion">
                                    <span class="tiendaCard__estrellas">⭐</span>
                                    <span class="tiendaCard__puntuacion">4.5</span>
                                </div>
                            </div>
                            <a href="##" class="tiendaCard__boton"><button class="boton boton--mapa">🗺️ Ver en
                                    mapa</button></a>
                        </div>
                    </div>
                </a>
                <div class="tiendaCard tiendaCard--verde">
                    <div class="tiendaCard__borde"></div>
                    <div class="tiendaCard__contenido">
                        <div class="tiendaCard__encabezado">
                            <h3 class="tiendaCard__titulo">Éxito Vecino</h3>
                            <span class="tiendaCard__distancia">📍 1.8 km</span>
                        </div>
                        <div class="tiendaCard__info">
                            <p class="tiendaCard__horario">⏰ Abierto hasta 8:30 PM</p>
                            <div class="tiendaCard__calificacion">
                                <span class="tiendaCard__estrellas">⭐</span>
                                <span class="tiendaCard__puntuacion">4.2</span>
                            </div>
                        </div>
                        <a href="##" class="tiendaCard__boton"><button class="boton boton--mapa">🗺️ Ver en
                                mapa</button></a>
                    </div>
                </div>
                <div class="tiendaCard tiendaCard--morado">
                    <div class="tiendaCard__borde"></div>
                    <div class="tiendaCard__contenido">
                        <div class="tiendaCard__encabezado">
                            <h3 class="tiendaCard__titulo">Plaza de Mercado La 27</h3>
                            <span class="tiendaCard__distancia">📍 3.2 km</span>
                        </div>
                        <div class="tiendaCard__info">
                            <p class="tiendaCard__horario tiendaCard__horario--rojo">⏰ Cierra 6:00 PM</p>
                            <div class="tiendaCard__calificacion">
                                <span class="tiendaCard__estrellas">⭐</span>
                                <span class="tiendaCard__puntuacion">4.8</span>
                            </div>
                        </div>
                        <a href="##" class="tiendaCard__boton"><button class="boton boton--mapa">🗺️ Ver en
                                mapa</button></a>
                    </div>
                </div>
            </div>
            <a href="#" class="tiendasCercanas__boton">
                <button class="boton boton--registrar">Ver mapa completo</button>
            </a>
            <a href="${pageContext.request.contextPath}/public/modules/05_TiendasCercanas/01_TiendasCercanas.jsp"
                class="tiendasCercanas__boton">
                <button class="boton boton--volver">Volver</button>
            </a>
        </main>
    </body>

    </html>
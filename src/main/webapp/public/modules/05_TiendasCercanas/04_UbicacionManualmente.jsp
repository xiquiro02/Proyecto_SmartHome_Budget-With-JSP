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
            href="${pageContext.request.contextPath}/asset/css/modules/05_TiendasCercanas/estilosUbicacionManualmente.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png"
                alt="Logo de SmartHome Budget">
            <a href="${pageContext.request.contextPath}/public/modules/05_TiendasCercanas/01_TiendasCercanas.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
            <div class="encabezado__contenedorTitulo">
                <h1 class="encabezado__titulo">Ubicación</h1>
            </div>
        </header>
        <main class="confirmacion">
            <div class="confirmacion__contenedor">
                <div class="confirmacion__img">
                    <img class="confirmacion__icono" src="${pageContext.request.contextPath}/asset/imagenes/lapiz.png"
                        alt="Icono de confirmacion">
                </div>
                <p class="confirmacion__titulo">Ingresar ubicación </p>

                <div class="resumen">
                    <div class="resumen__campo">
                        <img class="resumen__icono"
                            src="${pageContext.request.contextPath}/asset/imagenes/paseo-por-la-ciudad.png"
                            alt="Icono Nombre">
                        <div class="resumen__info">
                            <label class="resumen__etiqueta">Ciudad:</label>
                            <input type="text" class="resumen__input" placeholder="Ej: Cabecera">
                        </div>
                    </div>

                    <div class="resumen__campo">
                        <img class="resumen__icono" src="${pageContext.request.contextPath}/asset/imagenes/pueblo.png"
                            alt="Icono Categoría">
                        <div class="resumen__info">
                            <label class="resumen__etiqueta">Barrio:</label>
                            <input type="text" class="resumen__input" placeholder="Ej: Cabecera">
                        </div>
                    </div>

                    <div class="resumen__campo">
                        <img class="resumen__icono"
                            src="${pageContext.request.contextPath}/asset/imagenes/direccion.png" alt="Icono Categoría">
                        <div class="resumen__info">
                            <label class="resumen__etiqueta">Dirección:</label>
                            <input type="text" class="resumen__input" placeholder="Ej: Cl. 45 #29-24">
                        </div>
                    </div>
                </div>

                <a href="${pageContext.request.contextPath}/public/modules/05_TiendasCercanas/05_UbicacionGuardada-manualmente.jsp"
                    class="confirmacion__botones">
                    <button type="submit" class="boton boton--registrar ">Guardar ubicación</button>
                </a>
                <a href="${pageContext.request.contextPath}/public/modules/05_TiendasCercanas/01_TiendasCercanas.jsp"
                    class="confirmacion__botones">
                    <button type="button" class="boton boton--cancelar">Cancelar</button>
                </a>
            </div>
        </main>
    </body>

    </html>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Link iconos  -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
    <!-- Link Fuentes -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <!-- Link estilos.css  -->
    <link rel="stylesheet" href="../../../asset/utils/styles.css">
    <link rel="stylesheet" href="../../../asset/modules/05_TiendasCercanas/estilosCompararPrecios.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="../../../asset/imagenes/Logo redondo.png" alt="Logo de SmartHome Budget">
        <a href="01_TiendasCercanas.html">
            <span class="material-symbols-outlined"> arrow_back_ios_new </span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Comparar precios</h1>
        </div>
    </header>
    <main class="compararPrecios">
        <div class="productoSeleccionado">
            <p class="productoSeleccionado__label">Producto seleccionado:</p>
            <div class="productoSeleccionado__info">
                <h2 class="productoSeleccionado__nombre">Arroz 1 kg</h2>
                <span class="productoSeleccionado__categoria">Alimentos</span>
            </div>
        </div>
        <div class="comparacionTitulo">
            <img class="comparacionTitulo__icono" src="../../../asset/imagenes/equilibrar.png" alt="Icono Información">
            <h3 class="comparacionTitulo__texto">Comparación entre tiendas</h3>
        </div>
        <div class="tiendaComparacion">
            <div class="tiendaCard tiendaCard--azul">
                <div class="tiendaCard__borde"></div>
                <div class="tiendaCard__contenido">
                    <div class="tiendaCard__encabezado">
                        <span class="tiendaCard__icono">🛍️</span>
                        <h4 class="tiendaCard__nombre">Éxito Cabecera</h4>
                    </div>
                    <div class="tiendaCard__precio">
                        <span class="tiendaCard__label">Precio:</span>
                        <span class="tiendaCard__valor">5,200 COP</span>
                    </div>
                </div>
            </div>
            <div class="tiendaCard tiendaCard--verde">
                <div class="tiendaCard__borde"></div>
                <div class="tiendaCard__contenido">
                    <div class="tiendaCard__encabezado">
                        <span class="tiendaCard__icono">🛍️</span>
                        <h4 class="tiendaCard__nombre">Olímpica La Flora</h4>
                    </div>
                    <div class="tiendaCard__precio">
                        <span class="tiendaCard__label">Precio:</span>
                        <span class="tiendaCard__valor">4,900 COP</span>
                    </div>
                </div>
            </div>
            <div class="tiendaCard tiendaCard--morado">
                <div class="tiendaCard__borde"></div>
                <div class="tiendaCard__contenido">
                    <div class="tiendaCard__encabezado">
                        <span class="tiendaCard__icono">🛍️</span>
                        <h4 class="tiendaCard__nombre">Tienda Don Pepe</h4>
                    </div>
                    <div class="tiendaCard__precio">
                        <span class="tiendaCard__label">Precio:</span>
                        <span class="tiendaCard__valor">4,500 COP</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="opcionEconomica">
            <div class="opcionEconomica__encabezado">
                <span class="opcionEconomica__icono">📊</span>
                <h3 class="opcionEconomica__titulo">Opción más económica</h3>
            </div>
            <div class="opcionEconomica__tarjeta">
                <span class="opcionEconomica__tiendaIcono">💰</span>
                <div class="opcionEconomica__info">
                    <h4 class="opcionEconomica__nombre">Tienda Don Pepe</h4>
                    <p class="opcionEconomica__precio">4,500 COP</p>
                    <p class="opcionEconomica__ahorro">Ahorras 700 COP respecto a la tienda más costosa</p>
                </div>
            </div>
        </div>
        <a href="##" class="compararPrecios__boton">
            <button class="boton boton--registrar">Ver mapa completo</button>
        </a>
        <a href="01_TiendasCercanas.html" class="compararPrecios__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>
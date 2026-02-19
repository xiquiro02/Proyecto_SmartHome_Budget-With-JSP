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
    <link rel="stylesheet" href="../../../asset/modules/05_TiendasCercanas/estilosTiendasCercanas.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <div class="encabezado__contenedor">
            <img class="encabezado__imagen" src="../../../asset/imagenes/tienda.png" alt="Logo de Tiendas Cercanas">
            <h1 class="encabezado__titulo">Tiendas Cercanas</h1>
        </div>
        <div class="encabezado__contenido">
            <p class="encabezado__descripcion">Encuentra tiendas cercanas y recibe recomendaciones según tu ubicación.</p>
        </div>
        <div class="tiendas__busqueda">
            <input type="search" class="tiendas__barra" placeholder=" 🔍 Buscar Tiendas...">
        </div>
    </header>
    <main class="tiendas">
        <div class="tarjetaAccion tarjetaAccion--azul">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono" src="../../../asset/imagenes/agregar_factura.png" alt="Ubicación">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Registrar Ubicación</h3>
                    <p class="tarjetaAccion__subtitulo">El sistema necesita tu ubicación para mostrarte tiendas cercanas.</p>
                    <ul class="tarjetaAccion__lista">
                        <li>• Puedes permitir acceso al GPS</li>
                        <li>• Ingresar una ubicación manualmente</li>
                    </ul>
                </div>
            </div>
            <div class="tarjetaAccion__botones">
                <a href="02_Ubicacion.html">
                    <button class="boton boton--editar">Usar mi ubicación</button>
                </a>
                <a href="04_UbicacionManualmente.html">
                    <button class="boton boton--marcar">Ingresar ubicación</button>
                </a>
            </div>
        </div>
        <div class="tarjetaAccion tarjetaAccion--verde">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono tarjetaAccion__icono--verde" src="../../../asset/imagenes/shopping-list.png" alt="Lista">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Lista de Tiendas Cercanas</h3>
                    <p class="tarjetaAccion__subtitulo">Ver lista completa de tiendas según tu ubicación.</p>
                </div>
            </div>
            <a href="06_Lista-tiendasCercanas.html">
                <button class="boton boton--VerLista">Ver lista</button>
            </a>
        </div>
        <div class="tarjetaAccion tarjetaAccion--morado">
            <div class="tarjetaAccion__encabezado">
                <img class="tarjetaAccion__icono tarjetaAccion__icono--morado" src="../../../asset/imagenes/comparar.png" alt="Comparar">
                <div class="tarjetaAccion__contenido">
                    <h3 class="tarjetaAccion__titulo">Comparar Precios de Productos</h3>
                    <p class="tarjetaAccion__subtitulo">Compara precios de productos entre varias tiendas.</p>
                </div>
            </div>
            <a href="08_CompararPrecios.html">
                <button class="boton boton--resumen">Comparar</button>
            </a>
        </div>
        <a href="../Menu principal/01_menu_principal.html" class="tiendas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>
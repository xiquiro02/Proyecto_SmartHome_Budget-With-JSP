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
    <link rel="stylesheet" href="../../../asset/modules/04_ProductosDisponiblesCasa/estilosConsultarInventario-Vacio.css">

    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="../../../asset/imagenes/Logo redondo.png" alt="Logo de SmartHome Budget">
        <a href="01_MiInventario.html">
            <span class="material-symbols-outlined"> arrow_back_ios_new </span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Consultar inventario</h1>
        </div>
    </header>
    <main class="contenedorInventario">
        <div class="buscador">
            <input type="search" class="buscador__input" placeholder="🔍 Buscar producto…">
        </div>

        <div class="filtros">
            <p class="filtros__titulo">Filtrar por:</p>
            <div class="filtro-grupo">
                <a href="##" class="filtro-enlace">
                    Categoría
                    <img class="filtro-imagen" src="../../../asset/imagenes/flecha.png" alt="Icono filtros">
                </a>
                <a href="##" class="filtro-enlace" alt="Icono filtros">
                    Cantidad
                    <img class="filtro-imagen" src="../../../asset/imagenes/flecha.png" alt="Icono filtros">
                </a>
            </div>
        </div>

        <div class="estadoVacio">
            <img src="../../../asset/imagenes/Caja-vacia.png" alt="Caja vacía" class="estadoVacio__imagen">
            <p class="estadoVacio__textoTitulo">No tienes productos en tu inventario todavía</p>
            <p class="estadoVacio__textoDescripcion">¡Agrega tus artículos del hogar para llevar el control fácilmente!</p>
        </div>

        <a href="02_RegistrarProductoDisponibles.html" class="boton-NuevoProducto">
            <button class="boton boton--cancelar">+ Nuevo producto</button>
        </a>

    </main>
</body>
</html>
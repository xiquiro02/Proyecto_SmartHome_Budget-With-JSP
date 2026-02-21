<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Link iconos  -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
    <!-- Link Fuentes -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <!-- Link estilos.css  -->
    <link rel="stylesheet" href="../../../asset/utils/styles.css">
    <link rel="stylesheet" href="../../../asset/modules/06_Finanzas/estilosDetalleIngresos.css">

    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="../../../asset/imagenes/Logo redondo.png" alt="Logo de SmartHome Budget">
        <a href="01_Finanzas.html">
            <span class="material-symbols-outlined"> arrow_back_ios_new </span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Detalle de Ingresos</h1>
        </div>
    </header>
    <main class="consultarFacturas">
        <div class="consultarFacturas__busqueda">
            <input type="search" class="consultarFacturas__busqueda-input" placeholder="🔍 Buscar ingreso..">
        </div>

        <div class="facturaLista">
            <div class="facturaCard facturaCard--verde">
                <div class="facturaCard__borde"></div>
                <div class="facturaCard__contenido">
                    <div class="facturaCard__encabezado">
                        <h3 class="facturaCard__titulo">Salario</h3>
                        <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">$ 1.000.000</span>
                    </div>
                    <p class="facturaCard__detalles">01 Mar 2026</p>
                </div>
            </div>  
        </div>

        <div class="facturaLista">
            <div class="facturaCard facturaCard--verde">
                <div class="facturaCard__borde"></div>
                <div class="facturaCard__contenido">
                    <div class="facturaCard__encabezado">
                        <h3 class="facturaCard__titulo">Venta ocasional</h3>
                        <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">$ 200.000</span>
                    </div>
                    <p class="facturaCard__detalles">10 Mar 2026</p>
                </div>
            </div>  
        </div>

        <div class="totalPagado">
            <h3 class="totalPagado__titulo">Total ingresos</h3>
            <p class="totalPagado__monto">$ 1.200.000</p>
        </div>

        <a href="02_RegistrarIngresos.html" class="consultarFacturas__boton">
            <button class="boton boton--registrar">+ Registrar nuevo ingreso</button>
        </a>
        <a href="01_Finanzas.html" class="consultarFacturas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>
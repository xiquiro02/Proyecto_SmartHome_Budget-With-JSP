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
    <link rel="stylesheet" href="../../../asset/modules/02_Gestion_facturas-recordatorios/estilosHistorialPagos.css">

    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="../../../asset/imagenes/Logo redondo.png" alt="Logo de SmartHome Budget">
        <a href="01_facturas.html">
            <span class="material-symbols-outlined"> arrow_back_ios_new </span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Historial de Pagos</h1>
        </div>
    </header>
    <main class="consultarFacturas">
        <div class="consultarFacturas__descripcion">
            <p class="consultarFacturas__parrafo">
                Aquí podrás ver todas las facturas que ya marcaste 
                como pagadas. Puedes consultar la fecha en que pagaste 
                y el monto correspondiente.
            </p>
        </div>
        <div class="consultarFacturas__filtros">
            <p class="consultarFacturas__filtro-label">Filtrar por:</p>
            <div class="consultarFacturas__filtro-grupo">
                <a href="12_HistorialPagos-Filtrar-categoria.html" class="consultarFacturas__filtro-enlace">
                    Categoría
                    <img class="consultarFacturas__filtro-imagen" src="../../../asset/imagenes/flecha.png" alt="Icono filtros">
                </a>
                <a href="13_HistorialPagos-Filtrar-Fecha.html" class="consultarFacturas__filtro-enlace" alt="Icono filtros">
                    Fecha
                    <img class="consultarFacturas__filtro-imagen" src="../../../asset/imagenes/flecha.png" alt="Icono filtros">
                </a>
                <a href="14_HistorialPagos-Filtrar-Monto.html" class="consultarFacturas__filtro-enlace" alt="Icono filtros">
                    Monto
                    <img class="consultarFacturas__filtro-imagen" src="../../../asset/imagenes/flecha.png" alt="Icono filtros">
                </a>
            </div>
        </div>
        <div class="facturaLista">
            <div class="facturaCard facturaCard--verde">
                <div class="facturaCard__borde"></div>
                <div class="facturaCard__contenido">
                    <div class="facturaCard__encabezado">
                        <h3 class="facturaCard__titulo">Agua</h3>
                        <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">Pagada</span>
                    </div>
                    <p class="facturaCard__detalles">Monto: $ 55.000</p>
                    <p class="facturaCard__detalles">Pagado el: 15 Oct 2025</p>
                </div>
            </div>  
        </div>
        <div class="facturaLista">
            <div class="facturaCard facturaCard--verde">
                <div class="facturaCard__borde"></div>
                <div class="facturaCard__contenido">
                    <div class="facturaCard__encabezado">
                        <h3 class="facturaCard__titulo">Arriendo</h3>
                        <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">Pagada</span>
                    </div>
                    <p class="facturaCard__detalles">Monto: $ 550.000</p>
                    <p class="facturaCard__detalles">Pagado el: 25 Oct 2025</p>
                </div>
            </div>  
        </div>
        <div class="facturaLista">
            <div class="facturaCard facturaCard--verde">
                <div class="facturaCard__borde"></div>
                <div class="facturaCard__contenido">
                    <div class="facturaCard__encabezado">
                        <h3 class="facturaCard__titulo">Luz</h3>
                        <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">Pagada</span>
                    </div>
                    <p class="facturaCard__detalles">Monto: $ 25.000</p>
                    <p class="facturaCard__detalles">Pagado el: 5 Nov 2025</p>
                </div>
            </div>  
        </div>

        <div class="totalPagado">
            <h3 class="totalPagado__titulo">Total pagado</h3>
            <p class="totalPagado__monto">$ 630.000</p>
            <p class="totalPagado__texto">3 facturas procesadas</p>
        </div>

        <a href="##" class="consultarFacturas__boton">
            <button class="boton boton--resumen">Ver resumen financiero</button>
        </a>
        <a href="01_facturas.html" class="consultarFacturas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
</body>
</html>
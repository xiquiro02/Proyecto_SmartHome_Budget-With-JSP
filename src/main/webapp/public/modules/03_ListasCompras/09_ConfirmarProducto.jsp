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
    <link rel="stylesheet" href="../../../asset/modules/03_ListasCompras/estilosConfirmarProducto.css">

    <title>SmartHome Budget</title>
</head>
<body>
    <main class="confirmacion">
        <div class="confirmacion__contenedor">
            <div class="confirmacion__img">
                <img class="confirmacion__icono" src="../../../asset/imagenes/icono_confirmacion.png" alt="Icono de confirmacion">
            </div>
            <p class="confirmacion__titulo">Producto agregado correctamente</p>

            <div class="resumen">
                <div class="resumen__campo">
                    <img class="resumen__icono" src="../../../asset/imagenes/elementos-de-la-lista.png" alt="Icono Nombre">
                    <div class="resumen__info">
                        <label class="resumen__etiqueta">Nombre:</label>
                        <input type="text" class="resumen__input" value="Cepillo de dientes" readonly>
                    </div>
                </div>

                <div class="resumen__campo">
                    <img class="resumen__icono" src="../../../asset/imagenes/compensacion.png" alt="Icono Categoría">
                    <div class="resumen__info">
                        <label class="resumen__etiqueta">Cantidad:</label>
                        <input type="text" class="resumen__input" value="2" readonly>
                    </div>
                </div>

                <div class="resumen__campo">
                    <img class="resumen__icono" src="../../../asset/imagenes/productos.png" alt="Icono Categoría">
                    <div class="resumen__info">
                        <label class="resumen__etiqueta">Tipo:</label>
                        <input type="text" class="resumen__input" value="Aseo" readonly>
                    </div>
                </div>

                <div class="resumen__campo">
                    <img class="resumen__icono" src="../../../asset/imagenes/fecha.png" alt="Icono Creacion">
                    <div class="resumen__info">
                        <label class="resumen__etiqueta">Fecha:</label>
                        <input type="text" class="resumen__input" value="Hoy" readonly>
                    </div>
                </div>
            </div>

            <a href="08_AgregarProductos.html" class="confirmacion__botones">
                <button type="submit" class="boton boton--registrar ">Añadir otro producto</button>
            </a>
            <a href="01_listasCompras.html" class="confirmacion__botones">
                <button type="button" class="boton boton--volver">Volver</button>
            </a>
        </div>
    </main>
</body>
</html>
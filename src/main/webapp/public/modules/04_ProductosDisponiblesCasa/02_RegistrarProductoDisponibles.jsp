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
    <link rel="stylesheet" href="../../../asset/modules/04_ProductosDisponiblesCasa/estilosRegistrarProductosDisponibles.css">

    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="../../../asset/imagenes/Logo redondo.png" alt="Logo de SmartHome Budget">
        <a href="01_MiInventario.html">
            <span class="material-symbols-outlined"> arrow_back_ios_new </span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Registrar productos</h1>
        </div>
    </header>
    <main class="formulario">
        <div class="formulario__contenedor">
            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="nombreProducto">Nombre del producto:</label>
                <input type="text" id="nombreProducto" class="formulario__input" placeholder="Ej: Leche, Pan, Huevos... ">
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="cantidad">Cantidad disponible:</label>
                <select id="cantidad" class="formulario__select">
                    <option value="">Ingresar la cantidad</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                    <option value="12">12</option>
                    <option value="13">13</option>
                    <option value="14">14</option>
                    <option value="15">15</option>
                </select>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="categoria">Categoría:</label>
                <select id="categoria" class="formulario__select">
                    <option value="">Seleccionar Categoría:</option>
                    <option value="alimentos">Alimentos</option>
                    <option value="aseo">Aseo</option>
                    <option value="personal">Personal</option>
                    <option value="otros">Otros</option>
                    <option value="ropa">Ropa y calzado</option>
                </select>
            </div>
            <div class="formulario__etiquetas">
                <div class="etiqueta etiqueta--aseo">
                    <img src="../../../asset/imagenes/Aseo.png" alt="Icono de Aseo">
                    <span>Aseo</span>
                </div>
                <div class="etiqueta etiqueta--alimentos">
                    <img src="../../../asset/imagenes/alimentos-saludables.png" alt="Icono de Alimentos">
                    <span>Alimentos</span>
                </div>
                <div class="etiqueta etiqueta--personal">
                    <img src="../../../asset/imagenes/maquillaje.png" alt="Icono de Personal">
                    <span>Personal</span>
                </div>
                <div class="etiqueta etiqueta--otros">
                    <img src="../../../asset/imagenes/cajas.png" alt="Icono de Otros">
                    <span>Otros</span>
                </div>
                <div class="etiqueta etiqueta--ropa-calzado">
                    <img src="../../../asset/imagenes/Ropa-calzado.jpg" alt="Icono de Ropa y calzado">
                    <span>Ropa y calzado</span>
                </div>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="descripcion">Descripción (opcional):</label>
                <textarea id="descripcion" class="formulario__textarea" rows="4" placeholder="Agrega detalles sobre este producto.."></textarea>
            </div>

            <a href="03_ConfirmarRegistroProductosDisponibles.html" class="formulario__botones">
                <button type="button" class="boton boton--registrar">Registrar producto</button>
            </a>
            <a href="01_MiInventario.html" class="formulario__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </div>
    </main>
</body>
</html>
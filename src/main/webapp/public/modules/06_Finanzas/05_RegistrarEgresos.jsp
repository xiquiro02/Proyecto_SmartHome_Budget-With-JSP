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
    <link rel="stylesheet" href="../../../asset/modules/06_Finanzas/estilosRegistrarEgresos.css">

    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="../../../asset/imagenes/Logo redondo.png" alt="Logo de SmartHome Budget">
        <a href="01_Finanzas.html">
            <span class="material-symbols-outlined"> arrow_back_ios_new </span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Registrar Egreso</h1>
        </div>
    </header>
    <main class="formulario">
        <div class="formulario__contenedor">
            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="categoria">Tipo de gasto</label>
                <select id="categoria" class="formulario__select">
                    <option value="">Seleccionar gasto</option>
                    <option value="alimentos">Fijo</option>
                    <option value="aseo">Variable</option>
                    <option value="personal">Esencial</option>
                    <option value="otros">Personal</option>
                </select>
            </div>
            <div class="formulario__etiquetas">
                <div class="etiqueta etiqueta--aseo">
                    <img src="../../../asset/imagenes/ventas.png" alt="Icono de Aseo">
                    <span>Fijo (Servicios)</span>
                </div>
                <div class="etiqueta etiqueta--alimentos">
                    <img src="../../../asset/imagenes/inversiones.png" alt="Icono de Alimentos">
                    <span>Variable (comida)</span>
                </div>
                <div class="etiqueta etiqueta--personal">
                    <img src="../../../asset/imagenes/dolares.png" alt="Icono de Personal">
                    <span>Personal (entretenimiento)</span>
                </div>
                <div class="etiqueta etiqueta--otros">
                    <img src="../../../asset/imagenes/dolar-otros.png" alt="Icono de Otros">
                    <span>Esencial (medicamentos)</span>
                </div>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="nombre">Monto del egreso:</label>
                <input type="text" id="nombre" class="formulario__input" placeholder="$ 0.00">
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta">Fecha del ingreso:</label>
                <label class="formulario__fecha">
                    <input type="radio" id="hoy" name="fecha" checked>
                    <span>Hoy (automática)</span>
                </label>
            </div>

            <div class="formulario__campo">
                <label class="formulario__etiqueta" for="descripcion">Descripción (opcional):</label>
                <textarea id="descripcion" class="formulario__textarea" rows="4" placeholder="Ej: Pago de luz, compra mercado, cine..."></textarea>
            </div>


            <a href="06_RegistroEgresoExito.html" class="formulario__botones">
                <button type="button" class="boton boton--registrar">Guardar egreso</button>
            </a>
            <a href="07_ DetalleEgresos.html" class="formulario__botones">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>

        </div>
    </main>
</body>
</html>
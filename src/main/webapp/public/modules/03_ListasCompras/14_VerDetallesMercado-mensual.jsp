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
    <link rel="stylesheet" href="../../../asset/modules/03_ListasCompras/estilosVerDetallesMercado-mensual.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenedor-lista">
        <article class="tarjetaMercado">
            <div class="tarjetaMercado__encabezado">
                <div class="tarjetaMercado__icono">
                    <img src="../../../asset/imagenes/lista-compra.png" alt="Icono de mercado">
                </div>
                <div class="tarjetaMercado__info">
                    <h2 class="tarjetaMercado__nombre">Mercado mensual</h2>
                    <p class="tarjetaMercado__categoria">Categoría: Alimentos</p>
                </div>
            </div>
            <div class="tarjetaMercado__etiquetas">
                <span class="etiqueta etiqueta--completa">Completa</span>
                <span class="etiqueta etiqueta--total">12 productos</span>
            </div>
        </article>

        <section class="estadisticas">
            <div class="estadisticas__item">
                <div class="estadisticas__numero estadisticas__numero--comprado">12</div>
                <p class="estadisticas__texto">Comprados</p>
            </div>
            <div class="estadisticas__item">
                <div class="estadisticas__numero estadisticas__numero--pendiente">0</div>
                <p class="estadisticas__texto">Pendientes</p>
            </div>
        </section>
        <p class="fecha">Creada: 10 Feb 2025</p>
        <h3 class="tituloSeccion">Productos de la lista</h3>

        <ul class="listaProductos">
            <li class="producto producto--completado">
                <label class="producto__label">
                    <input type="checkbox" class="producto__input" checked>
                    <span class="producto__circulo"></span>
                    <div class="producto__info">
                        <h4 class="producto__nombre">Leche</h4>
                        <p class="producto__cantidad">2 unidades</p>
                    </div>
                </label>
            </li>

            <li class="producto producto--completado">
                <label class="producto__label">
                    <input type="checkbox" class="producto__input" checked>
                    <span class="producto__circulo"></span>
                    <div class="producto__info">
                        <h4 class="producto__nombre">Pan</h4>
                        <p class="producto__cantidad">3 unidades</p>
                    </div>
                </label>
            </li>

            <li class="producto producto--completado">
                <label class="producto__label">
                    <input type="checkbox" class="producto__input" checked>
                    <span class="producto__circulo"></span>
                    <div class="producto__info">
                        <h4 class="producto__nombre">Huevos</h4>
                        <p class="producto__cantidad">30 unidades</p>
                    </div>
                </label>
            </li>

            <li class="producto producto--completado">
                <label class="producto__label">
                    <input type="checkbox" class="producto__input" checked>
                    <span class="producto__circulo"></span>
                    <div class="producto__info">
                        <h4 class="producto__nombre">Arroz</h4>
                        <p class="producto__cantidad">1 kg</p>
                    </div>
                </label>
            </li>


            <li class="producto producto--completado">
                <label class="producto__label">
                    <input type="checkbox" class="producto__input" checked>
                    <span class="producto__circulo"></span>
                    <div class="producto__info">
                        <h4 class="producto__nombre">Aceite</h4>
                        <p class="producto__cantidad">1 litro</p>
                    </div>
                </label>
            </li>
        </ul>

        <div class="acciones">
            <a href="17_ProductosComprado-Todos.html" class="acciones__marcar"><button class="boton boton--marcar">Marcar todo</button></a>
            <a href="18_ProductosPendientes-Todos.html" class="acciones__desmarcado"><button class="boton boton--marcar">Desmarcado</button></a>
        </div>

        <a href="01_listasCompras.html" class="acciones__volver"><button class="boton boton--volver">Volver</button></a>
    </main>
</body>
</html>
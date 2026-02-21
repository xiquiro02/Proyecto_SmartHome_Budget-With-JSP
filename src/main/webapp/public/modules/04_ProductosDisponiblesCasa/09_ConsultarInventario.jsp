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
    <link rel="stylesheet" href="../../../asset/modules/04_ProductosDisponiblesCasa/estilosConsultarInventario.css">

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
                <a href="10_FiltroCategorias-ConsultarInventario.html" class="filtro-enlace">
                    Categoría
                    <img class="filtro-imagen" src="../../../asset/imagenes/flecha.png" alt="Icono filtros">
                </a>
                <a href="11_FiltroCantidad-ConsultarInventario.html" class="filtro-enlace" alt="Icono filtros">
                    Cantidad
                    <img class="filtro-imagen" src="../../../asset/imagenes/flecha.png" alt="Icono filtros">
                </a>
            </div>
        </div>

        <div class="gridProductos">
            <article class="tarjetaProducto tarjetaProducto--verde">
                <img src="../../../asset/imagenes/alimentos-saludables.png" alt="Arroz Blanco" class="tarjetaProducto__icono">
                <div class="tarjetaProducto__info">
                    <h3 class="tarjetaProducto__nombre">Arroz Blanco</h3>
                    <p class="tarjetaProducto__detalle">Categoría: Alimentos</p>
                    <p class="tarjetaProducto__detalle">Cantidad: 3 libras</p>
                    <p class="tarjetaProducto__detalle">Fecha: Hoy</p>
                </div>
                <div class="tarjetaProducto__acciones">
                    <a href="04_EditarProductoCasa.html"><button class="botonAccion botonAccion--editar">✏️ Editar</button></a>
                    <a href="06_EliminarProducto-Casa.html"><button class="botonAccion botonAccion--eliminar">🗑️ Eliminar</button></a>
                </div>
            </article>

            <article class="tarjetaProducto tarjetaProducto--azul">
                <img src="../../../asset/imagenes/Aseo.png" alt="Detergente líquido" class="tarjetaProducto__icono">
                <div class="tarjetaProducto__info">
                    <h3 class="tarjetaProducto__nombre">Detergente líquido</h3>
                    <p class="tarjetaProducto__detalle">Categoría: Aseo</p>
                    <p class="tarjetaProducto__detalle">Cantidad: 2 litros</p>
                    <p class="tarjetaProducto__detalle">Fecha: Hoy</p>
                </div>
                <div class="tarjetaProducto__acciones">
                    <a href="04_EditarProductoCasa.html"><button class="botonAccion botonAccion--editar">✏️ Editar</button></a>
                    <a href="06_EliminarProducto-Casa.html"><button class="botonAccion botonAccion--eliminar">🗑️ Eliminar</button></a>
                </div>
            </article>

            <article class="tarjetaProducto tarjetaProducto--verde">
                <img src="../../../asset/imagenes/alimentos-saludables.png" alt="Pan tajado" class="tarjetaProducto__icono">
                <div class="tarjetaProducto__info">
                    <h3 class="tarjetaProducto__nombre">Pan tajado</h3>
                    <p class="tarjetaProducto__detalle">Categoría: Alimentos</p>
                    <p class="tarjetaProducto__detalle">Cantidad: 4 paquetes</p>
                    <p class="tarjetaProducto__detalle">Fecha: 10 Oct 2025</p>
                </div>
                <div class="tarjetaProducto__acciones">
                    <a href="04_EditarProductoCasa.html"><button class="botonAccion botonAccion--editar">✏️ Editar</button></a>
                    <a href="06_EliminarProducto-Casa.html"><button class="botonAccion botonAccion--eliminar">🗑️ Eliminar</button></a>
                </div>
            </article>

            <article class="tarjetaProducto tarjetaProducto--gris">
                <img src="../../../asset/imagenes/cajas.png" alt="Velas aromáticas" class="tarjetaProducto__icono">
                <div class="tarjetaProducto__info">
                    <h3 class="tarjetaProducto__nombre">Velas aromáticas</h3>
                    <p class="tarjetaProducto__detalle">Categoría: Otros</p>
                    <p class="tarjetaProducto__detalle">Cantidad: 3 unidades</p>
                    <p class="tarjetaProducto__detalle">Fecha: 10 Oct 2025</p>
                </div>
                <div class="tarjetaProducto__acciones">
                    <a href="04_EditarProductoCasa.html"><button class="botonAccion botonAccion--editar">✏️ Editar</button></a>
                    <a href="06_EliminarProducto-Casa.html"><button class="botonAccion botonAccion--eliminar">🗑️ Eliminar</button></a>
                </div>
            </article>

            <article class="tarjetaProducto tarjetaProducto--morado">
                <img src="../../../asset/imagenes/maquillaje.png" alt="Crema hidratante facial" class="tarjetaProducto__icono">
                <div class="tarjetaProducto__info">
                    <h3 class="tarjetaProducto__nombre">Crema hidratante facial</h3>
                    <p class="tarjetaProducto__detalle">Categoría: Personal</p>
                    <p class="tarjetaProducto__detalle">Cantidad: 1 unidad</p>
                    <p class="tarjetaProducto__detalle">Fecha: 12 Sep 2025</p>
                </div>
                <div class="tarjetaProducto__acciones">
                    <a href="04_EditarProductoCasa.html"><button class="botonAccion botonAccion--editar">✏️ Editar</button></a>
                    <a href="06_EliminarProducto-Casa.html"><button class="botonAccion botonAccion--eliminar">🗑️ Eliminar</button></a>
                </div>
            </article>

            <article class="tarjetaProducto tarjetaProducto--azul">
                <img src="../../../asset/imagenes/Aseo.png" alt="Limpiador de multiusos" class="tarjetaProducto__icono">
                <div class="tarjetaProducto__info">
                    <h3 class="tarjetaProducto__nombre">Limpiador de multiusos</h3>
                    <p class="tarjetaProducto__detalle">Categoría: Aseo</p>
                    <p class="tarjetaProducto__detalle">Cantidad: 1 unidad</p>
                    <p class="tarjetaProducto__detalle">Fecha: 12 Sep 2025</p>
                </div>
                <div class="tarjetaProducto__acciones">
                    <a href="04_EditarProductoCasa.html"><button class="botonAccion botonAccion--editar">✏️ Editar</button></a>
                    <a href="06_EliminarProducto-Casa.html"><button class="botonAccion botonAccion--eliminar">🗑️ Eliminar</button></a>
                </div>
            </article>
        </div>

        <a href="02_RegistrarProductoDisponibles.html" class="boton-NuevoProducto">
            <button class="boton boton--cancelar">+ Nuevo producto</button>
        </a>

    </main>
</body>
</html>
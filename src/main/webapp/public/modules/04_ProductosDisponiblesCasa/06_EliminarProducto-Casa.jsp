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
    <link rel="stylesheet" href="../../../asset/modules/04_ProductosDisponiblesCasa/estilosEliminarProductoCasa.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
                <img class="contenido__icono-img" src="../../../asset/imagenes/eliminar.png" alt="Icono de Eliminar">
                <h1 class="contenido__titulo">Eliminar producto</h1>
                <p class="contenido__parrafo">Estás a punto de eliminar el producto del inventario:</p>
                <p class="contenido__parrafo"><strong>Producto:</strong> Detergente líquido</p>
                <p class="contenido__parrafo"><strong>Categoría:</strong> Aseo</p>
                <p class="contenido__parrafo"><strong>Cantidad actual:</strong> 2 litros</p>
                <p class="contenido__parrafo">❗ Esta acción no se puede deshacer.  Una vez eliminado, el producto desaparecerá de tu inventario.</p>
                <div class="contenido__grupo">
                    <a href="07_ProductoEliminado.html">            
                        <button type="submit" class="boton boton--Eliminar">Sí, eliminar</button>
                    </a>
                    <a href="09_ConsultarInventario.html">
                        <button type="submit" class="boton boton--cancelar">No, conservar</button>
                    </a>
                </div>
        </div>
    </main>
</body>
</html>
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
    <link rel="stylesheet" href="../../../asset/modules/03_ListasCompras/estilosEliminarLista.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
                <img class="contenido__icono-img" src="../../../asset/imagenes/eliminar.png" alt="Icono de Eliminar">
                <h1 class="contenido__titulo">Eliminar lista</h1>
                <p class="contenido__parrafo">¿Estás seguro de que deseas eliminar <strong>“Mercado mensual”</strong> ? Esta acción no se puede deshacer.</p>
                <div class="contenido__grupo">
                    <a href="07_ConfirmarEliminacionLista.html">            
                        <button type="submit" class="boton boton--Eliminar">Sí, eliminar</button>
                    </a>
                    <a href="01_listasCompras.html">
                        <button type="submit" class="boton boton--cancelar">No, conservar</button>
                    </a>
                </div>
        </div>
    </main>
</body>
</html>
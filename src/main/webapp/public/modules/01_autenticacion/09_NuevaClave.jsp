<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Link iconos  -->
    <link rel="stylesheet"
        href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
    <!-- Link Fuentes -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <!-- Link estilos.css  -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosNuevaClave.css">
    <title>SmartHome Budget</title>
</head>

<body>
    <main class="contenido">
        <div class="contenedor">
            <h1 class="contenido__titulo">Nueva Contraseña</h1>
            <p class="contenido__parrafo">Por seguridad, crea una nueva contraseña diferente a la anterior.</p>
            <form class="contenido__formulario" method="post" action="${pageContext.request.contextPath}/NuevaClave">
                <div class="contenido__grupo">
                    <label for="nueva_clave" class="contenido__label"> Nueva Contraseña</label>
                    <input type="password" class="contenido__input" id="nueva_clave" name="nueva_clave"
                        placeholder="Ingresa tu contraseña" required />
                </div>
                <div class="contenido__grupo">
                    <label for="confirmar_clave" class="contenido__label">Confirmar Contraseña</label>
                    <input type="password" class="contenido__input" id="confirmar_clave" name="confirmar_clave"
                        placeholder="Ingresa tu contraseña" required />
                </div>
            <p class="contenido__parrafo">Requisitos mínimos:</p>
            <div class="contenido__grupo--fila">
                <img class="contenido__icono-img"
                    src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png"
                    alt="Icono de requisitos mínimos">
                <p class="contenido__label">Debe tener mínimo 8 caracteres</p>
            </div>
            <div class="contenido__grupo--fila">
                <img class="contenido__icono-img"
                    src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png"
                    alt="Icono de requisitos mínimos">
                <p class="contenido__label">Debe incluir al menos una letra</p>
            </div>
            <div class="contenido__grupo--fila">
                <img class="contenido__icono-img"
                    src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png"
                    alt="Icono de requisitos mínimos">
                <p class="contenido__label">Debe incluir al menos un número</p>
            </div>
            <div class="contenido__grupo">
                <button type="submit" class="boton boton--registrar">Guardar</button>
                <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/04_iniciarSesion.jsp">
                    <button type="button" class="boton boton--cancelar">Cancelar</button>
                </a>
            </div>
            </form>
        </div>
    </main>
    
    <script src="${pageContext.request.contextPath}/asset/js/nuevaClave.js"></script>
</body>

</html>

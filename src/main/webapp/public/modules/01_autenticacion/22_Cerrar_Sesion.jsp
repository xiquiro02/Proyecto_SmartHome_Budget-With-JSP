<%@page contentType="text/html" pageEncoding="UTF-8" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosCerrar_Sesion.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
                <h1 class="contenido__titulo">Cerrar Sesión </h1>
                <p class="contenido__parrafo">¿Quieres Cerrar Sesión?</p>
                <p class="contenido__parrafo">¡Gracias por visitarnos! Hasta pronto</p>
                <div class="contenido__grupo">
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/01_principal.jsp">            
                        <button type="submit" class="boton boton--registrar">Cerrar Sesión</button>
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/19_MiPerfil.jsp">
                        <button type="submit" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
        </div>
    </main>
</body>
</html>
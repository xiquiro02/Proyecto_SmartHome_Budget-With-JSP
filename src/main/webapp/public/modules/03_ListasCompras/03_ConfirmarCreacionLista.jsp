<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosConfirmarCreacionLista.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Lista creada</h1></div>
</header>
<main class="confirmacion">
    <div class="confirmacion__contenedor">
        <div class="confirmacion__alerta">
            <img class="confirmacion__icono" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="OK">
            <p class="confirmacion__mensaje">¡Lista creada correctamente!</p>
        </div>
        <div class="resumen">
            <div class="resumen__campo">
                <img class="resumen__icono" src="${pageContext.request.contextPath}/asset/imagenes/elementos-de-la-lista.png" alt="Nombre">
                <div class="resumen__info">
                    <label class="resumen__etiqueta">Nombre:</label>
                    <input type="text" class="resumen__input" value="${nombreLista}" readonly>
                </div>
            </div>
            <div class="resumen__campo">
                <img class="resumen__icono" src="${pageContext.request.contextPath}/asset/imagenes/fecha.png" alt="Fecha">
                <div class="resumen__info">
                    <label class="resumen__etiqueta">Creación:</label>
                    <input type="text" class="resumen__input" value="Hoy" readonly>
                </div>
            </div>
        </div>
        <p style="text-align:center; color: #666; margin: 0.5rem 0;">Ahora puedes agregar productos a tu lista.</p>
        <a href="${pageContext.request.contextPath}/Listas" class="confirmacion__botones">
            <button type="button" class="boton boton--registrar">Ver mis listas</button>
        </a>
        <a href="${pageContext.request.contextPath}/Listas?accion=crear" class="confirmacion__botones">
            <button type="button" class="boton boton--cancelar">Crear otra lista</button>
        </a>
    </div>
</main>
</body>
</html>

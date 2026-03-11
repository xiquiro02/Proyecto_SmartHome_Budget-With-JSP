<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosEliminarFactura.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
            <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/eliminar.png" alt="Eliminar">
            <h1 class="contenido__titulo">Eliminar factura</h1>
            <p class="contenido__parrafo">¿Estás seguro de que deseas eliminar la factura <strong>"${factura.nombreFactura}"</strong>?</p>
            <p class="contenido__parrafos">Monto: <fmt:formatNumber value="${factura.monto}" type="currency" currencySymbol="$"/></p>
            <p class="contenido__parrafos">Esta acción no se puede deshacer.</p>
            <div class="contenido__grupo">
                <form action="${pageContext.request.contextPath}/Facturas" method="post">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="idEgreso" value="${factura.idEgresos}">
                    <button type="submit" class="boton boton--Eliminar">Sí, eliminar</button>
                </form>
                <a href="${pageContext.request.contextPath}/Facturas?accion=lista">
                    <button class="boton boton--cancelar">Cancelar</button>
                </a>
            </div>
        </div>
    </main>
</body>
</html>

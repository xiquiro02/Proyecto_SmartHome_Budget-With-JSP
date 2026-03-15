<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosEditarFactura.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas?accion=lista"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
        <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Editar Factura</h1></div>
    </header>
    <main class="factura">
        <c:if test="${not empty param.error}">
            <div class="mensaje mensaje--error">⚠️
                <c:choose>
                    <c:when test="${param.error == 'campos_vacios'}">Completa todos los campos obligatorios.</c:when>
                    <c:when test="${param.error == 'monto_invalido'}">El monto debe ser un número positivo.</c:when>
                    <c:when test="${param.error == 'fecha_invalida'}">La fecha no es válida.</c:when>
                    <c:otherwise>Error al actualizar. Intenta nuevamente.</c:otherwise>
                </c:choose>
            </div>
        </c:if>
        
<form class="factura__formulario" action="${pageContext.request.contextPath}/Facturas" method="post">
        <input type="hidden" name="accion" value="actualizar">
        <input type="hidden" name="idEgreso" value="${factura.idEgresos}">

        <div class="factura__grupo">
            <label class="factura__label">Nombre de la factura *</label>
            <input type="text" name="nombreFactura" class="factura__input"
                   value="${factura.nombreFactura}" maxlength="150" required>
        </div>

        <div class="factura__grupo">
            <label class="factura__label">Categoría *</label>
            <select name="idCategoriaEgreso" class="factura__select" required>
                <c:forEach var="cat" items="${categorias}">
                    <option value="${cat.idCategoriaEgreso}"
                        ${cat.idCategoriaEgreso == factura.idCategoriaEgreso ? 'selected' : ''}>
                        ${cat.nombreCategoriaEgreso}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="factura__fila">
                <div class="factura__grupo factura__grupo--medio">
                    <label class="factura__label">Monto *</label>
                    <input type="number" name="monto" class="factura__input"
                           value="${factura.monto}" min="0.01" step="0.01" required>
                </div>
                <div class="factura__grupo factura__grupo--medio">
                    <label class="factura__label">Vencimiento *</label>
                    <input type="date" name="fechaVencimiento" class="factura__input factura__input--fecha"
                           value="${f.fechaVencimientoFormateada}" required>
                </div>
            </div>

            <div class="factura__grupo">
                <label class="factura__label">Método de pago *</label>
                <select name="idMetodoPago" class="factura__select" required>
                    <c:forEach var="met" items="${metodosPago}">
                        <option value="${met.idMetodoPago}"
                            ${met.idMetodoPago  == factura.idMetodoPago ? 'selected' : ''}>
                            ${met.nombreMetodoPago}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="factura__grupo">
                <label class="factura__label">Estado de pago *</label>
                <div class="factura__estado">
                    <label class="factura__estado-opcion">
                        <input type="radio" name="estadoPago" value="Pendiente" ${factura.estadoPago == 'Pendiente' ? 'checked' : ''}><span>Pendiente</span>
                    </label>
                    <label class="factura__estado-opcion">
                        <input type="radio" name="estadoPago" value="Pagada" ${factura.estadoPago == 'Pagada' ? 'checked' : ''}><span>Pagada</span>
                    </label>
                    <label class="factura__estado-opcion">
                        <input type="radio" name="estadoPago" value="Vencida" ${factura.estadoPago == 'Vencida' ? 'checked' : ''}><span>Vencida</span>
                    </label>
                </div>
            </div>

            <div class="factura__grupo">
                <label class="factura__label">Notas adicionales (opcional)</label>
                <textarea name="descripcion" class="factura__textarea">${factura.descripcion}</textarea>
            </div>

            <div class="factura__boton">
                <button type="submit" class="boton boton--registrar">Guardar cambios</button>
            </div>
            <a href="${pageContext.request.contextPath}/Facturas?accion=lista" class="factura__boton">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </main>
</body>
</html>

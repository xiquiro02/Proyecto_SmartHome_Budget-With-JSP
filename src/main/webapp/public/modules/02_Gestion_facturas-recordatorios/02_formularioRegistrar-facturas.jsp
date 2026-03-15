<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosFormularioRegistrar-facturas.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Facturas">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Registrar Factura o pago</h1>
        </div>
    </header>

    <main class="factura">
        <c:if test="${not empty error}">
            <div class="mensaje mensaje--error">⚠️ ${error}</div>
        </c:if>

        <form class="factura__formulario" id="registroFactura" action="${pageContext.request.contextPath}/Facturas" method="post">
            <input type="hidden" name="accion" value="registrar">

            <div class="factura__grupo">
                <label class="factura__label">Nombre de la factura *</label>
                <input type="text" name="nombreFactura" class="factura__input"
                       placeholder="Ej: Factura de electricidad"
                       maxlength="150" pattern=".*\S.*" 
                       title="No puede estar vacío o contener solo espacios" required>
            </div>

            <div class="factura__grupo">
                <label class="factura__label">Categoría *</label>
                <select name="idCategoriaEgreso" class="factura__select" required>
                    <option value="">Seleccionar categoría</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat.idCategoriaEgreso}">${cat.nombreCategoriaEgreso}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="factura__fila">
                <div class="factura__grupo factura__grupo--medio">
                    <label class="factura__label">Monto a pagar *</label>
                    <input type="number" name="monto" class="factura__input"
                           placeholder="0.00" min="0.01" max="9999999.99" step="0.01" required>
                </div>
                <div class="factura__grupo factura__grupo--medio">
                    <label class="factura__label">Vencimiento *</label>
                    <input type="date" name="fechaVencimiento" 
                           class="factura__input factura__input--fecha" 
                           min="2000-01-01" max="2100-12-31" required>
                </div>
            </div>

            <div class="factura__grupo">
                <label class="factura__label">Método de pago *</label>
                <select name="idMetodoPago" class="factura__select" required>
                    <option value="">Seleccionar método</option>
                    <c:forEach var="met" items="${metodosPago}">
                        <option value="${met.idMetodoPago}">${met.nombreMetodoPago}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="factura__grupo">
                <label class="factura__label">Estado de pago *</label>
                <div class="factura__estado">
                    <label class="factura__estado-opcion">
                        <input type="radio" name="estadoPago" value="Pendiente" checked>
                        <span>Pendiente</span>
                    </label>
                    <label class="factura__estado-opcion">
                        <input type="radio" name="estadoPago" value="Pagada">
                        <span>Pagada</span>
                    </label>
                    <label class="factura__estado-opcion">
                        <input type="radio" name="estadoPago" value="Vencida">
                        <span>Vencida</span>
                    </label>
                </div>
            </div>

            <div class="factura__grupo">
                <label class="factura__label">Notas adicionales (opcional)</label>
                <textarea name="descripcion" class="factura__textarea" maxlength="500"
                          placeholder="Agrega detalles sobre esta factura..."></textarea>
            </div>

            <div class="factura__recordatorio">
                <div class="factura__recordatorio-header">
                    <img class="factura__recordatorio-icono" src="${pageContext.request.contextPath}/asset/imagenes/notificar-alerta.png" alt="Recordatorio">
                    <span class="factura__recordatorio-texto">Recordatorio automático</span>
                </div>
                <p class="factura__recordatorio-mensaje">Se generará un recordatorio 2 días y 1 día antes del vencimiento.</p>
            </div>

            <div class="factura__boton">
                <button type="submit" class="boton boton--registrar" id="btnGuardarFactura">Guardar</button>
            </div>
            <a href="${pageContext.request.contextPath}/Facturas" class="factura__boton">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
        </form>
    </main>
    <script src="${pageContext.request.contextPath}/asset/js/validacionesFormularioFacturas.js"></script>
</body>
</html>

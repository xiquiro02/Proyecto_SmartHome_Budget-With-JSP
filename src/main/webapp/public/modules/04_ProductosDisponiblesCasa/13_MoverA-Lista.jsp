<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/04_ProductosDisponiblesCasa/estilosMoverA-Lista.css">
    <title>SmartHome Budget</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
    <a href="${pageContext.request.contextPath}/Inventario?accion=agotados"><span class="material-symbols-outlined">arrow_back_ios_new</span></a>
    <div class="encabezado__contenedorTitulo"><h1 class="encabezado__titulo">Mover a lista</h1></div>
</header>
<main class="contenedorMover">
    <div class="infoProducto">
        <h2 class="infoProducto__titulo">Seleccionar lista de destino</h2>
        <p class="infoProducto__texto">
            Producto agotado: <strong class="infoProducto__nombre">"${inv.nombreProducto}"</strong>.<br>
            Selecciona a qué lista deseas agregarlo:
        </p>
    </div>

    <form action="${pageContext.request.contextPath}/Inventario" method="post">
        <input type="hidden" name="accion" value="mover">
        <input type="hidden" name="idInventario" value="${inv.idInventario}">

        <div class="formulario__campo" style="margin:1rem 0">
            <label>Cantidad a agregar:</label>
            <input type="number" name="cantidad" value="1" min="1" max="99"
                   style="width:80px; padding:0.4rem; margin-left:0.5rem">
        </div>

        <c:choose>
            <c:when test="${empty listas}">
                <p style="color:#999; text-align:center">No tienes listas de compras. Crea una primero.</p>
                <a href="${pageContext.request.contextPath}/Listas?accion=crear">
                    <button type="button" class="boton boton--registrar">Crear lista</button>
                </a>
            </c:when>
            <c:otherwise>
                <ul class="listaOpciones">
                    <c:forEach var="lista" items="${listas}" varStatus="s">
                        <li class="opciones">
                            <label class="opcion__label">
                                <input type="radio" name="idLista" value="${lista.idListaCompras}"
                                       ${s.first ? 'checked' : ''}>
                                <span class="opcion__circulo"></span>
                                <div class="opcion__info">
                                    <h4 class="opcion__nombre">${lista.nombreLista}
                                        <small style="color:#888">(${lista.estadoLista})</small>
                                    </h4>
                                </div>
                            </label>
                        </li>
                    </c:forEach>
                </ul>

                <div class="consejo">
                    <p class="consejo__titulo">Consejo:</p>
                    <p class="consejo__descripcion">Selecciona la lista que mejor corresponda con el tipo de producto.</p>
                </div>

                <button type="submit" class="boton boton--registrar">Agregar a lista</button>
            </c:otherwise>
        </c:choose>
    </form>

    <a href="${pageContext.request.contextPath}/Inventario?accion=agotados" class="acciones">
        <button type="button" class="boton boton--cancelar">Cancelar</button>
    </a>
</main>
</body>
</html>

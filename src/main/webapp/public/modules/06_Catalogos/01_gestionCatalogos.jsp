<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%
    // Verificar sesión y rol
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() +
            "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
        return;
    }
    Integer idRolSesion = (Integer) session.getAttribute("idRol");
    if (idRolSesion == null || idRolSesion == 3) {
        response.sendRedirect(request.getContextPath() + "/Menu?error=acceso_denegado");
        return;
    }

    // Determinar título e icono según tipo de catálogo
    String tipo = request.getAttribute("tipo") != null
                  ? (String) request.getAttribute("tipo") : "";
    String titulo       = "Gestión de catálogos";
    String icono        = "categorias.png";
    String campo        = "Nombre";
    String placeholder  = "Escribe el nombre...";

    switch (tipo) {
        case "categoriaEgreso":
            titulo      = "Categorías de Egresos";
            icono       = "Registrar-egresos.png";
            campo       = "Nombre de categoría (ej: Servicios, Arriendo)";
            placeholder = "Ej: Servicios, Arriendo";
            break;
        case "categoriaIngreso":
            titulo      = "Categorías de Ingresos";
            icono       = "ingresos-pasivos.png";
            campo       = "Nombre de categoría (ej: Salario, Ventas)";
            placeholder = "Ej: Salario, Ventas";
            break;
        case "metodoPago":
            titulo      = "Métodos de Pago";
            icono       = "metodo-de-pago.png";
            campo       = "Nombre del método (ej: Nequi, Tarjeta)";
            placeholder = "Ej: Nequi, Tarjeta";
            break;
        case "tipoProducto":
            titulo      = "Tipos de Producto";
            icono       = "productos.png";
            campo       = "Nombre del tipo (ej: Alimentos, Aseo)";
            placeholder = "Ej: Alimentos, Aseo";
            break;
    }

    // Ítem en edición (si viene de acción editar)
    Object itemEditar = request.getAttribute("itemEditar");
    int    idEditar   = 0;
    String nombreEditar = "";

    if (itemEditar != null) {
        if (itemEditar instanceof com.smarthome.smarthome_budget.modelo.CategoriaEgreso) {
            com.smarthome.smarthome_budget.modelo.CategoriaEgreso ce =
                (com.smarthome.smarthome_budget.modelo.CategoriaEgreso) itemEditar;
            idEditar      = ce.getIdCategoriaEgreso();
            nombreEditar  = ce.getNombreCategoriaEgreso();
        } else if (itemEditar instanceof com.smarthome.smarthome_budget.modelo.CategoriasIngresos) {
            com.smarthome.smarthome_budget.modelo.CategoriasIngresos ci =
                (com.smarthome.smarthome_budget.modelo.CategoriasIngresos) itemEditar;
            idEditar      = ci.getIdCategoriaIngreso();
            nombreEditar  = ci.getNombreCategoriaIngreso();
        } else if (itemEditar instanceof com.smarthome.smarthome_budget.modelo.MetodoPago) {
            com.smarthome.smarthome_budget.modelo.MetodoPago mp =
                (com.smarthome.smarthome_budget.modelo.MetodoPago) itemEditar;
            idEditar      = mp.getIdMetodoPago();
            nombreEditar  = mp.getNombreMetodoPago();
        } else if (itemEditar instanceof com.smarthome.smarthome_budget.modelo.TipoProducto) {
            com.smarthome.smarthome_budget.modelo.TipoProducto tp =
                (com.smarthome.smarthome_budget.modelo.TipoProducto) itemEditar;
            idEditar      = tp.getIdTipoProducto();
            nombreEditar  = tp.getNombreTipoProducto();
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/06_Catalogos/estilosCatalogos.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <title>SmartHome Budget — <%= titulo %></title>
</head>
<body>

<header class="encabezado">
    <img class="encabezado__imagen"
         src="${pageContext.request.contextPath}/asset/imagenes/<%= icono %>"
         alt="<%= titulo %>">
    <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp">
        <span class="material-symbols-outlined">arrow_back_ios_new</span>
    </a>
    <div class="encabezado__contenedorTitulo">
        <h1 class="encabezado__titulo"><%= titulo %></h1>
    </div>
</header>

<main class="catalogos">

    <%-- Mensajes de error y éxito --%>
    <% String errorAttr = (String) request.getAttribute("error");
       String exitoParam = request.getParameter("exito");
       String errorParam = request.getParameter("error");
       if (errorAttr != null) { %>
    <div class="mensaje mensaje--error">⚠️ <%= errorAttr %></div>
    <% } %>

    <%-- Formulario crear / editar --%>
    <% if (idRolSesion == 1 || idRolSesion == 2) { %>
    <section class="catalogos__formulario">
        <h2 class="catalogos__subtitulo">
            <%= idEditar > 0 ? "Editar" : "Nuevo" %>
        </h2>
        <form method="post" action="${pageContext.request.contextPath}/Catalogos"
              onsubmit="return validarCatalogo()">
            <input type="hidden" name="tipo"   value="<%= tipo %>">
            <input type="hidden" name="accion" value="<%= idEditar > 0 ? "actualizar" : "crear" %>">
            <% if (idEditar > 0) { %>
            <input type="hidden" name="id" value="<%= idEditar %>">
            <% } %>

            <div class="catalogos__grupo">
                <label class="catalogos__label"><%= campo %></label>
                <div class="catalogos__inputRow">
                    <input type="text" id="inputNombre" name="nombre"
                           class="catalogos__input"
                           value="<%= nombreEditar %>"
                           placeholder="<%= placeholder %>"
                           maxlength="100" required>
                    <button type="submit" class="boton boton--registrar catalogos__btnGuardar">
                        <%= idEditar > 0 ? "💾 Guardar" : "➕ Agregar" %>
                    </button>
                </div>
            </div>

            <% if (idEditar > 0) { %>
            <a href="${pageContext.request.contextPath}/Catalogos?tipo=<%= tipo %>">
                <button type="button" class="boton boton--cancelar">Cancelar</button>
            </a>
            <% } %>
        </form>
    </section>
    <% } %>

    <%-- Lista de ítems --%>
    <section class="catalogos__lista">
        <h2 class="catalogos__subtitulo">Lista actual</h2>

        <c:choose>
            <c:when test="${empty items}">
                <p class="catalogos__vacio">No hay registros aún. Agrega el primero.</p>
            </c:when>
            <c:otherwise>
                <ul class="catalogos__items">
                    <c:forEach var="item" items="${items}">
                        <li class="catalogos__item">

                            <%-- Nombre según tipo --%>
                            <span class="catalogos__itemNombre">
                                <c:choose>
                                    <c:when test="${tipo == 'categoriaEgreso'}">${item.nombreCategoriaEgreso}</c:when>
                                    <c:when test="${tipo == 'categoriaIngreso'}">${item.nombreCategoriaIngreso}</c:when>
                                    <c:when test="${tipo == 'metodoPago'}">${item.nombreMetodoPago}</c:when>
                                    <c:when test="${tipo == 'tipoProducto'}">${item.nombreTipoProducto}</c:when>
                                </c:choose>
                            </span>

                            <%-- ID según tipo --%>
                            <c:set var="itemId" value="0"/>
                            <c:if test="${tipo == 'categoriaEgreso'}"><c:set var="itemId" value="${item.idCategoriaEgreso}"/></c:if>
                            <c:if test="${tipo == 'categoriaIngreso'}"><c:set var="itemId" value="${item.idCategoriaIngreso}"/></c:if>
                            <c:if test="${tipo == 'metodoPago'}"><c:set var="itemId" value="${item.idMetodoPago}"/></c:if>
                            <c:if test="${tipo == 'tipoProducto'}"><c:set var="itemId" value="${item.idTipoProducto}"/></c:if>

                            <div class="catalogos__itemAcciones">
                                <%-- Editar (ROL 1 y 2) --%>
                                <% if (idRolSesion == 1 || idRolSesion == 2) { %>
                                <a href="${pageContext.request.contextPath}/Catalogos?tipo=${tipo}&accion=editar&id=${itemId}">
                                    <button class="boton boton--editar catalogos__btnAccion">✏️</button>
                                </a>
                                <% } %>

                                <%-- Eliminar (solo ROL 1) --%>
                                <% if (idRolSesion == 1) { %>
                                <form method="post" action="${pageContext.request.contextPath}/Catalogos"
                                      class="catalogos__formEliminar"
                                      onsubmit="return confirmarEliminar(this, '${tipo}')">
                                    <input type="hidden" name="tipo"   value="${tipo}">
                                    <input type="hidden" name="accion" value="eliminar">
                                    <input type="hidden" name="id"     value="${itemId}">
                                    <button type="submit" class="boton boton--Eliminar catalogos__btnAccion">🗑️</button>
                                </form>
                                <% } %>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </section>

    <a href="${pageContext.request.contextPath}/Menu" style="text-decoration:none;">
        <button class="boton boton--volver" style="max-width:380px;width:100%;margin:1rem auto;display:block;">
            Volver al menú
        </button>
    </a>
</main>

<script>
function validarCatalogo() {
    const nombre = document.getElementById('inputNombre').value.trim();
    if (!nombre) {
        Swal.fire({ icon:'warning', title:'Campo vacío',
            text:'El nombre no puede estar vacío.',
            confirmButtonText:'Entendido', confirmButtonColor:'#1E88E5' });
        return false;
    }
    if (nombre.length > 100) {
        Swal.fire({ icon:'error', title:'Nombre muy largo',
            text:'El nombre no puede superar 100 caracteres.',
            confirmButtonText:'Entendido', confirmButtonColor:'#1E88E5' });
        return false;
    }
    return true;
}

function confirmarEliminar(form, tipo) {
    const nombres = {
        categoriaEgreso:  'esta categoría de egresos',
        categoriaIngreso: 'esta categoría de ingresos',
        metodoPago:       'este método de pago',
        tipoProducto:     'este tipo de producto'
    };
    Swal.fire({
        icon: 'warning',
        title: '¿Eliminar?',
        html: '¿Seguro que deseas eliminar ' + (nombres[tipo] || 'este ítem') + '?<br>' +
              '<small>No se puede eliminar si tiene registros asociados.</small>',
        showCancelButton: true,
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#E51E1E',
        cancelButtonColor: '#9E9E9E'
    }).then(function(result) {
        if (result.isConfirmed) form.submit();
    });
    return false;
}

// Mensajes de éxito y error desde URL
window.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const exito  = params.get('exito');
    const error  = params.get('error');

    const exitoMensajes = {
        crear:      '¡Registro creado correctamente!',
        actualizar: '¡Registro actualizado correctamente!',
        eliminar:   'Registro eliminado.'
    };

    if (exito && exitoMensajes[exito]) {
        Swal.fire({ icon:'success', title:'Listo', text: exitoMensajes[exito],
            timer:2000, showConfirmButton:false });
        window.history.replaceState({}, document.title, window.location.pathname +
            window.location.search.replace(/[&?]exito=[^&]*/g, ''));
    }
    if (error === 'sin_permiso') {
        Swal.fire({ icon:'error', title:'Sin permisos',
            text:'No tienes permisos para realizar esa acción.',
            confirmButtonColor:'#1E88E5' });
        window.history.replaceState({}, document.title, window.location.pathname);
    }
});
</script>
</body>
</html>

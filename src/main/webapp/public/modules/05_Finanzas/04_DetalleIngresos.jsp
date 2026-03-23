<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
        return;
    }
    Integer idRol = (Integer) session.getAttribute("idRol");
    if (idRol != null && idRol == 3) {
        response.sendRedirect(request.getContextPath() + "/Menu?error=sin_permiso_finanzas");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/05_Finanzas/estilosDetalleIngresos.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png" alt="Logo">
        <a href="${pageContext.request.contextPath}/Finanzas">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Detalle de Ingresos</h1>
        </div>
    </header>

    <main class="consultarFacturas">

        <%-- Mensajes manejados por SweetAlert2 al cargar la página --%>

        <c:choose>
            <c:when test="${empty ingresos}">
                <div style="text-align:center;padding:40px 20px;color:#888;">
                    <p>No hay ingresos registrados.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="ingreso" items="${ingresos}">
                <div class="facturaLista">
                    <div class="facturaCard facturaCard--verde">
                        <div class="facturaCard__borde"></div>
                        <div class="facturaCard__contenido">
                            <div class="facturaCard__encabezado">
                                <h3 class="facturaCard__titulo">${ingreso.nombreCategoria}</h3>
                                <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">
                                    $ <fmt:formatNumber value="${ingreso.monto}" pattern="#,##0.00"/>
                                </span>
                            </div>
                            <p class="facturaCard__detalles">
                                ${ingreso.fechaIngresoFormateada}
                                <c:if test="${not empty ingreso.descripcion}"> — ${ingreso.descripcion}</c:if>
                            </p>

                            <%--
                                CONTROL DE ROLES:
                                Rol 1 (Admin) → puede editar Y eliminar
                                Rol 2 (Adulto) → solo puede ver (ni editar ni eliminar)
                            --%>
                            <% if (idRol != null && idRol == 1) { %>
                            <div style="display:flex;gap:8px;margin-top:6px;">
                                <a href="${pageContext.request.contextPath}/Finanzas?accion=editarIngreso&amp;id=${ingreso.idIngresos}">
                                    <button class="botonAccion botonAccion--editar">✏️ Editar</button>
                                </a>
                                <form id="fAnularIng${ingreso.idIngresos}"
                                      action="${pageContext.request.contextPath}/Finanzas" method="post"
                                      style="display:inline">
                                    <input type="hidden" name="accion"    value="anularIngreso">
                                    <input type="hidden" name="idIngreso" value="${ingreso.idIngresos}">
                                    <button type="button" class="botonAccion botonAccion--eliminar"
                                            onclick="confirmarAnular('fAnularIng${ingreso.idIngresos}','ingreso')">🚫 Anular</button>
                                </form>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
                </c:forEach>

                <div class="totalPagado">
                    <h3 class="totalPagado__titulo">Total ingresos</h3>
                    <p class="totalPagado__monto">$ <fmt:formatNumber value="${totalIngresos}" pattern="#,##0.00"/></p>
                </div>
            </c:otherwise>
        </c:choose>

        <%-- ── Sección de anulados (solo Rol 1) ─────────────────────────── --%>
        <% if (idRol != null && idRol == 1) { %>
        <c:if test="${not empty ingresosAnulados}">
        <div style="margin-top:16px;">
            <button onclick="toggleAnulados('anulados-ing')" class="botonAccion"
                    style="width:100%;padding:10px;border-radius:10px;background:#F5F5F5;color:#666;font-size:13px;">
                📂 Ver ingresos anulados (<c:out value="${fn:length(ingresosAnulados)}"/>)
            </button>
            <div id="anulados-ing" style="display:none;margin-top:10px;">
                <c:forEach var="anulado" items="${ingresosAnulados}">
                <div style="background:#FAFAFA;border-radius:12px;padding:14px;margin-bottom:8px;
                            border:1px solid #E0E0E0;opacity:0.75;position:relative;">
                    <div style="position:absolute;left:0;top:0;bottom:0;width:4px;
                                background:#BDBDBD;border-radius:4px;"></div>
                    <div style="padding-left:12px;">
                        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:4px;">
                            <span style="font-size:15px;font-weight:700;color:#9E9E9E;text-decoration:line-through;">
                                ${anulado.nombreCategoria}
                            </span>
                            <span style="padding:4px 12px;border-radius:8px;font-size:12px;
                                         background:#EEEEEE;color:#9E9E9E;">
                                🚫 Anulado
                            </span>
                        </div>
                        <p style="font-size:13px;color:#BDBDBD;margin:0 0 8px;">
                            ${anulado.fechaIngresoFormateada}
                            <c:if test="${not empty anulado.descripcion}"> — ${anulado.descripcion}</c:if>
                        </p>
                        <form id="fReactivarIng${anulado.idIngresos}"
                              action="${pageContext.request.contextPath}/Finanzas" method="post"
                              style="display:inline">
                            <input type="hidden" name="accion"    value="reactivarIngreso">
                            <input type="hidden" name="idIngreso" value="${anulado.idIngresos}">
                            <button type="button" class="botonAccion botonAccion--editar"
                                    style="font-size:12px;"
                                    onclick="confirmarReactivar('fReactivarIng${anulado.idIngresos}','ingreso')">✅ Reactivar</button>
                        </form>
                    </div>
                </div>
                </c:forEach>
            </div>
        </div>
        </c:if>
        <% } %>

        <a href="${pageContext.request.contextPath}/Finanzas?accion=formIngreso" class="consultarFacturas__boton">
            <button class="boton boton--registrar">+ Registrar nuevo ingreso</button>
        </a>
        <a href="${pageContext.request.contextPath}/Finanzas" class="consultarFacturas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>
<script>
function toggleAnulados(id) {
    var el = document.getElementById(id);
    el.style.display = el.style.display === 'none' ? 'block' : 'none';
}

function confirmarAnular(formId, tipo) {
    Swal.fire({
        title: '¿Anular este ' + tipo + '?',
        text: 'Quedará guardado y podrás reactivarlo después.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#E51E1E',
        cancelButtonColor: '#9E9E9E',
        confirmButtonText: '🚫 Sí, anular',
        cancelButtonText: 'Cancelar'
    }).then(function(result) {
        if (result.isConfirmed) {
            document.getElementById(formId).submit();
        }
    });
}

function confirmarReactivar(formId, tipo) {
    Swal.fire({
        title: '¿Reactivar este ' + tipo + '?',
        text: 'Volverá a aparecer en tu lista activa.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#4CAF50',
        cancelButtonColor: '#9E9E9E',
        confirmButtonText: '✅ Sí, reactivar',
        cancelButtonText: 'Cancelar'
    }).then(function(result) {
        if (result.isConfirmed) {
            document.getElementById(formId).submit();
        }
    });
}

window.addEventListener('DOMContentLoaded', function() {
    var params = new URLSearchParams(window.location.search);
    var exito = params.get('exito');
    var error = params.get('error');
    if (exito === 'ingreso_anulado') {
        Swal.fire({ icon: 'success', title: '¡Anulado!', text: 'Ingreso anulado correctamente.',
                    timer: 2500, showConfirmButton: false });
    } else if (exito === 'ingreso_reactivado') {
        Swal.fire({ icon: 'success', title: '¡Reactivado!', text: 'Ingreso reactivado correctamente.',
                    timer: 2500, showConfirmButton: false });
    } else if (error === 'sin_permiso') {
        Swal.fire({ icon: 'error', title: 'Sin permiso', text: 'No tienes permiso para esa acción.',
                    timer: 2500, showConfirmButton: false });
    } else if (error === 'no_se_pudo_anular' || error === 'no_se_pudo_reactivar') {
        Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo completar la acción. Verifica que el registro exista.',
                    timer: 3000, showConfirmButton: false });
    }
});
</script>
</body>
</html>

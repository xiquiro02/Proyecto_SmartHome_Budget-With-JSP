<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="com.smarthome.smarthome_budget.modelo.Usuario" %>
<%@page import="com.smarthome.smarthome_budget.modelo.RegistroEgreso" %>
<%@page import="com.smarthome.smarthome_budget.modelo.PresupuestoMensual" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="java.time.format.DateTimeFormatter" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/Menuprincipal/estilosMenuprincipal.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/components/notificacionesSistema.css">
    <title>SmartHome Budget</title>
</head>

<%
    // Verificar sesión activa
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() +
            "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
        return;
    }
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
    Integer idRolSesion   = (Integer) session.getAttribute("idRol");
    if (idRolSesion == null) idRolSesion = 3;

    // Datos del dashboard inyectados por MenuPrincipalServlet
    RegistroEgreso proxPago   = (RegistroEgreso)    request.getAttribute("proximoPago");
    BigDecimal disponible     = (BigDecimal)         request.getAttribute("disponible");
    PresupuestoMensual pres   = (PresupuestoMensual) request.getAttribute("presupuesto");

    // CORRECCIÓN FASE 3/5: usa getNombreUsuario() con alias de fallback
    String nombreMostrar = usuarioSesion.getNombreUsuario() != null && !usuarioSesion.getNombreUsuario().isEmpty()
        ? usuarioSesion.getNombreUsuario()
        : usuarioSesion.getApellido();

    // Código de invitación recién generado (desde GenerarCodigoInvitacionServlet)
    String codigoGenerado = (String) request.getAttribute("codigoGenerado");
    String rolAsignado    = (String) request.getAttribute("rolAsignado");

    // Errores del filtro
    String errorParam = request.getParameter("error");
%>

<body>
    <header class="encabezado">
        <img class="encabezado__imagen"
             src="${pageContext.request.contextPath}/asset/imagenes/icono-smarthome.png"
             alt="Logo SmartHome Budget">
        <h1 class="encabezado__titulo">SmartHome Budget</h1>
    </header>

    <main class="menuPrincipal">

        <%-- Código de invitación generado --%>
        <% if (codigoGenerado != null && !codigoGenerado.isEmpty()) { %>
        <div class="tarjetaCodigo">
            <p class="tarjetaCodigo__titulo">¡Código generado para <%= rolAsignado %>!</p>
            <p class="tarjetaCodigo__codigo"><%= codigoGenerado %></p>
            <p class="tarjetaCodigo__subtitulo">Válido por 7 días. Compártelo con el nuevo miembro.</p>
        </div>
        <% } %>

        <div class="tarjetasResumen">

            <%-- Tarjeta: Próximo pago --%>
            <div class="tarjetaResumen tarjetaResumen--verde">
                <img class="tarjetaResumen__icono"
                     src="${pageContext.request.contextPath}/asset/imagenes/icono-luz.png" alt="Pago">
                <div class="tarjetaResumen__contenido">
                    <% if (proxPago != null) { %>
                    <div class="tarjetaResumen__fila">
                        <p class="tarjetaResumen__titulo">Próximo pago:</p>
                        <p class="tarjetaResumen__valor"><%= proxPago.getNombreCategoriaEgreso() %></p>
                    </div>
                    <div class="tarjetaResumen__fila">
                        <p class="tarjetaResumen__titulo">Vence:</p>
                        <p class="tarjetaResumen__valor--fecha">
                            <%= proxPago.getFechaVencimiento() != null
                                ? proxPago.getFechaVencimiento().format(DateTimeFormatter.ofPattern("d MMM"))
                                : "—" %>
                        </p>
                    </div>
                    <% } else { %>
                    <div class="tarjetaResumen__fila">
                        <p class="tarjetaResumen__titulo">Próximo pago:</p>
                        <p class="tarjetaResumen__valor">Sin pagos pendientes</p>
                    </div>
                    <% } %>
                </div>
            </div>

            <%-- Tarjeta: Presupuesto disponible (solo ROL 1 y 2) --%>
            <% if (idRolSesion == 1 || idRolSesion == 2) { %>
            <div class="tarjetaResumen tarjetaResumen--azul">
                <img class="tarjetaResumen__icono"
                     src="${pageContext.request.contextPath}/asset/imagenes/Presupuesto.png" alt="Presupuesto">
                <div class="tarjetaResumen__contenido">
                    <p class="tarjetaResumen__titulo">Presupuesto disponible:</p>
                    <% if (pres != null && disponible != null) { %>
                        <p class="tarjetaResumen__valor">
                            $<fmt:formatNumber value="<%= disponible %>" pattern="#,##0"/>
                        </p>
                    <% } else { %>
                        <p class="tarjetaResumen__valor">Sin configurar</p>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>

        <%-- Saludo dinámico --%>
        <div class="bloqueSaludo">
            <h2 class="bloqueSaludo__titulo">Hola, <%= nombreMostrar %>!</h2>
            <p class="bloqueSaludo__subtitulo">¿Qué deseas hacer hoy?</p>
        </div>

        <div class="gridOpciones">

            <%-- Facturas → Servlet /Facturas --%>
            <a href="${pageContext.request.contextPath}/Facturas" class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                     src="${pageContext.request.contextPath}/asset/imagenes/facturas.png" alt="Facturas">
                <p class="tarjetaOpcion__titulo">Facturas y Pagos</p>
            </a>

            <%-- Listas → Servlet /Listas --%>
            <a href="${pageContext.request.contextPath}/Listas" class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                     src="${pageContext.request.contextPath}/asset/imagenes/listas-deCompras.png" alt="Listas">
                <p class="tarjetaOpcion__titulo">Listas de Compras</p>
            </a>

            <%-- Inventario → Servlet /Inventario --%>
            <a href="${pageContext.request.contextPath}/Inventario" class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                     src="${pageContext.request.contextPath}/asset/imagenes/mi-inventario.png" alt="Inventario">
                <p class="tarjetaOpcion__titulo">Mi Inventario</p>
            </a>

            <%-- Finanzas → solo ROL 1 y 2 (RF30) --%>
            <% if (idRolSesion == 1 || idRolSesion == 2) { %>
            <a href="${pageContext.request.contextPath}/Finanzas" class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                     src="${pageContext.request.contextPath}/asset/imagenes/finanzas.png" alt="Finanzas">
                <p class="tarjetaOpcion__titulo">Finanzas</p>
            </a>
            <% } %>

            <%-- Ajustes --%>
            <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp"
               class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                     src="${pageContext.request.contextPath}/asset/imagenes/ajustes.png" alt="Ajustes">
                <p class="tarjetaOpcion__titulo">Ajustes</p>
            </a>

            <%-- Generar código de invitación — solo ADMINISTRADOR (ROL 1) --%>
            <% if (idRolSesion == 1) { %>
            <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/10_CodigoVerificacion.jsp"
               class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                     src="${pageContext.request.contextPath}/asset/imagenes/agregar-usuario.png" alt="Invitar">
                <p class="tarjetaOpcion__titulo">Invitar miembro</p>
            </a>
            <% } %>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/asset/js/notificaciones.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            <% if ("acceso_denegado".equals(errorParam)) { %>
            mostrarBanner('No tienes permisos para acceder a esa sección.', 'error');
            <% } %>

            verificarNotificacionesMenu({
                // Pagos
                fechaVencimiento:      '<%= proxPago != null && proxPago.getFechaVencimiento() != null ? proxPago.getFechaVencimiento().toString() : "" %>',
                nombrePago:            '<%= proxPago != null && proxPago.getNombreCategoriaEgreso() != null ? proxPago.getNombreCategoriaEgreso().replace("'", "\\'") : "" %>',
                // Confirmación de pago reciente (redirige con ?pagado=NombrePago)
                pagoConfirmado:        '<%= request.getParameter("pagado") != null ? request.getParameter("pagado").replace("'", "\\'") : "" %>',
                // Presupuesto
                porcentajePresupuesto: <%= pres != null ? pres.getPorcentajeUsado() : 0 %>,
                totalGastadoMes:       <%= pres != null && pres.getTotalEgresos() != null ? pres.getTotalEgresos().doubleValue() : 0 %>,
                // Inventario y listas (se activan cuando el servlet los exponga)
                productosAgotados:     <%= request.getAttribute("productosAgotados") != null ? request.getAttribute("productosAgotados") : 0 %>,
                listasConPendientes:   <%= request.getAttribute("listasConPendientes") != null ? request.getAttribute("listasConPendientes") : 0 %>
            });
        });
    </script>

    <%-- Barra de navegación inferior --%>
    <nav class="navInferior">
        <%-- CORRECCIÓN FASE 5: botón Inicio apunta a /Menu (servlet) --%>
        <a href="${pageContext.request.contextPath}/Menu" class="navInferior__item navInferior__item--activo">
            <img class="navInferior__icono"
                 src="${pageContext.request.contextPath}/asset/imagenes/hogar.png" alt="Inicio">
            <p>Inicio</p>
        </a>
        <%-- CORRECCIÓN FASE 5: botón Perfil apunta a /Perfil (servlet) --%>
        <a href="${pageContext.request.contextPath}/Perfil" class="navInferior__item">
            <%-- Foto de perfil si existe, sino icono por defecto --%>
            <% if (usuarioSesion.getFotoPerfil() != null && !usuarioSesion.getFotoPerfil().isEmpty()) { %>
            <img class="navInferior__icono"
                 src="${pageContext.request.contextPath}<%= usuarioSesion.getFotoPerfil() %>"
                 alt="Perfil" style="border-radius:50%;object-fit:cover;">
            <% } else { %>
            <img class="navInferior__icono"
                 src="${pageContext.request.contextPath}/asset/imagenes/usuario.png" alt="Perfil">
            <% } %>
            <p>Perfil</p>
        </a>
    </nav>
</body>
</html>

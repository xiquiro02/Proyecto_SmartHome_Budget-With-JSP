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
        href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/asset/css/modules/Menuprincipal/estilosMenuprincipal.css">
    <title>SmartHome Budget</title>
</head>

<%
    // Verificar sesión activa
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
        return;
    }
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
    Integer idRolSesion   = (Integer) session.getAttribute("idRol");
    if (idRolSesion == null) idRolSesion = 3;

    // Datos del dashboard inyectados por MenuPrincipalServlet
    RegistroEgreso proxPago   = (RegistroEgreso) request.getAttribute("proximoPago");
    BigDecimal disponible     = (BigDecimal)     request.getAttribute("disponible");
    PresupuestoMensual pres   = (PresupuestoMensual) request.getAttribute("presupuesto");

    String nombreMostrar = usuarioSesion.getNombreUsuario() != null
        ? usuarioSesion.getNombreUsuario() : usuarioSesion.getPrimerApellido();
%>

<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/icono-smarthome.png"
            alt="Logo SmartHome Budget">
        <h1 class="encabezado__titulo">SmartHome Budget</h1>
    </header>
    <main class="menuPrincipal">
        <div class="tarjetasResumen">

            <!-- Tarjeta: Próximo pago -->
            <div class="tarjetaResumen tarjetaResumen--verde">
                <img class="tarjetaResumen__icono"
                    src="${pageContext.request.contextPath}/asset/imagenes/icono-luz.png" alt="Pago">
                <div class="tarjetaResumen__contenido">
                    <% if (proxPago != null) { %>
                    <div class="tarjetaResumen__fila">
                        <p class="tarjetaResumen__titulo">Próximo pago:</p>
                        <p class="tarjetaResumen__valor"><%= proxPago.getNombreFactura() %></p>
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

            <!-- Tarjeta: Presupuesto disponible -->
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

        </div>

        <!-- Saludo dinámico -->
        <div class="bloqueSaludo">
            <h2 class="bloqueSaludo__titulo">Hola, <%= nombreMostrar %>!</h2>
            <p class="bloqueSaludo__subtitulo">¿Qué deseas hacer hoy?</p>
        </div>

        <div class="gridOpciones">
            <!-- Facturas → Servlet -->
            <a href="${pageContext.request.contextPath}/Facturas" class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                    src="${pageContext.request.contextPath}/asset/imagenes/facturas.png" alt="Facturas">
                <p class="tarjetaOpcion__titulo">Facturas y Pagos</p>
            </a>

            <!-- Listas → Servlet -->
            <a href="${pageContext.request.contextPath}/Listas" class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                    src="${pageContext.request.contextPath}/asset/imagenes/listas-deCompras.png" alt="Listas">
                <p class="tarjetaOpcion__titulo">Listas de Compras</p>
            </a>

            <!-- Inventario → Servlet -->
            <a href="${pageContext.request.contextPath}/Inventario" class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                    src="${pageContext.request.contextPath}/asset/imagenes/mi-inventario.png" alt="Inventario">
                <p class="tarjetaOpcion__titulo">Mi Inventario</p>
            </a>

            <!-- Finanzas → Servlet (solo ROL 1 y 2) -->
            <% if (idRolSesion == 1 || idRolSesion == 2) { %>
            <a href="${pageContext.request.contextPath}/Finanzas" class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                    src="${pageContext.request.contextPath}/asset/imagenes/finanzas.png" alt="Finanzas">
                <p class="tarjetaOpcion__titulo">Finanzas</p>
            </a>
            <% } %>

            
            <!-- Ajustes -->
            <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp"
                class="tarjetaOpcion">
                <img class="tarjetaOpcion__icono"
                    src="${pageContext.request.contextPath}/asset/imagenes/ajustes.png" alt="Ajustes">
                <p class="tarjetaOpcion__titulo">Ajustes</p>
            </a>
        </div>
    </main>

    <nav class="navInferior">
        <a href="${pageContext.request.contextPath}/Menu" class="navInferior__item navInferior__item--activo">
            <img class="navInferior__icono" src="${pageContext.request.contextPath}/asset/imagenes/hogar.png"
                alt="Inicio">
            <p>Inicio</p>
        </a>
        <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/02_notificacionesResumen-Filtro-Todas.jsp"
            class="navInferior__item">
            <img class="navInferior__icono"
                src="${pageContext.request.contextPath}/asset/imagenes/notificaciones.png" alt="Recordatorios">
            <p>Recordatorios</p>
        </a>
        <a href="${pageContext.request.contextPath}/Perfil" class="navInferior__item">
            <img class="navInferior__icono" src="${pageContext.request.contextPath}/asset/imagenes/usuario.png"
                alt="Perfil">
            <p>Perfil</p>
        </a>
    </nav>
</body>

</html>

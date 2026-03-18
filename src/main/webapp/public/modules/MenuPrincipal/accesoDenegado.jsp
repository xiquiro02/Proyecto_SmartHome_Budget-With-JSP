<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <title>SmartHome Budget — Acceso denegado</title>
    <style>
        .acceso-denegado {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            padding: 2rem;
            background: #fff;
            text-align: center;
            gap: 1rem;
        }
        .acceso-denegado__icono {
            font-size: 4rem;
        }
        .acceso-denegado__titulo {
            font-size: 1.4rem;
            font-weight: 700;
            color: #E51E1E;
            margin: 0;
        }
        .acceso-denegado__mensaje {
            font-size: 1rem;
            color: #555;
            max-width: 320px;
            line-height: 1.5;
        }
        .acceso-denegado__boton {
            margin-top: 1rem;
            padding: 0.9rem 2rem;
            background: var(--btn-primary, #1E88E5);
            color: #fff;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: background 0.2s;
        }
        .acceso-denegado__boton:hover {
            background: #1565C0;
        }
    </style>
</head>
<body>
    <%-- Si no hay sesión, redirigir al login --%>
    <%
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }
    %>

    <main class="acceso-denegado">
        <div class="acceso-denegado__icono">🔒</div>
        <h1 class="acceso-denegado__titulo">Acceso denegado</h1>
        <p class="acceso-denegado__mensaje">
            No tienes permisos para acceder a esta sección.
            Si crees que esto es un error, contacta al administrador de tu hogar.
        </p>
        <a href="${pageContext.request.contextPath}/Menu" class="acceso-denegado__boton">
            Volver al menú
        </a>
    </main>
</body>
</html>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%
    // Solo ADMINISTRADOR (ROL 1) puede acceder
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() +
            "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
        return;
    }
    Integer idRolSesion = (Integer) session.getAttribute("idRol");
    if (idRolSesion == null || idRolSesion != 1) {
        response.sendRedirect(request.getContextPath() + "/Menu?error=acceso_denegado");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/Menuprincipal/estilosCodigoVerificacion.css">
    <title>SmartHome Budget — Invitar miembro</title>
</head>
<body>
    <header class="encabezado">
        <a href="${pageContext.request.contextPath}/Menu">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
    </header>

    <main class="contenido">
        <div class="contenedor">
            <img class="contenido__icono-img"
                 src="${pageContext.request.contextPath}/asset/imagenes/agregar-usuario.png"
                 alt="Invitar miembro">
            <h1 class="contenido__titulo">Invitar miembro</h1>
            <p class="contenido__parrafo">
                Genera un código de invitación para que un familiar se una a tu hogar.
                Elige el rol que tendrá dentro del hogar:
            </p>

            <%-- Formulario que envía al servlet GenerarCodigoInvitacion --%>
            <form method="post" action="${pageContext.request.contextPath}/GenerarCodigoInvitacion">

                <div class="contenido__grupo">
                    <label class="contenido__label">Rol del nuevo miembro</label>

                    <label style="display:flex;align-items:center;gap:10px;padding:12px;
                                  border:2px solid #E0E0E0;border-radius:10px;margin-bottom:8px;cursor:pointer;">
                        <input type="radio" name="idRol" value="2" required>
                        <div>
                            <strong>Cotitular</strong>
                            <p style="font-size:12px;color:#666;margin:2px 0 0;">
                                Puede gestionar inventario, listas y ver resumen financiero.
                            </p>
                        </div>
                    </label>

                    <label style="display:flex;align-items:center;gap:10px;padding:12px;
                                  border:2px solid #E0E0E0;border-radius:10px;cursor:pointer;">
                        <input type="radio" name="idRol" value="3">
                        <div>
                            <strong>Invitado (Hijo)</strong>
                            <p style="font-size:12px;color:#666;margin:2px 0 0;">
                                Solo puede ver inventario y agregar productos a listas.
                            </p>
                        </div>
                    </label>
                </div>

                <p style="font-size:12px;color:#888;margin:8px 0 16px;text-align:center;">
                    El código generado es válido por 7 días.
                </p>

                <div class="contenido__grupo">
                    <button type="submit" class="boton boton--registrar">
                        Generar código
                    </button>
                    <a href="${pageContext.request.contextPath}/Menu">
                        <button type="button" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </form>
        </div>
    </main>
</body>
</html>

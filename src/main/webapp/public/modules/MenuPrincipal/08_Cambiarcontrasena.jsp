<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/Menuprincipal/estilosCambiarContrasena.css">
    <title>SmartHome Budget</title>
</head>
<body>
    <main class="contenido">
        <div class="contenedor">
            <h1 class="contenido__titulo">Cambiar Contraseña</h1>
            <p class="contenido__parrafo">Por seguridad, crea una nueva contraseña diferente a la anterior.</p>

            <c:if test="${not empty error}">
                <div style="padding:10px;background:#ffe0e0;border-radius:8px;color:#c00;margin:10px 0;">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/Seguridad">
                <input type="hidden" name="accion" value="cambiarContrasena">

                <div class="contenido__grupo">
                    <label for="password" class="contenido__label">Nueva Contraseña</label>
                    <input type="password" class="contenido__input" id="password" name="password"
                        placeholder="Ingresa tu nueva contraseña" required minlength="8">
                </div>
                <div class="contenido__grupo">
                    <label for="password2" class="contenido__label">Confirmar Contraseña</label>
                    <input type="password" class="contenido__input" id="password2" name="password2"
                        placeholder="Repite la contraseña" required minlength="8">
                </div>

                <p class="contenido__parrafo">Requisitos mínimos:</p>
                <div class="contenido__grupo--fila">
                    <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="✓" style="width:20px;height:20px;">
                    <p class="contenido__label">Mínimo 8 caracteres</p>
                </div>
                <div class="contenido__grupo--fila">
                    <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="✓" style="width:20px;height:20px;">
                    <p class="contenido__label">Una letra mayúscula</p>
                </div>
                <div class="contenido__grupo--fila">
                    <img class="contenido__icono-img" src="${pageContext.request.contextPath}/asset/imagenes/icono_confirmacion.png" alt="✓" style="width:20px;height:20px;">
                    <p class="contenido__label">Un símbolo especial (!@#$...)</p>
                </div>

                <div class="contenido__grupo">
                    <button type="submit" class="boton boton--registrar">Guardar</button>
                    <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp">
                        <button type="button" class="boton boton--cancelar">Cancelar</button>
                    </a>
                </div>
            </form>
        </div>
    </main>
</body>
</html>

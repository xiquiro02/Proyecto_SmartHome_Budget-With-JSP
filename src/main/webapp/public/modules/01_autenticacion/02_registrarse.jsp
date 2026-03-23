<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosRegistrarse.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/01_principal.jsp">
            <span class="material-symbols-outlined">arrow_back_ios_new</span>
        </a>
    </header>

    <main class="contenido">
        <div class="contenedor">
            <h1 class="contenido__titulo">Registrarse</h1>

            <%
                String errorParam = request.getParameter("error");
                if (errorParam != null) {
                    String msg = "";
                    switch (errorParam) {
                        case "campos_vacios":    msg = "Todos los campos obligatorios deben completarse."; break;
                        case "correo_existe":    msg = "Este correo ya está registrado."; break;
                        case "documento_existe": msg = "Este documento ya está registrado."; break;
                        case "codigo_invalido":  msg = "El código de invitación no es válido o ha expirado."; break;
                        case "registro_fallido": msg = "No se pudo completar el registro. Intenta de nuevo."; break;
                        default: msg = "Ocurrió un error. Intenta de nuevo.";
                    }
            %>
            <div style="padding:10px;background:#ffe0e0;border-radius:8px;color:#c00;margin-bottom:12px;text-align:center;">
                <%= msg %>
            </div>
            <% } %>

            <%-- novalidate suprime los globos del navegador; onsubmit ejecuta nuestra validación JS --%>
            <form class="contenido__formulario" method="post"
                  action="${pageContext.request.contextPath}/Registro"
                  novalidate
                  onsubmit="return validarFormularioRegistro()">

                <div class="contenido__grupo">
                    <label for="documento" class="contenido__label">Documento de identidad</label>
                    <input type="text" class="contenido__input" id="documento" name="documento"
                           placeholder="Ej: 1234567890" required maxlength="11"
                           pattern="[0-9]{6,11}"
                           title="El documento debe contener solo números (entre 6 y 11 dígitos)"/>
                    <span class="campo-error" id="error-documento"></span>
                </div>

                <div class="contenido__grupo">
                    <label for="nombre" class="contenido__label">Nombre</label>
                    <input type="text" class="contenido__input" id="nombre" name="nombre"
                           placeholder="Ingresa tu nombre" required maxlength="50"
                           pattern="^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"
                           title="Solo letras, sin números ni símbolos"/>
                    <span class="campo-error" id="error-nombre"></span>
                </div>

                <div class="contenido__grupo">
                    <label for="apellido" class="contenido__label">Apellido</label>
                    <input type="text" class="contenido__input" id="apellido"
                           name="apellido1" placeholder="Ingresa tu apellido" required
                           maxlength="50" pattern="^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"
                           title="Solo letras, sin números ni símbolos"/>
                    <span class="campo-error" id="error-apellido"></span>
                </div>

                <div class="contenido__grupo">
                    <label for="segundoApellido" class="contenido__label">
                        Segundo Apellido <span style="color:#999;font-size:12px;">(opcional)</span>
                    </label>
                    <input type="text" class="contenido__input" id="segundoApellido"
                           name="apellido2" placeholder="Ingresa tu segundo apellido"
                           maxlength="50" pattern="^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"
                           title="Solo letras, sin números ni símbolos"/>
                    <span class="campo-error" id="error-segundoApellido"></span>
                </div>

                <div class="contenido__grupo">
                    <label for="correo" class="contenido__label">Correo electrónico</label>
                    <input type="email" class="contenido__input" id="correo" name="correo"
                           placeholder="Ingresa tu correo" required/>
                    <span class="campo-error" id="error-correo"></span>
                </div>

                <div class="contenido__grupo">
                    <label for="telefono" class="contenido__label">Número de teléfono</label>
                    <input type="tel" class="contenido__input" id="telefono" name="telefono"
                           placeholder="Ingresa tu número de teléfono" required
                           pattern="[0-9]{8,15}"
                           title="Solo números, entre 8 y 15 dígitos"/>
                    <span class="campo-error" id="error-telefono"></span>
                </div>

                <div class="contenido__grupo">
                    <label for="password" class="contenido__label">Contraseña</label>
                    <input type="password" class="contenido__input" id="password"
                           name="contrasena" placeholder="Mínimo 8 caracteres, letras y números" required
                           minlength="8"
                           title="Mínimo 8 caracteres, al menos una letra y un número"/>
                    <span class="campo-error" id="error-password"></span>
                </div>

                <div class="contenido__grupo">
                    <label for="confirmarPassword" class="contenido__label">Confirmar Contraseña</label>
                    <input type="password" class="contenido__input" id="confirmarPassword"
                           name="confirmarContrasena" placeholder="Repite tu contraseña"
                           required minlength="8"/>
                    <span class="campo-error" id="error-confirmarPassword"></span>
                </div>

                <div class="contenido__grupo">
                    <label for="codigoInvitacion" class="contenido__label">
                        Código de invitación <span style="color:#999;font-size:12px;">(opcional)</span>
                    </label>
                    <input type="text" class="contenido__input" id="codigoInvitacion"
                           name="codigoInvitacion" placeholder="Ej: ABC12345" maxlength="50">
                    <span class="campo-error" id="error-codigoInvitacion"></span>
                    <small class="contenido__ayuda">
                        Si tienes un código, ingrésalo para unirte a un hogar existente.
                        Si lo dejas vacío, se creará un nuevo hogar automáticamente.
                    </small>
                </div>

                <div class="contenido__grupo">
                    <button type="submit" class="boton boton--registrar">Crear Cuenta</button>
                </div>
            </form>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/asset/js/validacionesRegistro.js"></script>
</body>
</html>

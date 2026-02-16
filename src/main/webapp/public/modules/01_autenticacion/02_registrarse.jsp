<%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <!-- Link iconos  -->
                <link rel="stylesheet"
                        href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
                <!-- Link Fuentes -->
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <!-- Link estilos.css  -->
                <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/utils/styles.css">
                <link rel="stylesheet"
                        href="${pageContext.request.contextPath}/asset/modules/01_autenticacion/estilosRegistrase.css">
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                <title>SmartHome Budget</title>
        </head>

        <body>
                <header class="encabezado">
                        <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/01_principal.jsp">
                                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
                        </a>
                </header>
                <main class="contenido">
                        <div class="contenedor">
                                <h1 class="contenido__titulo">Registrarse</h1>
                                <form class="contenido__formulario" method="post" onsubmit="return validarContrasenas()"
                                        action="${pageContext.request.contextPath}/RegistroServlet">
                                        <div class="contenido__grupo">
                                                <label for="nombre" class="contenido__label">Nombre</label>
                                                <input type="text" class="contenido__input" id="nombre" name="nombre"
                                                        placeholder="Ingresa tu nombre" required />
                                        </div>
                                        <div class="contenido__grupo">
                                                <label for="apellido" class="contenido__label">Apellido</label>
                                                <input type="text" class="contenido__input" id="apellido"
                                                        name="apellido1" placeholder="Ingresa tu apellido" required />
                                        </div>
                                        <div class="contenido__grupo">
                                                <label for="segundoApellido" class="contenido__label">Segundo
                                                        Apellido</label>
                                                <input type="text" class="contenido__input" id="segundoApellido"
                                                        name="apellido2" placeholder="Ingresa tu segundo apellido" />
                                        </div>
                                        <div class="contenido__grupo">
                                                <label for="correo" class="contenido__label">Correo</label>
                                                <input type="email" class="contenido__input" id="correo" name="correo"
                                                        placeholder="Ingresa tu correo" required />
                                        </div>
                                        <div class="contenido__grupo">
                                                <label for="telefono" class="contenido__label">Número de
                                                        teléfono</label>
                                                <input type="tel" class="contenido__input" id="telefono" name="telefono"
                                                        placeholder="Ingresa tu número de teléfono" required />
                                        </div>
                                        <div class="contenido__grupo">
                                                <label for="password" class="contenido__label">Contraseña</label>
                                                <input type="password" class="contenido__input" id="password"
                                                        name="contrasena" placeholder="Ingresa tu contraseña"
                                                        required />
                                        </div>
                                        <div class="contenido__grupo">
                                                <label for="confirmarPassword" class="contenido__label">Confirmar
                                                        Contraseña</label>
                                                <input type="password" class="contenido__input" id="confirmarPassword"
                                                        name="confirmarContrasena" placeholder="Confirma tu contraseña"
                                                        required />
                                        </div>
                                        <div class="contenido__grupo">
                                                <button type="submit" class="boton boton--registrar">Crear
                                                        Cuenta</button>
                                        </div>
                                </form>
                        </div>
                </main>
                <script src="${pageContext.request.contextPath}/asset/JavaScript/validarContrasenas.js"></script>
        </body>

        </html>
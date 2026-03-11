<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Link iconos  -->
        <link rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=chevron_right" />
        <!-- Link Fuentes -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <!-- Link estilos.css  -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
        <link rel="stylesheet"
            href="${pageContext.request.contextPath}/asset/css/modules/Menuprincipal/estilosAjustes.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/ajustes.png"
                alt="SmartHome Budget">
            <h1 class="encabezado__titulo">Ajustes</h1>
        </header>
        <main class="ajustes">
            <section class="ajustes__seccion">
                <h3 class="ajustes__seccion-titulo">Cuenta</h3>
                <a href="${pageContext.request.contextPath}/Perfil"
                    class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/editarPerfil.png" alt="Editar">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Editar perfil</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="${pageContext.request.contextPath}/Seguridad?accion=formCambiarContrasena"
                    class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/restablecer-la-contrasena.png"
                        alt="Contraseña">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Cambiar contraseña</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="${pageContext.request.contextPath}/Perfil?accion=cerrarSesion"
                    class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/cerrar-sesion.png" alt="Cerrar sesión">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Cerrar sesión</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="${pageContext.request.contextPath}/Perfil?accion=eliminarCuenta"
                    class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/borrar-usuario.png" alt="Eliminar">
                    <div class="ajustes__item-contenido ajustes__item-contenido--rojo">
                        <p class="ajustes__item-titulo ajustes__item-titulo--rojo">Eliminar cuenta</p>
                        <p class="ajustes__item-subtitulo">(Acción permanente)</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
            </section>
            <section class="ajustes__seccion">
                <h3 class="ajustes__seccion-titulo">Notificaciones</h3>
                <div class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png"
                        alt="Notificaciones">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Activar todas las notificaciones</p>
                        <div class="switch">
                            <input class="switch__input" id="switch1" type="checkbox">
                            <label class="switch__label" for="switch1"></label>
                        </div>
                    </div>
                </div>
                <div class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="Desactivar">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Desactivar todas las notificaciones</p>
                        <div class="switch">
                            <input class="switch__input" id="switch2" type="checkbox">
                            <label class="switch__label" for="switch2"></label>
                        </div>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/07_notificaciones.jsp"
                    class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="Ver">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Ver notificaciones</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
            </section>
            <section class="ajustes__seccion">
                <h3 class="ajustes__seccion-titulo">General</h3>
                <a href="#tema" class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/tema-claro-oscuro.png" alt="Tema">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Tema claro/oscuro</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="#tamañoidioma" class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/tamano-de-fuente.png"
                        alt="Tamaño de letra">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Tamaño de letra</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="#idioma" class="ajustes__item">
                    <img class="ajustes__item-icono" src="${pageContext.request.contextPath}/asset/imagenes/idioma.png"
                        alt="Idioma">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Idioma</p>
                        <p class="ajustes__item-subtitulo">Español</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="#privacidad" class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/politica-de-privacidad.png"
                        alt="Privacidad">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Política de privacidad</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="#terminos" class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/terminos-de-uso.png" alt="Términos">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Términos de uso</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="#ayuda" class="ajustes__item">
                    <img class="ajustes__item-icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/mesa-de-ayuda.png" alt="Ayuda">
                    <div class="ajustes__item-contenido">
                        <p class="ajustes__item-titulo">Centro de ayuda y soporte</p>
                    </div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
            </section>
            <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp"
                class="ajustes__boton">
                <button class="boton boton--volver">Volver</button>
            </a>
        </main>
    </body>

    </html>
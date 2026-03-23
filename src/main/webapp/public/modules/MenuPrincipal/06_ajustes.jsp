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
            <%
                Integer idRolAjustes = (Integer) session.getAttribute("idRol");
                if (idRolAjustes != null && (idRolAjustes == 1 || idRolAjustes == 2)) {
            %>
            <section class="ajustes__seccion">
                <h3 class="ajustes__seccion-titulo">Catálogos</h3>
                <a href="${pageContext.request.contextPath}/Catalogos?tipo=categoriaEgreso" class="ajustes__item">
                    <img class="ajustes__item-icono" src="${pageContext.request.contextPath}/asset/imagenes/Registrar-egresos.png" alt="Cat. Egresos">
                    <div class="ajustes__item-contenido"><p class="ajustes__item-titulo">Categorías de Egresos</p></div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="${pageContext.request.contextPath}/Catalogos?tipo=categoriaIngreso" class="ajustes__item">
                    <img class="ajustes__item-icono" src="${pageContext.request.contextPath}/asset/imagenes/ingresos-pasivos.png" alt="Cat. Ingresos">
                    <div class="ajustes__item-contenido"><p class="ajustes__item-titulo">Categorías de Ingresos</p></div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="${pageContext.request.contextPath}/Catalogos?tipo=metodoPago" class="ajustes__item">
                    <img class="ajustes__item-icono" src="${pageContext.request.contextPath}/asset/imagenes/metodo-de-pago.png" alt="Métodos de Pago">
                    <div class="ajustes__item-contenido"><p class="ajustes__item-titulo">Métodos de Pago</p></div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
                <a href="${pageContext.request.contextPath}/Catalogos?tipo=tipoProducto" class="ajustes__item">
                    <img class="ajustes__item-icono" src="${pageContext.request.contextPath}/asset/imagenes/productos.png" alt="Tipos de Producto">
                    <div class="ajustes__item-contenido"><p class="ajustes__item-titulo">Tipos de Producto</p></div>
                    <span class="material-symbols-outlined">chevron_right</span>
                </a>
            </section>
            <% } %>
            <a href="${pageContext.request.contextPath}/Menu" class="ajustes__boton">
                <button class="boton boton--volver">Volver</button>
            </a>
        </main>
    </body>

    </html>
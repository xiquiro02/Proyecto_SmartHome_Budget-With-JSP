<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new"/>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/modules/Menuprincipal/estilosNotificaciones.css">
    <title>SmartHome Budget — Notificaciones</title>
</head>
<body>
<header class="encabezado">
    <img class="encabezado__imagen"
         src="${pageContext.request.contextPath}/asset/imagenes/notification-icon-free-png.webp"
         alt="Notificaciones">
    <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp">
        <span class="material-symbols-outlined">arrow_back_ios_new</span>
    </a>
    <div class="encabezado__contenedorTitulo">
        <h1 class="encabezado__titulo">Notificaciones</h1>
    </div>
</header>

<main class="notificaciones">
    <h2 class="notificaciones__titulo">Tipos de notificaciones</h2>
    <section class="notificaciones__seccion">

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Recordatorios de pagos</p>
                <div class="switch">
                    <input class="switch__input" id="sw1" type="checkbox" data-key="notif_recordatorios_pagos">
                    <label class="switch__label" for="sw1"></label>
                </div>
            </div>
        </div>

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Pagos atrasados</p>
                <div class="switch">
                    <input class="switch__input" id="sw2" type="checkbox" data-key="notif_pagos_atrasados">
                    <label class="switch__label" for="sw2"></label>
                </div>
            </div>
        </div>

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Confirmación de pagos</p>
                <div class="switch">
                    <input class="switch__input" id="sw3" type="checkbox" data-key="notif_confirmacion_pagos">
                    <label class="switch__label" for="sw3"></label>
                </div>
            </div>
        </div>

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Lista de compras pendiente</p>
                <div class="switch">
                    <input class="switch__input" id="sw4" type="checkbox" data-key="notif_lista_compras">
                    <label class="switch__label" for="sw4"></label>
                </div>
            </div>
        </div>

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Productos por agotarse</p>
                <div class="switch">
                    <input class="switch__input" id="sw5" type="checkbox" data-key="notif_productos_agotarse">
                    <label class="switch__label" for="sw5"></label>
                </div>
            </div>
        </div>

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Recomendaciones de compra</p>
                <div class="switch">
                    <input class="switch__input" id="sw6" type="checkbox" data-key="notif_recomendaciones">
                    <label class="switch__label" for="sw6"></label>
                </div>
            </div>
        </div>

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Alertas de presupuesto</p>
                <div class="switch">
                    <input class="switch__input" id="sw7" type="checkbox" data-key="notif_presupuesto">
                    <label class="switch__label" for="sw7"></label>
                </div>
            </div>
        </div>

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Resumen semanal de gastos</p>
                <div class="switch">
                    <input class="switch__input" id="sw8" type="checkbox" data-key="notif_resumen_semanal">
                    <label class="switch__label" for="sw8"></label>
                </div>
            </div>
        </div>

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Alertas por gasto inusual</p>
                <div class="switch">
                    <input class="switch__input" id="sw9" type="checkbox" data-key="notif_gasto_inusual">
                    <label class="switch__label" for="sw9"></label>
                </div>
            </div>
        </div>

        <div class="notificaciones__item">
            <img class="notificaciones__icono" src="${pageContext.request.contextPath}/asset/imagenes/notificacion-TODAS.png" alt="">
            <div class="notificaciones__contenido">
                <p class="notificaciones__texto">Ofertas y descuentos cercanos</p>
                <div class="switch">
                    <input class="switch__input" id="sw10" type="checkbox" data-key="notif_ofertas">
                    <label class="switch__label" for="sw10"></label>
                </div>
            </div>
        </div>

    </section>

    <div id="toastGuardado" class="notificaciones__toast notificaciones__toast--oculto">
        ✅ Preferencias guardadas
    </div>

    <div class="notificaciones__botones">
        <button class="boton boton--registrar" onclick="guardarNotificaciones()">Guardar cambios</button>
        <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/06_ajustes.jsp" style="text-decoration:none;width:100%;">
            <button class="boton boton--cancelar">Volver</button>
        </a>
    </div>
</main>

<script>
    // Cargar estados desde localStorage al abrir la página
    document.querySelectorAll('.switch__input[data-key]').forEach(function(sw) {
        var val = localStorage.getItem(sw.dataset.key);
        // Por defecto activo (unchecked = verde). Si fue desactivado = checked = gris
        sw.checked = (val === 'off');
    });

    function guardarNotificaciones() {
        document.querySelectorAll('.switch__input[data-key]').forEach(function(sw) {
            localStorage.setItem(sw.dataset.key, sw.checked ? 'off' : 'on');
        });
        var toast = document.getElementById('toastGuardado');
        toast.classList.remove('notificaciones__toast--oculto');
        setTimeout(function() {
            toast.classList.add('notificaciones__toast--oculto');
        }, 2500);
    }

    // Utilidad pública: consultar si una notificación está activa desde otras páginas
    // Uso: NotifActiva('notif_recordatorios_pagos')
    window.NotifActiva = function(key) {
        return localStorage.getItem(key) !== 'off';
    };
</script>
</body>
</html>

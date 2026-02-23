<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Link iconos  -->
        <link rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
        <!-- Link Fuentes -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <!-- Link estilos.css  -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
        <link rel="stylesheet"
            href="${pageContext.request.contextPath}/asset/css/modules/06_Finanzas/estilosFinanzas.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <div class="encabezado__contenedor">
                <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/finanzas.png"
                    alt="Logo de Finanzas">
                <h1 class="encabezado__titulo">Finanzas</h1>
            </div>
            <div class="encabezado__contenido">
                <p class="encabezado__descripcion">Administra tus ingresos, gastos y presupuesto mensual en un solo
                    lugar.</p>
            </div>
        </header>
        <main class="finanzas">
            <div class="tarjetaAccion tarjetaAccion--azul">
                <div class="tarjetaAccion__encabezado">
                    <img class="tarjetaAccion__icono"
                        src="${pageContext.request.contextPath}/asset/imagenes/Resumen-financiero.png" alt="Resumen">
                    <div class="tarjetaAccion__contenido">
                        <h3 class="tarjetaAccion__titulo">Resumen financiero</h3>
                        <p class="tarjetaAccion__subtitulo">Visualiza ingresos, gastos y el dinero disponible con
                            gráficos sencillos.</p>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/public/modules/06_Finanzas/08_ResumenFinanciero.jsp">
                    <button class="boton boton--editar">Ver resumen</button>
                </a>
            </div>
            <div class="tarjetaAccion tarjetaAccion--verde">
                <div class="tarjetaAccion__encabezado">
                    <img class="tarjetaAccion__icono tarjetaAccion__icono--verde"
                        src="${pageContext.request.contextPath}/asset/imagenes/ingresos-pasivos.png" alt="Ingresos">
                    <div class="tarjetaAccion__contenido">
                        <h3 class="tarjetaAccion__titulo">Registrar ingresos</h3>
                        <p class="tarjetaAccion__subtitulo">Añade el dinero que recibes y mantén actualizado tu balance.
                        </p>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/public/modules/06_Finanzas/04_DetalleIngresos.jsp">
                    <button class="boton boton--agregar">Registrar ingresos</button>
                </a>
            </div>
            <div class="tarjetaAccion tarjetaAccion--rojo">
                <div class="tarjetaAccion__encabezado">
                    <img class="tarjetaAccion__icono tarjetaAccion__icono--rojo"
                        src="${pageContext.request.contextPath}/asset/imagenes/Registrar-egresos.png" alt="Egresos">
                    <div class="tarjetaAccion__contenido">
                        <h3 class="tarjetaAccion__titulo">Registrar egresos</h3>
                        <p class="tarjetaAccion__subtitulo">Registra tus gastos y controla en qué se va tu dinero.</p>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/public/modules/06_Finanzas/07_DetalleEgresos.jsp">
                    <button class="boton boton--Eliminar">Registrar egresos</button>
                </a>
            </div>
            <div class="tarjetaAccion tarjetaAccion--morado">
                <div class="tarjetaAccion__encabezado">
                    <img class="tarjetaAccion__icono tarjetaAccion__icono--morado"
                        src="${pageContext.request.contextPath}/asset/imagenes/Presupuesto-mensual.png"
                        alt="Presupuesto">
                    <div class="tarjetaAccion__contenido">
                        <h3 class="tarjetaAccion__titulo">Presupuesto mensual</h3>
                        <p class="tarjetaAccion__subtitulo">Define un límite de gasto y recibe alertas si te acercas o
                            lo superas.</p>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/public/modules/06_Finanzas/09_PresupuestoMensual.jsp">
                    <button class="boton boton--resumen">Configurar presupuesto</button>
                </a>
            </div>
            <a href="${pageContext.request.contextPath}/public/modules/MenuPrincipal/01_menuPrincipal.jsp"
                class="finanzas__boton">
                <button class="boton boton--volver">Volver</button>
            </a>
        </main>
    </body>

    </html>
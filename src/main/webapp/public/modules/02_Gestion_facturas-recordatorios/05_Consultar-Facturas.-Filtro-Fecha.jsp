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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
        <link rel="stylesheet"
            href="${pageContext.request.contextPath}/asset/css/modules/02_Gestion_facturas-recordatorios/estilosConsultar-Facturas.-Filtro-Fecha.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png"
                alt="Logo de SmartHome Budget">
            <a
                href="${pageContext.request.contextPath}/public/modules/02_Gestion_facturas-recordatorios/01_facturas.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
            <div class="encabezado__contenedorTitulo">
                <h1 class="encabezado__titulo">Consultar Facturas</h1>
            </div>
        </header>
        <main class="consultarFacturas">
            <div class="consultarFacturas__busqueda">
                <input type="search" class="consultarFacturas__busqueda-input" placeholder="🔍 Buscar factura...">
            </div>
            <div class="consultarFacturas__filtros">
                <p class="consultarFacturas__filtro-label">Filtrar por:</p>
                <div class="consultarFacturas__filtro-grupo">
                    <a href="${pageContext.request.contextPath}/public/modules/02_Gestion_facturas-recordatorios/04_Consultar-Facturas.-Filtro-Categoria.jsp"
                        class="consultarFacturas__filtro-enlace">
                        Categoría
                        <img class="consultarFacturas__filtro-imagen"
                            src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="Icono filtros">
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/02_Gestion_facturas-recordatorios/03_Consultar-Facturas.jsp"
                        class="consultarFacturas__filtro-enlace consultarFacturas__filtro-enlace--activo"
                        alt="Icono filtros">
                        Fecha
                        <img class="consultarFacturas__filtro-imagen"
                            src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="Icono filtros">
                    </a>
                    <a href="${pageContext.request.contextPath}/public/modules/02_Gestion_facturas-recordatorios/06_Consultar-Facturas.-Filtro-Estado.jsp"
                        class="consultarFacturas__filtro-enlace" alt="Icono filtros">
                        Estado
                        <img class="consultarFacturas__filtro-imagen"
                            src="${pageContext.request.contextPath}/asset/imagenes/flecha.png" alt="Icono filtros">
                    </a>
                </div>
            </div>
            <div class="facturaLista">
                <div class="facturaCard facturaCard--naranja">
                    <div class="facturaCard__borde"></div>
                    <div class="facturaCard__contenido">
                        <div class="facturaCard__encabezado">
                            <h3 class="facturaCard__titulo">Agua - $75.000</h3>
                        </div>
                        <p class="facturaCard__fecha">Vence: 05 Dic 2025</p>
                        <div class="facturaCard__estadoAcciones">
                            <span class="facturaCard__etiqueta facturaCard__etiqueta--pendiente">Pendiente</span>
                            <div class="facturaCard__acciones">
                                <button class="boton boton--editar">✏️ Editar</button>
                                <button class="boton boton--Eliminar">🗑️ Eliminar</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="facturaCard facturaCard--rojo">
                    <div class="facturaCard__borde"></div>
                    <div class="facturaCard__contenido">
                        <div class="facturaCard__encabezado">
                            <h3 class="facturaCard__titulo">Luz - $82.000</h3>
                        </div>
                        <p class="facturaCard__fecha">Vence: 05 Dic 2025</p>
                        <div class="facturaCard__estadoAcciones">
                            <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">Pagada</span>
                            <div class="facturaCard__acciones">
                                <button class="boton boton--editar">✏️ Editar</button>
                                <button class="boton boton--Eliminar">🗑️ Eliminar</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="facturaCard facturaCard--verde">
                    <div class="facturaCard__borde"></div>
                    <div class="facturaCard__contenido">
                        <div class="facturaCard__encabezado">
                            <h3 class="facturaCard__titulo">Arriendo - $550.000</h3>
                        </div>
                        <p class="facturaCard__fecha">Vence: 30 Nov 2025</p>
                        <div class="facturaCard__estadoAcciones">
                            <span class="facturaCard__etiqueta facturaCard__etiqueta--vencida">Vencida</span>
                            <div class="facturaCard__acciones">
                                <button class="boton boton--editar">✏️ Editar</button>
                                <button class="boton boton--Eliminar">🗑️ Eliminar</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="facturaCard facturaCard--azul">
                    <div class="facturaCard__borde"></div>
                    <div class="facturaCard__contenido">
                        <div class="facturaCard__encabezado">
                            <h3 class="facturaCard__titulo">Gas - $41.500</h3>
                        </div>
                        <p class="facturaCard__fecha">Vence: 15 Oct 2025</p>
                        <div class="facturaCard__estadoAcciones">
                            <span class="facturaCard__etiqueta facturaCard__etiqueta--pagada">Pagada</span>
                            <div class="facturaCard__acciones">
                                <button class="boton boton--editar">✏️ Editar</button>
                                <button class="boton boton--Eliminar">🗑️ Eliminar</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="facturaCard facturaCard--morado">
                    <div class="facturaCard__borde"></div>
                    <div class="facturaCard__contenido">
                        <div class="facturaCard__encabezado">
                            <h3 class="facturaCard__titulo">Internet - $80.000</h3>
                        </div>
                        <p class="facturaCard__fecha">Vence: 25 Ene 2025</p>
                        <div class="facturaCard__estadoAcciones">
                            <span class="facturaCard__etiqueta facturaCard__etiqueta--pendiente">Pendiente</span>
                            <div class="facturaCard__acciones">
                                <button class="boton boton--editar">✏️ Editar</button>
                                <button class="boton boton--Eliminar">🗑️ Eliminar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/public/modules/02_Gestion_facturas-recordatorios/02_formularioRegistrar-facturas.jsp"
                class="consultarFacturas__boton">
                <button class="boton boton--cancelar">+ Nueva factura</button>
            </a>
        </main>
        <div class="panelMesFiltro">
            <div class="filtroMes">
                <h3 class="filtroMes__titulo">Seleccionar mes</h3>
                <div class="filtroMes__gridMeses">
                    <button class="filtroMes__mes">Enero</button>
                    <button class="filtroMes__mes">Febrero</button>
                    <button class="filtroMes__mes">Marzo</button>
                    <button class="filtroMes__mes">Abril</button>
                    <button class="filtroMes__mes">Mayo</button>
                    <button class="filtroMes__mes">Junio</button>
                    <button class="filtroMes__mes">Julio</button>
                    <button class="filtroMes__mes">Agosto</button>
                    <button class="filtroMes__mes">Septiembre</button>
                    <button class="filtroMes__mes">Octubre</button>
                    <button class="filtroMes__mes">Noviembre</button>
                    <button class="filtroMes__mes">Diciembre</button>
                </div>
                <div class="filtroMes__anio">
                    <label class="filtroMes__anio-label">Año</label>
                    <select class="filtroMes__anio-select">
                        <option>2026</option>
                        <option>2025</option>
                        <option>2024</option>
                        <option>2023</option>
                        <option>2022</option>
                        <option>2021</option>
                        <option>2020</option>
                    </select>
                </div>
                <a href="##" class="filtroMes__boton">
                    <button class="boton boton--registrar">Aplicar filtro</button>
                </a>
            </div>
        </div>
    </body>

    </html>
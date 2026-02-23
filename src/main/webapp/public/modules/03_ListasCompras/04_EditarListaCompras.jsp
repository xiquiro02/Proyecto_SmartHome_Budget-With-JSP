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
            href="${pageContext.request.contextPath}/asset/css/modules/03_ListasCompras/estilosEditarListaCompras.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png"
                alt="Logo de SmartHome Budget">
            <a href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/02_CrearLista.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
            <div class="encabezado__contenedorTitulo">
                <h1 class="encabezado__titulo">Confirmar creación </h1>
            </div>
        </header>
        <main class="edicion">
            <div class="edicion__contenedor">
                <div class="edicion__campo">
                    <label class="edicion__etiqueta" for="nombre">Nombre de la lista:</label>
                    <input type="text" id="nombre" class="edicion__input" value="Mercado mensual">
                </div>

                <div class="edicion__campo">
                    <label class="edicion__etiqueta" for="categoria">Categoría:</label>
                    <select id="categoria" class="edicion__select">
                        <option value="alimentos" selected>Alimentos</option>
                        <option value="aseo">Aseo</option>
                        <option value="personal">Personal</option>
                        <option value="otros">Otros</option>
                    </select>
                </div>

                <div class="edicion__campo">
                    <label class="edicion__etiqueta">Fecha de creación:</label>
                    <input type="text" class="edicion__input" value="10 Feb 2025" readonly>
                </div>

                <div class="edicion__campo">
                    <label class="edicion__etiqueta" for="estado">Estado:</label>
                    <select id="estado" class="edicion__select edicion__select--completa">
                        <option value="completa" selected>Completa</option>
                        <option value="proceso">En proceso</option>
                        <option value="pendiente">Pendiente</option>
                    </select>
                </div>

                <div class="edicion__productos">
                    <h3 class="edicion__subtitulo">Productos en esta lista:</h3>

                    <div class="producto">
                        <div class="producto__info">
                            <span class="producto__nombre">Leche</span>
                            <span class="producto__cantidad">Cantidad: 2</span>
                        </div>
                        <div class="producto__botones">
                            <a
                                href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/10_EditarProducto.jsp"><button
                                    class="producto__boton producto__boton--editar">✏️
                                    Editar</button></a>
                            <a
                                href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/12_EliminarProducto.jsp"><button
                                    class="producto__boton producto__boton--eliminar">🗑️ Eliminar</button></a>
                        </div>
                    </div>

                    <div class="producto">
                        <div class="producto__info">
                            <span class="producto__nombre">Pan</span>
                            <span class="producto__cantidad">Cantidad: 1</span>
                        </div>
                        <div class="producto__botones">
                            <a
                                href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/10_EditarProducto.jsp"><button
                                    class="producto__boton producto__boton--editar">✏️
                                    Editar</button></a>
                            <a
                                href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/12_EliminarProducto.jsp"><button
                                    class="producto__boton producto__boton--eliminar">🗑️ Eliminar</button></a>
                        </div>
                    </div>

                    <div class="producto">
                        <div class="producto__info">
                            <span class="producto__nombre">Manzanas</span>
                            <span class="producto__cantidad">Cantidad: 5</span>
                        </div>
                        <div class="producto__botones">
                            <a
                                href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/10_EditarProducto.jsp"><button
                                    class="producto__boton producto__boton--editar">✏️
                                    Editar</button></a>
                            <a
                                href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/12_EliminarProducto.jsp"><button
                                    class="producto__boton producto__boton--eliminar">🗑️ Eliminar</button></a>
                        </div>
                    </div>

                    <div class="producto">
                        <div class="producto__info">
                            <span class="producto__nombre">Arroz blanco</span>
                            <span class="producto__cantidad">Cantidad: 4</span>
                        </div>
                        <div class="producto__botones">
                            <a
                                href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/10_EditarProducto.jsp"><button
                                    class="producto__boton producto__boton--editar">✏️
                                    Editar</button></a>
                            <a
                                href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/12_EliminarProducto.jsp"><button
                                    class="producto__boton producto__boton--eliminar">🗑️ Eliminar</button></a>
                        </div>
                    </div>

                    <a
                        href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/08_AgregarProductos.jsp"><button
                            class="boton boton--agregar">➕ Agregar
                            producto</button></a>
                </div>

                <a href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/05_ConfirmacionEdicionLista.jsp"
                    class="edicion__botones">
                    <button type="button" class="boton boton--registrar ">Guardar cambios</button>
                </a>
                <a href="${pageContext.request.contextPath}/public/modules/03_ListasCompras/01_listasCompras.jsp"
                    class="edicion__botones">
                    <button type="button" class="boton boton--cancelar">Cancelar</button>
                </a>
            </div>
        </main>
    </body>

    </html>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Link iconos  -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
    <!-- Link Fuentes -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <!-- Link estilos.css  -->
    <link rel="stylesheet" href="../../../asset/utils/styles.css">
    <link rel="stylesheet" href="../../../asset/modules/06_Finanzas/estilosPresupuestoMensual.css">

    <title>SmartHome Budget</title>
</head>
<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="../../../asset/imagenes/Logo redondo.png" alt="Logo de SmartHome Budget">
        <a href="01_Finanzas.html">
            <span class="material-symbols-outlined"> arrow_back_ios_new </span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Presupuesto Mensual</h1>
        </div>
    </header>
    
    <main class="presupuestoMensual">
        <div class="presupuestoMensual__introduccion">
            <img class="introduccion__icono" src="../../../asset/imagenes/PresupuestoMES.png" alt="Logo de SmartHome Budget">
            <div class="introduccion__contenido">
                <h2 class="introduccion__titulo">Presupuesto Mensual</h2>
                <p class="introduccion__texto">
                    Define cuánto deseas gastar al mes para mantener el control de tu dinero.
                </p>
            </div>
        </div>
        <div class="formularioPresupuesto">
            <div class="formularioPresupuesto__campo">
                <label class="formularioPresupuesto__label" for="presupuesto"> Presupuesto máximo del mes </label>
                <input type="text" id="presupuesto" class="formularioPresupuesto__input" placeholder="$ 0.00">
            </div>
            <div class="formularioPresupuesto__campo">
                <label class="formularioPresupuesto__label" for="mes">
                    Mes a aplicar
                </label>
                <select id="mes" class="formularioPresupuesto__select">
                    <option value="">Seleccionar mes</option>
                    <option value="enero">Enero</option>
                    <option value="febrero">Febrero</option>
                    <option value="marzo">Marzo</option>
                    <option value="abril">Abril</option>
                    <option value="mayo">Mayo</option>
                    <option value="junio">Junio</option>
                    <option value="julio">Julio</option>
                    <option value="agosto">Agosto</option>
                    <option value="septiembre">Septiembre</option>
                    <option value="octubre">Octubre</option>
                    <option value="noviembre">Noviembre</option>
                    <option value="diciembre">Diciembre</option>
                </select>
            </div>
            <div class="formularioPresupuesto__campo">
                <label class="formularioPresupuesto__label">
                    Alertas de presupuesto
                </label>
                <div class="formularioPresupuesto__opciones">
                    <label class="checkboxOpcion">
                        <input type="checkbox" class="checkboxOpcion__input" checked>
                        <span class="checkboxOpcion__marca">✓</span>
                        <span class="checkboxOpcion__texto">Avisarme cuando llegue al 80%</span>
                    </label>
                    <label class="checkboxOpcion">
                        <input type="checkbox" class="checkboxOpcion__input" checked>
                        <span class="checkboxOpcion__marca">✓</span>
                        <span class="checkboxOpcion__texto">Avisarme cuando supere el presupuesto</span>
                    </label>
                </div>
            </div>
        </div>
        <div class="vistaPrevia">
            <div class="vistaPrevia__encabezado">
                <span class="vistaPrevia__icono">📄</span>
                <h3 class="vistaPrevia__titulo">Vista previa</h3>
            </div>
            <div class="vistaPrevia__contenido">
                <div class="vistaPrevia__item">
                    <span class="vistaPrevia__label">Presupuesto:</span>
                    <span class="vistaPrevia__valor">$ 1.000.000</span>
                </div>
                <div class="vistaPrevia__item">
                    <span class="vistaPrevia__label">Gastos actuales:</span>
                    <span class="vistaPrevia__valor">$ 650.000</span>
                </div>
                <div class="vistaPrevia__item">
                    <span class="vistaPrevia__label">Disponible:</span>
                    <span class="vistaPrevia__valor vistaPrevia__valor--verde">$ 350.000</span>
                </div>
            </div>
        </div>
        <a href="01_Finanzas.html" class="presupuestoMensual__boton">
            <button class="boton boton--registrar">Guardar presupuesto</button>
        </a>
        <a href="01_Finanzas.html" class="presupuestoMensual__boton">
            <button class="boton boton--cancelar">Cancelar</button>
        </a>
    </main>

</body>
</html>
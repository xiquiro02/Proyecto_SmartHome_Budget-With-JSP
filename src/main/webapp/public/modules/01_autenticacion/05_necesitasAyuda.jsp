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
            href="${pageContext.request.contextPath}/asset/css/modules/01_autenticacion/estilosnecesitasAyuda.css">

        <title>SmartHome Budget</title>
    </head>

    <body>
        <header class="encabezado">
            <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo-redondo.png"
                alt="Logo de SmartHome Budget">
            <a href="${pageContext.request.contextPath}/public/modules/01_autenticacion/01_principal.jsp">
                <span class="material-symbols-outlined"> arrow_back_ios_new </span>
            </a>
            <div class="encabezado__contenedorTitulo">
                <h1 class="encabezado__titulo">¿Necesitas Ayuda?</h1>
            </div>
        </header>
        <main class="contenido">
            <h2 class="contenido__titulo">Preguntas Frecuentes</h2>
            <div class="contenido__lista-preguntas">
                <!-- Pregunta 1 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/agregar-usuario.png" alt="Configurar cuenta">
                        <h3 class="acordeon-title">¿Cómo configuro mi cuenta?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Para configurar tu cuenta en SmartHome Budget, sigue estos pasos simples:</p>
                            <ol>
                                <li>Abre la aplicación y selecciona la opción "Registrarse".</li>
                                <li>Ingresa tus datos personales: nombre, apellidos, correo electrónico, número de teléfono y una contraseña (debe tener al menos 8 caracteres).</li>
                                <li>El sistema validará que el correo no esté registrado previamente y enviará un código de verificación a tu correo electrónico.</li>
                                <li>Ingresa el código para activar tu cuenta.</li>
                                <li>Una vez activada, podrás iniciar sesión y personalizar tu perfil.</li>
                            </ol>
                            <p>Recuerda que el registro es obligatorio para acceder a todas las funciones de la app.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 2 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/Eliminar-cuenta.png" alt="Eliminar cuenta">
                        <h3 class="acordeon-title">¿Cómo elimino mi cuenta?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Si deseas eliminar tu cuenta de forma permanente, sigue estos pasos:</p>
                            <ol>
                                <li>Inicia sesión en la aplicación.</li>
                                <li>Ve a la sección de "Ajustes".</li>
                                <li>Selecciona la opción "Eliminar cuenta".</li>
                                <li>El sistema te pedirá una confirmación adicional para proceder.</li>
                                <li>Confirma tu identidad ingresando tu contraseña actual.</li>
                                <li>Una vez confirmado, toda tu información será eliminada permanentemente y no podrá revertirse.</li>
                            </ol>
                            <p><strong>Nota:</strong> Esta acción es irreversible, así que asegúrate de respaldar cualquier dato importante antes de proceder.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 3 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/recuperar-contrasena.png" alt="Recuperar contraseña">
                        <h3 class="acordeon-title">¿Cómo recupero mi contraseña?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Si olvidaste tu contraseña, puedes recuperarla fácilmente a través de una opción: recuperación por correo electrónico. Sigue los pasos a continuación:</p>
                            <ul>
                                <li><strong>Opción: Recuperación por correo electrónico</strong></li>
                            </ul>
                            <ol>
                                <li>En la pantalla de inicio de sesión, selecciona "Olvidé mi contraseña".</li>
                                <li>Elige la opción "Recuperar por email".</li>
                                <li>Aparecerá un mensaje pidiendo que ingreses tu correo registrado.</li>
                                <li>Ingresa tu correo electrónico registrado en el recuadro proporcionado.</li>
                                <li>Haz clic en el botón "Email". Si deseas cancelar, selecciona "Cancelar".</li>
                                <li>Recibirás un mensaje de confirmación indicando que el email se envió exitosamente. Haz clic en el botón "Ir al correo" para abrir tu aplicación de email y buscar el enlace.</li>
                                <li>Haz clic en el enlace del email (tiene un tiempo de expiración limitado).</li>
                                <li>Crea una nueva contraseña segura (mínimo 8 caracteres) y confirma el cambio.</li>
                                <li>Inicia sesión con tu nueva contraseña.</li>
                            </ol>
                            <p><strong>Nota:</strong> Solo se enviará la recuperación al correo registrado previamente para verificar tu identidad.</p>
                            <p>Recuerda que el enlace tiene un tiempo de expiración limitado, y solo se enviará a tu correo registrado para verificar tu identidad. Si encuentras problemas, intenta reenviar el código o contacta soporte.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 4 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/metodo-de-pago.png" alt="Pago">
                        <h3 class="acordeon-title">¿Cómo agrego mi primer recordatorio de pago?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Para agregar un recordatorio de pago de facturas, sigue estos pasos:</p>
                            <ol>
                                <li>Inicia sesión y ve al módulo "Facturas y Pagos".</li>
                                <li>Selecciona "Registrar factura".</li>
                                <li>Ingresa los detalles: nombre de la factura (ej. "Luz", "Agua"), categoría, monto a pagar, fecha de vencimiento y estado (pendiente, pagada o vencida).</li>
                                <li>Guarda el registro. El sistema lo almacenará y activará notificaciones automáticas.</li>
                                <li>Puedes agregar varias facturas; solo se permiten fechas de vencimiento válidas y futuras.</li>
                            </ol>
                            <p>Esto te ayudará a evitar olvidos en pagos importantes.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 5 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/edito-o-elimino-recordatorios.png" alt="Editar o eliminar recordatorios">
                        <h3 class="acordeon-title">¿Cómo edito o elimino un recordatorio?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Para modificar o eliminar un recordatorio de pago, sigue estos pasos:</p>
                            <ul>
                                <li><strong>Editar un recordatorio:</strong></li>
                            </ul>
                            <ol>
                                <li>Ve al módulo "Facturas y Pagos".</li>
                                <li>Selecciona "Consultar Facturas".</li>
                                <li>Selecciona la factura que deseas editar de la lista.</li>
                                <li>Haz clic en "Editar" y modifica los datos (nombre, monto, fecha de vencimiento).</li>
                                <li>Valida los cambios y guarda. Las notificaciones asociadas se actualizarán automáticamente.</li>
                            </ol>
                            <ul>
                                <li><strong>Eliminar un recordatorio:</strong></li>
                            </ul>
                            <ol>
                                <li>Ve al módulo "Facturas y Pagos".</li>
                                <li>Selecciona "Consultar Facturas".</li>
                                <li>Selecciona la factura en la lista.</li>
                                <li>Haz clic en "Eliminar".</li>
                                <li>Confirma la acción (solo se puede eliminar una factura a la vez).</li>
                                <li>La eliminación es permanente y no se puede revertir.</li>
                            </ol>
                            <p>Solo tú, como propietario, puedes modificar o eliminar tus recordatorios.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 6 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/registro-producto.png" alt="Registro de productos">
                        <h3 class="acordeon-title">¿Cómo registro mi primer producto de despensa?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Para registrar productos en tu despensa (inventario de casa), sigue estos pasos:</p>
                            <ol>
                                <li>Inicia sesión y ve al módulo "Mi Inventario".</li>
                                <li>Selecciona "Agregar producto".</li>
                                <li>Ingresa el nombre del producto, cantidad disponible y categoría (aseo, alimentos u otros).</li>
                                <li>Guarda el registro. Puedes agregar múltiples productos y clasificarlos por categorías.</li>
                                <li>No se permiten registros sin nombre ni cantidad.</li>
                            </ol>
                            <p>Esto te ayudará a llevar un control de lo que tienes en casa y recibir alertas cuando algo esté por agotarse.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 7 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/Recordatorios-automaticos.png" alt="Recordatorios automáticos">
                        <h3 class="acordeon-title">¿Cómo funcionan los recordatorios automáticos?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Los recordatorios automáticos están diseñados para notificarte de pagos pendientes, productos por agotarse y alertas financieras. Estos te ayudan a mantener tu hogar organizado, evitar gastos innecesarios y controlar tu presupuesto. A continuación, te explicamos cómo funcionan en cada área:</p>
                            
                            <h3>Para pagos de facturas:</h3>
                            <ul>
                                <li>El sistema envía notificaciones push automáticas para recordar pagos próximos por vencer o vencidos.</li>
                                <li>Se activan 2 días antes, 1 día antes y el día del vencimiento de la factura.</li>
                                <li>Puedes activar o desactivar estas alertas en la configuración de la app.</li>
                                <li>Requiere conexión a internet para las notificaciones push. Si no las activas, puedes revisar manualmente en el módulo "Ajustes" o "Recordatorios".</li>
                            </ul>
                            
                            <h3>Para productos en el inventario:</h3>
                            <ul>
                                <li>Cuando la cantidad de un producto en tu inventario llegue a un nivel bajo (definido por ti o por defecto), recibirás una notificación push.</li>
                                <li>Los productos con baja cantidad se sugerirán automáticamente en tu lista de compras.</li>
                                <li>Si la cantidad llega a cero, el producto se moverá automáticamente a la lista de compras (puedes seleccionar en qué lista)</li>
                                <li>Puedes activar o desactivar estas alertas en configuración.</li>
                                <li>Requiere conexión a internet para las notificaciones push. Si no las activas, puedes revisar manualmente en el módulo "Ajustes" o "Recordatorios".</li>
                            </ul>
                            
                            <h3>Para finanzas y presupuesto:</h3>
                            <ul>
                                <li>El sistema envía alertas automáticas cuando superas el presupuesto mensual configurado o cuando registres un gasto inusualmente alto.</li>
                                <li>Las alertas se envían en tiempo real y pueden activarse o desactivarse desde la configuración.</li>
                                <li>Te ayudan a controlar tus ingresos, egresos y balance disponible, evitando derroches.</li>
                                <li>Requiere conexión a internet para las notificaciones push. Si no las activas, puedes revisar manualmente en el módulo "Ajustes" o "Recordatorios".</li>
                            </ul>
                            
                            <p>Estos recordatorios son personalizables y se generan de forma automática para adaptarse a tus necesidades. Si tienes problemas con las notificaciones, verifica tu configuración y conexión a internet.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 8 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/factura-como-pagada.png" alt="Factura pagada">
                        <h3 class="acordeon-title">¿Puedo marcar una factura como "pagada"?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Sí, puedes marcar una factura como "pagada" para actualizar su estado y registrar el historial:</p>
                            <ol>
                                <li>Ve al módulo "Facturas y Pagos".</li>
                                <li>Selecciona "Consultar Facturas".</li>
                                <li>Selecciona la factura pendiente de la lista.</li>
                                <li>Haz clic en "Editar" y edita el estado a "Pagada".</li>
                                <li>Ingresa la fecha de pago real y confirma.</li>
                                <li>El sistema la moverá al historial de pagos, incluyendo fecha y monto, para análisis financiero.</li>
                            </ol>
                            <p>Solo se guardarán pagos confirmados como realizados.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 9 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/producto-agotado.png" alt="Producto agotado">
                        <h3 class="acordeon-title">¿Cómo sabe la aplicación que un producto está por agotarse?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>La aplicación monitorea tu inventario y te alerta cuando un producto está próximo a agotarse:</p>
                            <ul>
                                <li>Define una cantidad mínima para cada producto (por defecto o personalizada) al registrarlo.</li>
                                <li>Cuando la cantidad disponible llegue a ese nivel bajo, recibirás una notificación push.</li>
                                <li>Los productos con baja cantidad se sugerirán automáticamente en tu lista de compras, y si llega a cero, se moverán a la lista de compras.</li>
                                <li>Puedes activar o desactivar estas alertas en configuración. Requiere conexión a internet para notificaciones.</li>
                            </ul>
                            <p>Esto te ayuda a reponer productos a tiempo y evitar interrupciones en tu hogar.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 10 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/signo-de-interrogacion.png" alt="Soporte">
                        <h3 class="acordeon-title">¿No encontraste tu respuesta?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Si ninguna de las respuestas anteriores resolvió tu duda, estamos aquí para ayudarte. Envía un mensaje a nuestro equipo de soporte a través de:</p>
                            <ul>
                                <li>Correo electrónico: "ximenaquiro02@gmail.com" o "soporte@smarthomebudget.com"</li>
                                <li>Dentro de la app: Ve a "Ajustes" > "Centro de ayuda y soporte" y describe tu problema.</li>
                                <li>Incluye detalles como tu dispositivo y una captura de pantalla si es posible. Responderemos lo antes posible para mejorar tu experiencia.</li>
                            </ul>
                            <p>¡Gracias por usar SmartHome Budget! Si tienes más preguntas, revisa esta sección o actualiza la app para nuevas funciones.</p>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        
        <script src="${pageContext.request.contextPath}/asset/js/acordeonAyuda.js"></script>

    </body>

    </html>
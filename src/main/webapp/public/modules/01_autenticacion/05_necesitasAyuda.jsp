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
                            <p>Para crear una cuenta en SmartHome Budget y empezar a usar la aplicación, sigue estos pasos:</p>

                            <ol>
                                <li>Abre la aplicación y haz clic en la opción <strong>"Registrarse"</strong>.</li>
                                <li>Completa el formulario con tu información personal, como tu 
                                <strong>número de documento, nombre, apellidos, correo electrónico, número de teléfono y una contraseña</strong>. 
                                La contraseña debe tener al menos 8 caracteres para que tu cuenta sea segura.</li>
                                <li>El sistema revisará que tu <strong>documento y tu correo electrónico no estén registrados previamente</strong>. 
                                Esto se hace para evitar que una misma persona cree varias cuentas.</li>
                                <li>Si eres la primera persona en crear la cuenta del hogar, la aplicación te permitirá 
                                <strong>crear un nuevo hogar</strong>. Esto significa que podrás administrar la información y organizar a los miembros de tu familia dentro del sistema.</li>
                                <li>Una vez creado el hogar, podrás agregar a otros miembros de tu familia utilizando un 
                                <strong>código de invitación</strong>.</li>
                                <li>Para invitar a alguien, entra a la sección <strong>"Invitar miembro"</strong> dentro de la aplicación.</li>
                                <li>Selecciona el tipo de acceso que tendrá la persona que deseas invitar:</li>
                                <ul>
                                    <li><strong>Cotitular:</strong> Puede ayudar a gestionar el inventario, las listas de compras y ver el resumen financiero del hogar.</li>
                                    <li><strong>Invitado (Hijo):</strong> Tiene acceso limitado. Puede ver el inventario y agregar productos a las listas de compras.</li>
                                </ul>
                                <li>Después de elegir el tipo de miembro, haz clic en <strong>"Generar código"</strong>.</li>
                                <li>La aplicación generará un <strong>código de invitación único</strong>. Después deberás escribir el <strong>correo electrónico de la persona que deseas invitar</strong>, 
                                y el sistema enviará el código de invitación a ese correo.</li>
                                <li>La persona invitada recibirá un mensaje con el código y una indicación de que este 
                                <strong>es válido por 7 días</strong>.</li>
                                <li>Para unirse al hogar, la persona invitada deberá registrarse en la aplicación e ingresar el 
                                <strong>código de invitación</strong> recibido en su correo.</li>
                                <li>Una vez terminado el registro, todos los miembros podrán iniciar sesión y comenzar a utilizar las funciones de SmartHome Budget según el tipo de acceso que tengan.</li>
                            </ol>

                            <p>Crear una cuenta es necesario para poder usar la aplicación y formar parte de un hogar dentro de SmartHome Budget.</p>
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
                            <p>Si deseas eliminar tu cuenta de SmartHome Budget, sigue estos pasos:</p>

                            <ol>
                                <li>Inicia sesión en la aplicación con tu correo y contraseña.</li>
                                <li>Dirígete a la sección <strong>"Ajustes"</strong> o configuración de tu cuenta.</li>
                                <li>Selecciona la opción <strong>"Eliminar cuenta"</strong>.</li>
                                <li>El sistema mostrará un mensaje de confirmación para asegurarse de que realmente deseas eliminar tu cuenta.</li>
                                <li>Para continuar, deberás ingresar nuevamente tu <strong>contraseña actual</strong> como medida de seguridad.</li>
                                <li>Si tu cuenta pertenece a un hogar como <strong>administrador</strong>, primero deberás asignar la administración a otro miembro o eliminar el hogar antes de poder eliminar tu cuenta.</li>
                                <li>Una vez cumplidas estas condiciones y confirmada la acción, tu cuenta será eliminada del sistema y ya no podrás iniciar sesión.</li>
                            </ol>

                            <p><strong>Importante:</strong> Al eliminar tu cuenta, perderás el acceso a la información del hogar, incluyendo inventario, listas de compras y registros financieros.</p>

                            <p><strong>Nota:</strong> Esta acción es permanente y no se puede deshacer.</p>
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
                            <p>Si olvidaste tu contraseña, puedes recuperarla fácilmente utilizando la opción de recuperación por correo electrónico. Sigue estos pasos:</p>

                            <ol>
                                <li>En la pantalla de inicio de sesión, haz clic en la opción <strong>"Olvidé mi contraseña"</strong>.</li>
                                <li>El sistema te pedirá ingresar el <strong>correo electrónico</strong> que registraste al crear tu cuenta.</li>
                                <li>Escribe tu correo en el campo correspondiente y presiona el botón <strong>"Enviar"</strong>.</li>
                                <li>Recibirás un correo electrónico con un <strong>enlace para restablecer tu contraseña</strong>.</li>
                                <li>Abre tu correo y haz clic en el enlace recibido. Este enlace tiene un <strong>tiempo de validez limitado</strong> por seguridad.</li>
                                <li>Se abrirá una página donde podrás crear una <strong>nueva contraseña</strong>. Esta debe tener al menos 8 caracteres.</li>
                                <li>Confirma la nueva contraseña y guarda los cambios.</li>
                                <li>Una vez completado el proceso, podrás iniciar sesión nuevamente utilizando tu nueva contraseña.</li>
                            </ol>

                            <p><strong>Importante:</strong> El enlace de recuperación solo se enviará al correo electrónico que esté registrado en tu cuenta.</p>

                            <p><strong>Nota:</strong> Si el enlace expira o no encuentras el correo, puedes solicitar nuevamente la recuperación desde la pantalla de inicio de sesión.</p>
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
                            <p>Si deseas agregar un recordatorio para el pago de una factura, sigue estos pasos:</p>

                            <ol>
                                <li>Inicia sesión en la aplicación.</li>
                                <li>Dirígete a la sección <strong>"Facturas y Pagos"</strong>.</li>
                                <li>Haz clic en la opción <strong>"Registrar factura"</strong> para agregar una nueva.</li>
                                <li>Completa la información solicitada, como el <strong>nombre de la factura</strong> (por ejemplo: luz, agua o internet), la <strong>categoría</strong>, el <strong>monto a pagar</strong> y la <strong>fecha de vencimiento</strong>.</li>
                                <li>Selecciona el <strong>estado de la factura</strong>, que puede ser pendiente, pagada o vencida.</li>
                                <li>Guarda la información para registrar la factura en el sistema.</li>
                                <li>Una vez guardada, la aplicación podrá mostrar recordatorios para ayudarte a tener presente la fecha de pago.</li>
                            </ol>

                            <p>Esto te ayudará a organizar mejor tus pagos y evitar olvidar facturas importantes.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 5 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/registro-producto.png" alt="Registro de productos">
                        <h3 class="acordeon-title">¿Cómo registro mi primer producto de despensa?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Si deseas agregar productos a tu inventario del hogar, sigue estos pasos:</p>

                            <ol>
                                <li>Inicia sesión en la aplicación.</li>
                                <li>Dirígete a la sección <strong>"Mi Inventario"</strong>.</li>
                                <li>Haz clic en la opción <strong>"Agregar producto"</strong>.</li>
                                <li>Ingresa la información del producto, como el <strong>nombre</strong>, la <strong>cantidad disponible</strong> y la <strong>categoría</strong> a la que pertenece (por ejemplo: alimentos, aseo u otros).</li>
                                <li>Guarda la información para agregar el producto a tu inventario.</li>
                                <li>Puedes agregar varios productos y organizarlos por categorías para tener un mejor control.</li>
                                <li>Recuerda que no es posible guardar un producto si no tiene nombre o cantidad.</li>
                            </ol>

                            <p>Esto te ayudará a llevar un control de los productos que tienes en casa y a saber cuándo alguno está por agotarse.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 6 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/Recordatorios-automaticos.png" alt="Recordatorios automáticos">
                        <h3 class="acordeon-title">¿Cómo funcionan los recordatorios automáticos?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Los recordatorios automáticos están diseñados para ayudarte a mantener tu hogar organizado. La aplicación puede enviarte avisos sobre pagos de facturas, productos que están por agotarse y alertas relacionadas con tus gastos. A continuación te explicamos cómo funcionan:</p>

                            <h3>Recordatorios para pagos de facturas:</h3>
                            <ul>
                                <li>La aplicación puede enviarte notificaciones para recordarte cuando una factura está próxima a vencer.</li>
                                <li>Los avisos pueden aparecer <strong>2 días antes, 1 día antes y el mismo día del vencimiento</strong>.</li>
                                <li>De esta manera podrás pagar tus facturas a tiempo y evitar retrasos.</li>
                                <li>Puedes activar o desactivar estos recordatorios desde la sección de <strong>Configuración</strong> de la aplicación.</li>
                            </ul>

                            <h3>Recordatorios para productos en el inventario:</h3>
                            <ul>
                                <li>Si la cantidad de un producto en tu inventario es baja, la aplicación puede mostrarte una alerta.</li>
                                <li>Los productos que estén por acabarse pueden sugerirse automáticamente en tu <strong>lista de compras</strong>.</li>
                                <li>Si un producto se agota, podrás agregarlo fácilmente a tu lista de compras para recordarlo cuando vayas a comprar.</li>
                                <li>Estas alertas también pueden activarse o desactivarse desde la configuración.</li>
                            </ul>

                            <h3>Alertas sobre finanzas y presupuesto:</h3>
                            <ul>
                                <li>La aplicación puede avisarte cuando tus gastos se acerquen o superen el <strong>presupuesto mensual</strong> que hayas establecido.</li>
                                <li>También puede mostrar alertas cuando registres un gasto muy alto.</li>
                                <li>Esto te ayuda a tener un mejor control de tus ingresos, gastos y del dinero disponible.</li>
                                <li>Puedes activar o desactivar estas alertas desde la configuración.</li>
                            </ul>

                            <p>Todos estos recordatorios se generan automáticamente para ayudarte a organizar mejor tu hogar. Si no recibes las notificaciones, revisa la configuración de la aplicación o tu conexión a internet.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 7 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/factura-como-pagada.png" alt="Factura pagada">
                        <h3 class="acordeon-title">¿Puedo marcar una factura como "pagada"?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Sí, puedes marcar una factura como <strong>"Pagada"</strong> para actualizar su estado y registrar el pago dentro de la aplicación. Existen dos formas de hacerlo:</p>

                            <h3>Opción 1: Marcar la factura como pagada rápidamente</h3>
                            <ol>
                                <li>Inicia sesión y dirígete a la sección <strong>"Facturas y Pagos"</strong>.</li>
                                <li>Selecciona la opción <strong>"Consultar facturas"</strong>.</li>
                                <li>Busca la factura que esté en estado <strong>Pendiente</strong> o <strong>Vencida</strong>.</li>
                                <li>Haz clic en el botón <strong>"Pagada"</strong>.</li>
                                <li>El sistema actualizará el estado de la factura y registrará el pago.</li>
                            </ol>

                            <h3>Opción 2: Marcar la factura como pagada desde la edición</h3>
                            <ol>
                                <li>Ve a la sección <strong>"Facturas y Pagos"</strong>.</li>
                                <li>Selecciona <strong>"Consultar facturas"</strong>.</li>
                                <li>Elige la factura que deseas actualizar.</li>
                                <li>Haz clic en <strong>"Editar"</strong>.</li>
                                <li>Cambia el estado de la factura a <strong>"Pagada"</strong> e ingresa la fecha en que realizaste el pago.</li>
                                <li>Guarda los cambios para actualizar la información.</li>
                            </ol>

                            <p>Una vez marcada como pagada, la factura quedará registrada en el historial de pagos, lo que te permitirá llevar un mejor control de tus gastos.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 8 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/producto-agotado.png" alt="Producto agotado">
                        <h3 class="acordeon-title">¿Cómo sabe la aplicación que un producto está por agotarse?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>La aplicación puede ayudarte a saber cuándo un producto de tu inventario está por terminarse. Así podrás reponerlo a tiempo y evitar quedarte sin lo que necesitas en casa.</p>

                            <ul>
                                <li>Cuando agregas un producto a tu inventario, el sistema establece una <strong>cantidad mínima</strong> para ayudarte a controlar cuándo el producto está por agotarse.</li>
                                <li>Si la cantidad disponible de un producto llega a ese nivel bajo, la aplicación mostrará una <strong>alerta o recordatorio</strong>.</li>
                                <li>Los productos que estén por acabarse pueden sugerirse automáticamente en tu <strong>lista de compras</strong>, para que recuerdes comprarlos.</li>
                                <li>Si la cantidad de un producto llega a <strong>cero</strong>, podrás agregarlo fácilmente a tu lista de compras para no olvidarlo.</li>
                                <li>Puedes activar o desactivar estas alertas desde la sección de <strong>Configuración</strong> de la aplicación.</li>
                            </ul>

                            <p>Estas alertas te ayudan a mantener tu hogar organizado y asegurarte de tener siempre disponibles los productos que necesitas.</p>
                        </div>
                    </div>
                </div>

                <!-- Pregunta 9 -->
                <div class="acordeon-item">
                    <div class="acordeon-header">
                        <img class="acordeon-icon" src="${pageContext.request.contextPath}/asset/imagenes/signo-de-interrogacion.png" alt="Soporte">
                        <h3 class="acordeon-title">¿No encontraste tu respuesta?</h3>
                        <span class="acordeon-toggle">▼</span>
                    </div>
                    <div class="acordeon-content">
                        <div class="acordeon-body">
                            <p>Si ninguna de las respuestas anteriores resolvió tu duda, puedes comunicarte con nuestro equipo de soporte para recibir ayuda.</p>

                            <p>Puedes contactarnos a través del siguiente medio:</p>

                            <ul>
                                <li><strong>Correo electrónico:</strong> ximenaquiro02@gmail.com o soporte@smarthomebudget.com</li>
                                <li>Para brindarte una mejor atención, incluye información como el <strong>dispositivo que estás utilizando</strong> y, si es posible, una <strong>captura de pantalla del problema</strong> que estás presentando.</li>
                            </ul>

                            <p>Nuestro equipo revisará tu solicitud y te responderá lo antes posible.</p>

                            <p><strong>¡Gracias por usar SmartHome Budget!</strong> Tus comentarios y consultas nos ayudan a mejorar la aplicación y ofrecer una mejor experiencia a todos los usuarios.</p>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        
        <script src="${pageContext.request.contextPath}/asset/js/acordeonAyuda.js"></script>

    </body>

    </html>
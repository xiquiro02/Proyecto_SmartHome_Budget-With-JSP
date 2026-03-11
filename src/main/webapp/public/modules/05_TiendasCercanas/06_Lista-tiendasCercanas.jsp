<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet"
        href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,400,0,0&icon_names=arrow_back_ios_new" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/asset/css/modules/05_TiendasCercanas/estilosLista-tiendasCercanas.css">

    <!-- Leaflet CSS -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
          integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY=" crossorigin=""/>

    <title>SmartHome Budget - Tiendas Cercanas</title>

    <style>
        #mapa-tiendas {
            width: 100%;
            height: 320px;
            border-radius: 12px;
            margin: 16px 0;
            border: 2px solid #ddd;
        }
        .tiendas-vacias {
            text-align: center;
            padding: 40px 20px;
            color: #888;
        }
    </style>
</head>

<%
    // Verificar sesión
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
        return;
    }

    @SuppressWarnings("unchecked")
    List<Object[]> tiendas = (List<Object[]>) request.getAttribute("tiendas");
    Object[] ubicacionUsuario = (Object[]) request.getAttribute("ubicacionUsuario");

    // Coordenadas para el mapa
    // Por defecto: Bucaramanga, Colombia (zona de San Gil es cercana)
    double mapLat = 7.1193;
    double mapLng = -73.1227;
    String ciudadUsuario = "Tu ubicación";

    if (ubicacionUsuario != null) {
        ciudadUsuario = (String) ubicacionUsuario[1] + ", " + (String) ubicacionUsuario[0];
        double lat = (Double) ubicacionUsuario[3];
        double lng = (Double) ubicacionUsuario[4];
        if (lat != 0 && lng != 0) {
            mapLat = lat;
            mapLng = lng;
        }
    }
%>

<body>
    <header class="encabezado">
        <img class="encabezado__imagen" src="${pageContext.request.contextPath}/asset/imagenes/Logo redondo.png"
            alt="Logo de SmartHome Budget">
        <a href="${pageContext.request.contextPath}/Tiendas">
            <span class="material-symbols-outlined"> arrow_back_ios_new </span>
        </a>
        <div class="encabezado__contenedorTitulo">
            <h1 class="encabezado__titulo">Tiendas cercanas</h1>
        </div>
    </header>

    <main class="tiendasCercanas">

        <!-- Ubicación actual -->
        <div class="tiendasCercanas__ubicacion">
            <div class="ubicacion">
                <span class="ubicacion__icono">📍</span>
                <span class="ubicacion__texto">Ubicación: <%= ciudadUsuario %></span>
            </div>
            <a href="${pageContext.request.contextPath}/Tiendas?accion=ubicacionManual">
                <button class="ubicacion__boton">🔁 Actualizar ubicación</button>
            </a>
        </div>

        <!-- MAPA LEAFLET DINÁMICO -->
        <div id="mapa-tiendas"></div>

        <!-- Lista de tarjetas de tiendas -->
        <div class="tiendaLista">
        <% if (tiendas != null && !tiendas.isEmpty()) {
               String[] colores = {"azul", "verde", "morado", "naranja"};
               int colorIdx = 0;
               for (Object[] t : tiendas) {
                   int    idTienda  = (Integer) t[0];
                   String nombre    = (String)  t[1];
                   String ciudad    = (String)  t[3];
                   String direccion = (String)  t[4];
                   String color     = colores[colorIdx % colores.length];
                   colorIdx++;
        %>
            <a href="${pageContext.request.contextPath}/Tiendas?accion=informacion&id=<%= idTienda %>"
               class="tiendaInformacion">
                <div class="tiendaCard tiendaCard--<%= color %>">
                    <div class="tiendaCard__borde"></div>
                    <div class="tiendaCard__contenido">
                        <div class="tiendaCard__encabezado">
                            <h3 class="tiendaCard__titulo"><%= nombre %></h3>
                            <span class="tiendaCard__distancia">📍 <%= ciudad %></span>
                        </div>
                        <div class="tiendaCard__info">
                            <p class="tiendaCard__horario"><%= direccion %></p>
                        </div>
                        <button class="boton boton--mapa"
                            onclick="centrarMapa(<%= t[5] %>, <%= t[6] %>, '<%= nombre %>'); return false;">
                            🗺️ Ver en mapa
                        </button>
                    </div>
                </div>
            </a>
        <% } } else { %>
            <div class="tiendas-vacias">
                <p>🏪 No hay tiendas registradas aún.</p>
                <p>Puedes agregar tiendas desde la base de datos.</p>
            </div>
        <% } %>
        </div>

        <a href="${pageContext.request.contextPath}/Tiendas" class="tiendasCercanas__boton">
            <button class="boton boton--volver">Volver</button>
        </a>
    </main>

    <!-- Leaflet JS -->
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
        integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV/XN/WLEo=" crossorigin=""></script>

    <script>
        // ── Inicializar mapa ──────────────────────────────────────────────────
        const mapaLat = <%= mapLat %>;
        const mapaLng = <%= mapLng %>;

        const mapa = L.map('mapa-tiendas').setView([mapaLat, mapaLng], 14);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
            maxZoom: 19
        }).addTo(mapa);

        // ── Marcador ubicación del usuario ────────────────────────────────────
        const iconoUsuario = L.divIcon({
            html: '📍',
            iconSize: [30, 30],
            className: 'marcador-usuario'
        });

        L.marker([mapaLat, mapaLng], { icon: iconoUsuario })
            .addTo(mapa)
            .bindPopup('<b>Tu ubicación</b><br><%= ciudadUsuario %>')
            .openPopup();

        // ── Marcadores de tiendas ─────────────────────────────────────────────
        const tiendas = [
        <%
            if (tiendas != null) {
                for (int i = 0; i < tiendas.size(); i++) {
                    Object[] t = tiendas.get(i);
                    double lat = (Double) t[5];
                    double lng = (Double) t[6];
                    String nombre = ((String) t[1]).replace("'", "\\'");
                    String ciudad = ((String) t[3]).replace("'", "\\'");
                    String direccion = ((String) t[4]).replace("'", "\\'");
                    int idT = (Integer) t[0];
                    if (lat != 0 && lng != 0) {
        %>
            { id: <%= idT %>, lat: <%= lat %>, lng: <%= lng %>,
              nombre: '<%= nombre %>', ciudad: '<%= ciudad %>', direccion: '<%= direccion %>' },
        <%      }
                }
            }
        %>
        ];

        const iconoTienda = L.divIcon({
            html: '🏪',
            iconSize: [30, 30],
            className: 'marcador-tienda'
        });

        tiendas.forEach(function(t) {
            L.marker([t.lat, t.lng], { icon: iconoTienda })
                .addTo(mapa)
                .bindPopup(
                    '<b>' + t.nombre + '</b><br>' +
                    '📍 ' + t.ciudad + '<br>' +
                    t.direccion + '<br>' +
                    '<a href="${pageContext.request.contextPath}/Tiendas?accion=informacion&id=' + t.id + '">Ver detalle</a>'
                );
        });

        // ── Función para centrar el mapa desde un botón de tarjeta ────────────
        function centrarMapa(lat, lng, nombre) {
            if (lat === 0 && lng === 0) {
                alert('Esta tienda no tiene coordenadas registradas.');
                return;
            }
            mapa.flyTo([lat, lng], 16);
        }
    </script>
</body>

</html>

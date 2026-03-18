<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/utils/styles.css">
    <title>SmartHome Budget — Error del servidor</title>
    <style>
        .error-pagina {
            display: flex; flex-direction: column; align-items: center;
            justify-content: center; min-height: 100vh;
            padding: 2rem; background: #fff; text-align: center; gap: 1rem;
        }
        .error-pagina__codigo { font-size: 5rem; font-weight: 900; color: #FFCCCC; margin: 0; }
        .error-pagina__titulo { font-size: 1.4rem; font-weight: 700; color: #E51E1E; margin: 0; }
        .error-pagina__mensaje { font-size: 1rem; color: #777; max-width: 300px; line-height: 1.5; }
        .error-pagina__boton {
            margin-top: 1rem; padding: 0.9rem 2rem;
            background: #1E88E5; color: #fff; border: none;
            border-radius: 8px; font-size: 1rem;
            text-decoration: none; display: inline-block;
        }
    </style>
</head>
<body>
    <main class="error-pagina">
        <div class="error-pagina__codigo">500</div>
        <h1 class="error-pagina__titulo">Error interno</h1>
        <p class="error-pagina__mensaje">Ocurrió un error inesperado. Intenta de nuevo en unos momentos.</p>
        <a href="${pageContext.request.contextPath}/Menu" class="error-pagina__boton">Volver al menú</a>
    </main>
</body>
</html>

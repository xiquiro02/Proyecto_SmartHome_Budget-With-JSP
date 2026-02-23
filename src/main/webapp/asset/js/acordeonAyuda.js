/**
 * Funcionalidad del acordeón para la página de ayuda
 * Permite expandir/contraer las preguntas frecuentes
 */

function toggleAcordeon(header) {
    const content = header.nextElementSibling;
    const allHeaders = document.querySelectorAll('.acordeon-header');
    const allContents = document.querySelectorAll('.acordeon-content');
    
    // Cierra todos los demás acordeones
    allHeaders.forEach(h => {
        if (h !== header) {
            h.classList.remove('active');
        }
    });
    
    allContents.forEach(c => {
        if (c !== content) {
            c.classList.remove('active');
        }
    });
    
    // Toggle el acordeón actual
    header.classList.toggle('active');
    content.classList.toggle('active');
}

// Inicialización cuando el DOM está listo
document.addEventListener('DOMContentLoaded', function() {
    // Agregar event listeners a todos los acordeones
    const acordeonHeaders = document.querySelectorAll('.acordeon-header');
    acordeonHeaders.forEach(header => {
        header.addEventListener('click', function() {
            toggleAcordeon(this);
        });
    });
});

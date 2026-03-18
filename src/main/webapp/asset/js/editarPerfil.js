/**
 * Vista previa de imagen de perfil
 * Implementa preview inmediato al seleccionar archivo
 */
document.addEventListener('DOMContentLoaded', function() {
    const inputFoto = document.getElementById('foto');
    const imgPreview = document.getElementById('previewFoto');
    
    if (inputFoto && imgPreview) {
        inputFoto.addEventListener('change', function(event) {
            const archivo = event.target.files[0];
            
            if (archivo) {
                // Validar que sea una imagen
                if (!archivo.type.match('image.*')) {
                    console.error('El archivo seleccionado no es una imagen válida');
                    return;
                }
                
                // Validar tamaño (máximo 2MB)
                if (archivo.size > 2 * 1024 * 1024) {
                    console.error('La imagen excede el tamaño máximo de 2MB');
                    return;
                }
                
                // Crear FileReader para leer la imagen
                const reader = new FileReader();
                
                reader.onload = function(e) {
                    // Actualizar el src de la imagen con la vista previa
                    imgPreview.src = e.target.result;
                    imgPreview.style.borderRadius = '50%';
                    imgPreview.style.objectFit = 'cover';
                    
                    // Agregar clase para indicar que es una vista previa
                    imgPreview.classList.add('preview-activa');
                };
                
                reader.onerror = function() {
                    console.error('Error al leer la imagen seleccionada');
                };
                
                // Leer el archivo como URL de datos
                reader.readAsDataURL(archivo);
            }
        });
        
        // Agregar estilos CSS para la vista previa
        const estilo = document.createElement('style');
        estilo.textContent = `
            .preview-activa {
                border: 3px solid #4CAF50 !important;
                box-shadow: 0 0 10px rgba(76, 175, 80, 0.3) !important;
                transition: all 0.3s ease !important;
            }
            
            .preview-activa:hover {
                transform: scale(1.05);
                box-shadow: 0 0 15px rgba(76, 175, 80, 0.5) !important;
            }
        `;
        document.head.appendChild(estilo);
    }
});

/**
 * Función para resetear la vista previa si se cancela
 */
function resetearPreview() {
    const imgPreview = document.getElementById('previewFoto');
    if (imgPreview) {
        imgPreview.classList.remove('preview-activa');
        // Restaurar imagen original si es necesario
        const srcOriginal = imgPreview.getAttribute('data-src-original');
        if (srcOriginal) {
            imgPreview.src = srcOriginal;
        }
    }
}

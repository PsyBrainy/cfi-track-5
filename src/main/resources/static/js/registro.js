document.addEventListener('DOMContentLoaded', () => {
  // 2. Usar document.getElementById() en JS para capturar el formulario
  const registerForm = document.getElementById('registro');

  if (registerForm) {
    // 3. Agregar evento submit y prevenir la recarga con event.preventDefault()
    registerForm.addEventListener('submit', (event) => {
      event.preventDefault();

      // Captura de elementos e inputs
      const nombreInput = document.getElementById('Rnombre');
      const emailInput = document.getElementById('Remail');
      const contrasenaInput = document.getElementById('Rcontrasena');
      const confirmContrasenaInput = document.getElementById('r-contrasena');

      const errorNombre = document.getElementById('errorNombre');
      const errorEmail = document.getElementById('errorEmail');
      const errorContrasena = document.getElementById('errorContrasena');
      const errorConfirmContrasena = document.getElementById('errorConfirmarContrasena');

      const mensajeExito = document.getElementById('mensajeExito');

      // Limpiar errores previos
      [errorNombre, errorEmail, errorContrasena, errorConfirmContrasena, mensajeExito].forEach(el => {
        if (el) {
          el.classList.add('hidden');
          el.style.display = 'none';
        }

      });

      let isValid = true;

      // 4. Validar que el nombre no esté vacío
      if (!nombreInput.value.trim()) {
        showError(errorNombre, 'El campo nombre es obligatorio.');
        isValid = false;
      }

      // 4. Validar email con expresión regular básica
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailInput.value.trim()) {
        showError(errorEmail, 'El campo correo electrónico es obligatorio.');
        isValid = false;
      } else if (!emailRegex.test(emailInput.value.trim())) {
        showError(errorEmail, 'Ingresá un correo electrónico válido.');
        isValid = false;
      }

      // 4. Validar contraseña con longitud mínima (8 caracteres)
      if (!contrasenaInput.value) {
        showError(errorContrasena, 'La contraseña es obligatoria.');
        isValid = false;
      } else if (contrasenaInput.value.length < 8) {
        showError(errorContrasena, 'La contraseña debe tener al menos 8 caracteres.');
        isValid = false;
      }

      // 4. Validar confirmacion de contrasena
      if (confirmContrasenaInput && confirmContrasenaInput.value !== contrasenaInput.value) {
        showError(errorConfirmContrasena, 'Las contrasenas no coinciden.');
        isValid = false;
      }

      if (isValid) {
        // 1. Mostrar mensaje de éxito en la tarjeta
        const usuarioData = {
          nombre: nombreInput.value.trim(),
          email: emailInput.value.trim(),
          contrasena: contrasenaInput.value
        };

        console.log('Datos para enviar al backend', usuarioData);

        if (mensajeExito) {
          mensajeExito.textContent = '¡Registro exitoso! Redirigiendo al login...';
          mensajeExito.classList.remove('hidden');
          mensajeExito.style.display = 'block';
        }

        // 2. Redirigir a los 2 segundos a la pantalla de login
        setTimeout(() => {
          window.location.href = 'ingresar.html';
        }, 2000);
      }
    });
  }

  // 5. Función para mostrar mensajes de error en color rojo debajo de los inputs
  function showError(element, message) {
    if (!element) return;
    element.textContent = message;
    element.classList.remove('hidden');
    element.style.display = 'block';
  }
});

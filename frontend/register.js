document.addEventListener('DOMContentLoaded', () => {
  // 2. Usar document.getElementById() en JS para capturar el formulario
  const registerForm = document.getElementById('registerForm');

  if (registerForm) {
    // 3. Agregar evento submit y prevenir la recarga con event.preventDefault()
    registerForm.addEventListener('submit', (event) => {
      event.preventDefault();

      // Captura de elementos e inputs
      const nombreInput = document.getElementById('nombre');
      const emailInput = document.getElementById('email');
      const passwordInput = document.getElementById('password');

      const errorNombre = document.getElementById('errorNombre');
      const errorEmail = document.getElementById('errorEmail');
      const errorPassword = document.getElementById('errorPassword');

      // Limpiar errores previos
      [errorNombre, errorEmail, errorPassword].forEach(el => el.style.display = 'none');

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

      // 4. Validar contraseña con longitud mínima (ej. 6 caracteres)
      if (!passwordInput.value) {
        showError(errorPassword, 'La contraseña es obligatoria.');
        isValid = false;
      } else if (passwordInput.value.length < 6) {
        showError(errorPassword, 'La contraseña debe tener al menos 6 caracteres.');
        isValid = false;
      }

      if (isValid) {
        alert('¡Registro validado exitosamente!');
        registerForm.reset();
      }
    });
  }

  // 5. Función para mostrar mensajes de error en color rojo debajo de los inputs
  function showError(element, message) {
    element.textContent = message;
    element.style.display = 'block';
  }
});

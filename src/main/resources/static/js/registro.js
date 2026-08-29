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
      const mensajeError = document.getElementById('mensajeError');

      // Limpiar errores previos
      [errorNombre,
        errorEmail,
        errorContrasena,
        errorConfirmContrasena,
        mensajeExito,
        mensajeError
      ].forEach(el => {
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
      if (!confirmContrasenaInput.value) {
        showError(errorConfirmContrasena, 'Debes confirmar la contraseña.');
        isValid = false;
      } else if (confirmContrasenaInput && confirmContrasenaInput.value !== contrasenaInput.value) {
        showError(errorConfirmContrasena, 'Las contrasenas no coinciden.');
        isValid = false;
      }

      if (isValid) {
        //1. Crea objeto con los datos capturados
        const usuarioData = {
          nombre: nombreInput.value.trim(),
          email: emailInput.value.trim(),
          contrasena: contrasenaInput.value
        };

        console.log('Enviando solicitud de registro para:', usuarioData.email);

        const submitButton = registerForm.querySelector('button[type="submit"]');
        if (submitButton) {
          submitButton.disabled = true;
        }

        const config = {
          headers: {
            'Content-Type': 'application/json'
          }
        };

        axios.post('/api/usuarios/registrar', usuarioData, config)
            .then((response) => {
              console.log('Respuesta del servidor:', response.data);

              registerForm.reset();

              // Mostrar mensaje de éxito en la tarjeta
              if (mensajeExito) {
                mensajeExito.textContent = '¡Registro exitoso! Redirigiendo al login...';
                mensajeExito.classList.remove('hidden');
                mensajeExito.style.display = 'block';
              }

              // Redirigir a los 2 segundos a la pantalla de login
              setTimeout(() => {
                window.location.href = 'ingresar.html';
              }, 2000);
            })
            .catch((error) => {
              console.error('Error de la peticion:', error);

              if (!error.response) {
                showError(mensajeError, 'No se pudo conectar con el servidor. Verifica tu conexión')
                return;
              }

              const status = error.response.status;
              const data = error.response.data;
              const mensajeServidor = data?.message || data?.error;

              // Manejo de errores segun el codigo de estado
              if (status === 409) {
                showError(errorEmail, mensajeServidor || 'El correo ya esta registrado.');
              } else if (status === 400) {
                showError(mensajeError, mensajeServidor || 'Datos de registro inválidos.');
              } else if (status === 404) {
                showError(mensajeError, 'El servicio de registro no está disponible.');
              } else if (status >= 500) {
                showError(mensajeError, 'Error interno del servidor. Intenta de nuevo más tarde.');
              } else {
                showError(mensajeError, mensajeServidor || 'Ocurrió un error al procesar el registro.')
              }
            })
            .finally(() => {
              if (submitButton) {
                submitButton.disabled = false;
              }
            })
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

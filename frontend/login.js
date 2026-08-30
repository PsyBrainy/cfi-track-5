document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');

    if (loginForm) {
        loginForm.addEventListener('submit', (event) => {
            event.preventDefault();

            const emailInput = document.getElementById('email');
            const passwordInput = document.getElementById('password');

            const errorEmail = document.getElementById('errorEmail');
            const errorPassword = document.getElementById('errorPassword');

            errorEmail.style.display = 'none';
            errorPassword.style.display = 'none';

            let isValid = true;

            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailInput.value.trim()) {
                showError(errorEmail, 'El correo electrónico es obligatorio.');
                isValid = false;
            } else if (!emailRegex.test(emailInput.value.trim())) {
                showError(errorEmail, 'Ingresá un correo electrónico válido.');
                isValid = false;
            }

            if (!passwordInput.value) {
                showError(errorPassword, 'La contraseña es obligatoria.');
                isValid = false;
            }

            if (isValid) {
                alert('Validación correcta. Listo para enviar al servidor.');
            }
        });
    }

    function showError(element, message) {
        element.textContent = message;
        element.style.display = 'block';
    }
});

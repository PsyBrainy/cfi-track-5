document.addEventListener('DOMContentLoaded', () => {
    const inputMonto = document.getElementById('monto');
    const btnDepositar = document.getElementById('btn-depositar');
    const formDeposito = document.getElementById('form-deposito');
    const mensajeNotificacion = document.getElementById('mensaje-notificacion');
    const token = localStorage.getItem('token');

    if (btnDepositar) btnDepositar.disabled = true;

    if (inputMonto) {
        inputMonto.addEventListener('input', () => {
            const monto = parseFloat(inputMonto.value);
            if (!isNaN(monto) && monto > 0) {
                btnDepositar.disabled = false;
            } else {
                btnDepositar.disabled = true;
            }
        });
    }

    if (formDeposito) {
        formDeposito.addEventListener('submit', async (e) => {
            e.preventDefault();

            const monto = parseFloat(inputMonto.value);
            if (isNaN(monto) || monto <= 0) return;

            try {
                const response = await fetch('http://localhost:8080/api/transacciones/deposito', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify({ monto: monto })
                });

                if (response.ok) {
                    mostrarMensaje('¡Depósito procesado con éxito!', 'exito');
                    inputMonto.value = '';
                    btnDepositar.disabled = true;
                } else if (response.status === 401 || response.status === 403) {
                    localStorage.removeItem('token');
                    window.location.href = 'ingresar.html';
                } else {
                    mostrarMensaje('No se pudo procesar el depósito. Intente nuevamente.', 'error');
                }
            } catch (error) {
                console.error('Error al realizar el depósito:', error);
                mostrarMensaje('Error de conexión con el servidor.', 'error');
            }
        });
    }

    function mostrarMensaje(texto, tipo) {
        if (!mensajeNotificacion) return;
        mensajeNotificacion.textContent = texto;
        mensajeNotificacion.classList.remove('hidden', 'text-emerald-400', 'text-red-400');

        if (tipo === 'exito') {
            mensajeNotificacion.classList.add('text-emerald-400');
        } else {
            mensajeNotificacion.classList.add('text-red-400');
        }
    }
});
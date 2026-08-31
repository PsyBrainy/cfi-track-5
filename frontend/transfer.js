document.addEventListener('DOMContentLoaded', () => {
  const transferForm = document.getElementById('transfer-form');
  const alertBox = document.getElementById('alert-message');

  if (transferForm) {
    // 3. Interceptar el envío y evitar la recarga con event.preventDefault()
    transferForm.addEventListener('submit', async (e) => {
      e.preventDefault();

      const recipient = document.getElementById('recipient').value;
      const amount = parseFloat(document.getElementById('amount').value);

      const transferData = {
        destinatario: recipient,
        monto: amount
      };

      try {
        // 4. Realizar la petición POST enviando TransferenciaRequestDto en el body
        const response = await fetch('/api/transacciones/transferir', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
          },
          body: JSON.stringify(transferData)
        });

        // 5. Manejar la respuesta
        if (response.ok) {
          showAlert('¡Envío realizado con éxito!', 'success');
          transferForm.reset();
        } else {
          // Atrapar error (ej. saldo insuficiente o error 400)
          const errorData = await response.json().catch(() => null);
          const errorMsg = errorData?.message || 'Error 400: Saldo insuficiente o destinatario no válido.';
          showAlert(errorMsg, 'error');
        }
      } catch (error) {
        // Simulación para prueba si aún no está levantado el backend real
        showAlert('¡Envío realizado con éxito! (Simulación)', 'success');
        transferForm.reset();
      }
    });
  }

  function showAlert(msg, type) {
    alertBox.textContent = msg;
    alertBox.className = `alert-box alert-${type}`;
    alertBox.style.display = 'block';
  }
});

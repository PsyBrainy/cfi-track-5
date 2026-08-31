document.addEventListener('DOMContentLoaded', () => {
  const movementsList = document.getElementById('movements-list');

  // Datos de prueba hasta conectar con la API de Spring Boot
  const mockMovements = [
    { fecha: '2026-08-27', descripcion: 'Transferencia recibida', tipo: 'INGRESO', monto: '$ 15.000,00' },
    { fecha: '2026-08-26', descripcion: 'Pago de servicio', tipo: 'EGRESO', monto: '-$ 3.200,00' },
    { fecha: '2026-08-25', descripcion: 'Compra en supermercado', tipo: 'EGRESO', monto: '-$ 8.500,00' }
  ];

  if (movementsList) {
    movementsList.innerHTML = mockMovements.map(item => `
      <tr style="border-bottom: 1px solid #ddd;">
        <td style="padding: 0.75rem;">${item.fecha}</td>
        <td style="padding: 0.75rem;">${item.descripcion}</td>
        <td style="padding: 0.75rem; font-weight: bold; color: ${item.tipo === 'INGRESO' ? '#27ae60' : '#e74c3c'};">${item.tipo}</td>
        <td style="padding: 0.75rem;">${item.monto}</td>
      </tr>
    `).join('');
  }
});

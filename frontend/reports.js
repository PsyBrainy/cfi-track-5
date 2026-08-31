document.addEventListener('DOMContentLoaded', () => {
  const summaryContainer = document.getElementById('expense-summary');

  // Datos mock para simular el DTO ExpenseReportDto
  const reportsData = [
    { categoria: 'Servicios', total: 12500, porcentaje: 60, color: '#e74c3c' },
    { categoria: 'Supermercado', total: 8500, porcentaje: 40, color: '#f39c12' }
  ];

  if (summaryContainer) {
    summaryContainer.innerHTML = reportsData.map(item => `
      <div style="background-color: #f8f9fa; padding: 1rem; border-radius: 8px; border: 1px solid #ddd;">
        <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-weight: bold;">
          <span>${item.categoria}</span>
          <span>$ ${item.total.toLocaleString()}</span>
        </div>
        <div style="width: 100%; background-color: #e0e0e0; height: 12px; border-radius: 6px; overflow: hidden;">
          <div style="width: ${item.porcentaje}%; background-color: ${item.color}; height: 100%;"></div>
        </div>
      </div>
    `).join('');
  }
});

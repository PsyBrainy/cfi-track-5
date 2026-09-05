// Formateador de moneda en pesos argentinos
const formatCurrency = (amount) => {
    return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS',
        minimumFractionDigits: 2
    }).format(amount);
};

// Obtención e inyección del balance en el DOM
async function cargarBalance() {
    const saldoElement = document.getElementById('saldo-disponible');

    if (!saldoElement) return;

    try {
        // Se corrigió el endpoint a la ruta mapeada en CuentaController.java
        const response = await fetch('/api/cuentas/balance', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }

        const data = await response.json();
        
        // Mapea la propiedad que devuelve tu CuentaDTO (ej: data.balance, data.saldo, etc.)
        const saldoNumerico = data.balance ?? data.saldo ?? data.amount ?? data;

        saldoElement.textContent = formatCurrency(Number(saldoNumerico));

    } catch (error) {
        console.error('Error al obtener el saldo:', error);
        saldoElement.textContent = '$ 0,00';
    }
}

document.addEventListener('DOMContentLoaded', cargarBalance);
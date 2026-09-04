document.addEventListener('DOMContentLoaded', async () => {
    const contenedor = document.getElementById('reporte-gastos-contenedor');
    const badgeTotal = document.getElementById('reporte-total-gastos');

    if (!contenedor) {
        return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'ingresar.html';
        return;
    }

    try {
        const response = await fetch('/api/transacciones/reporte-gastos', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem('token');
            window.location.href = 'ingresar.html';
            return;
        }

        if (!response.ok) {
            contenedor.innerHTML = '<p class="text-xs text-center text-red-400 py-4">Error al cargar el reporte de gastos.</p>';
            return;
        }

        const reporte = await response.json();

        contenedor.innerHTML = '';

        if (!Array.isArray(reporte) || reporte.length === 0) {
            contenedor.innerHTML = '<p class="text-xs text-center text-gray-500 py-4">Aún no hay transacciones para generar un reporte.</p>';
            if (badgeTotal) badgeTotal.textContent = 'Total: $ 0,00';
            return;
        }

        // 1. Calcular la suma total acumulada de todas las transacciones
        const granTotal = reporte.reduce((acumulado, item) => acumulado + (Number(item.total) || 0), 0);

        if (badgeTotal) {
            badgeTotal.textContent = `Total operado: ${formatearMoneda(granTotal)}`;
        }

        // 2. Iterar cada categoría y generar su barra de progreso
        reporte.forEach(item => {
            const monto = Number(item.total) || 0;
            const porcentaje = granTotal > 0 ? Math.round((monto / granTotal) * 100) : 0;
            const tipo = String(item.tipoTransaccion || '').toUpperCase();

            // Configurar textos y colores según el tipo de transacción
            const config = obtenerConfiguracionTipo(tipo);

            // Contenedor de la barra de esta categoría
            const itemWrapper = document.createElement('div');
            itemWrapper.className = 'bg-[#0D0B14] p-4 rounded-xl border border-gray-800/80 hover:border-gray-700 transition';

            // Cabecera: Título, Monto y Porcentaje
            const header = document.createElement('div');
            header.className = 'flex justify-between items-center mb-2 text-xs';

            const titulo = document.createElement('span');
            titulo.className = 'font-semibold text-white flex items-center gap-2';
            titulo.innerHTML = `${config.icono} ${config.etiqueta}`;

            const infoMonto = document.createElement('div');
            infoMonto.className = 'flex items-center space-x-2';

            const textoMonto = document.createElement('span');
            textoMonto.className = `font-bold ${config.colorTexto}`;
            textoMonto.textContent = formatearMoneda(monto);

            const textoPorcentaje = document.createElement('span');
            textoPorcentaje.className = 'text-gray-400 text-[11px] bg-white/5 px-2 py-0.5 rounded-full';
            textoPorcentaje.textContent = `${porcentaje}%`;

            infoMonto.appendChild(textoMonto);
            infoMonto.appendChild(textoPorcentaje);

            header.appendChild(titulo);
            header.appendChild(infoMonto);

            // Barra de progreso exterior (track)
            const barraTrack = document.createElement('div');
            barraTrack.className = 'w-full h-2.5 bg-gray-800/90 rounded-full overflow-hidden';

            // Barra interior con relleno dinámico mediante style.width
            const barraFill = document.createElement('div');
            barraFill.className = `h-full rounded-full transition-all duration-700 ease-out ${config.gradienteBarra}`;
            barraFill.style.width = '0%'; // Inicia en 0 para animar

            barraTrack.appendChild(barraFill);

            // Ensamblar en el contenedor
            itemWrapper.appendChild(header);
            itemWrapper.appendChild(barraTrack);
            contenedor.appendChild(itemWrapper);

            // Animar el ancho después de pintar en el DOM
            requestAnimationFrame(() => {
                setTimeout(() => {
                    barraFill.style.width = `${porcentaje}%`;
                }, 50);
            });
        });

    } catch (error) {
        console.error('Error al generar el reporte de gastos:', error);
        contenedor.innerHTML = '<p class="text-xs text-center text-red-400 py-4">Error de conexión al obtener el reporte.</p>';
    }
});

function obtenerConfiguracionTipo(tipo) {
    switch (tipo) {
        case 'DEPOSITO':
            return {
                etiqueta: 'Depósitos',
                icono: '💰',
                colorTexto: 'text-emerald-400',
                gradienteBarra: 'bg-gradient-to-r from-emerald-500 to-teal-400'
            };
        case 'INGRESO':
            return {
                etiqueta: 'Transferencias Recibidas',
                icono: '📥',
                colorTexto: 'text-turquoiseNeon',
                gradienteBarra: 'bg-gradient-to-r from-cyan-400 to-turquoiseNeon'
            };
        case 'EGRESO':
            return {
                etiqueta: 'Transferencias Enviadas / Pagos',
                icono: '📤',
                colorTexto: 'text-rose-400',
                gradienteBarra: 'bg-gradient-to-r from-rose-500 to-fuchsiaNeon'
            };
        default:
            return {
                etiqueta: tipo || 'Otros',
                icono: '💳',
                colorTexto: 'text-gray-300',
                gradienteBarra: 'bg-gradient-to-r from-purple-500 to-indigo-500'
            };
    }
}

function formatearMoneda(valor) {
    return new Intl.NumberFormat('es-AR', {
        style: 'currency',
        currency: 'ARS',
        minimumFractionDigits: 2
    }).format(Math.abs(valor));
}
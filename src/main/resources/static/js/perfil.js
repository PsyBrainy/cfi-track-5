document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');

    if (!token) {
        window.location.href = 'ingresar.html';
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/api/balance', {
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
            throw new Error('Error al obtener los datos del balance');
        }

        const data = await response.json();
        console.log('Balance recibido:', data);

    } catch (error) {
        console.error('Error en la petición:', error);
    }
});
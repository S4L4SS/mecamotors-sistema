import { apiClient } from './api';

// Ajusta la ruta según el controlador real del backend
export async function crearCita(cita) {
  const res = await apiClient.post('/api/citas', cita);
  return res.data;
}

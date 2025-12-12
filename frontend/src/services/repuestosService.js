import { apiClient } from './api';

export async function obtenerRepuestos() {
  const res = await apiClient.get('/api/repuestos');
  return res.data;
}

export async function crearRepuesto(repuesto) {
  const res = await apiClient.post('/api/repuestos', repuesto);
  return res.data;
}

export async function actualizarRepuesto(id, repuesto) {
  const res = await apiClient.put(`/api/repuestos/${id}`, repuesto);
  return res.data;
}

export async function eliminarRepuesto(id) {
  const res = await apiClient.delete(`/api/repuestos/${id}`);
  return res.data;
}

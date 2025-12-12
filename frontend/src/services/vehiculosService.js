import { apiClient } from './api';

export async function obtenerVehiculos() {
  const res = await apiClient.get('/api/vehiculos');
  return res.data;
}

export async function obtenerVehiculosPorCliente(clienteId) {
  const res = await apiClient.get(`/api/vehiculos/cliente/${clienteId}`);
  return res.data;
}

export async function crearVehiculo(vehiculo) {
  const res = await apiClient.post('/api/vehiculos', vehiculo);
  return res.data;
}

export async function actualizarVehiculo(id, vehiculo) {
  const res = await apiClient.put(`/api/vehiculos/${id}`, vehiculo);
  return res.data;
}

export async function eliminarVehiculo(id) {
  const res = await apiClient.delete(`/api/vehiculos/${id}`);
  return res.data;
}

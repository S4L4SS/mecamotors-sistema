import { apiClient } from './api';

export async function obtenerCotizaciones() {
  const res = await apiClient.get('/api/cotizaciones');
  return res.data;
}

export async function obtenerCotizacionesPorCliente(clienteId) {
  const res = await apiClient.get(`/api/cotizaciones/cliente/${clienteId}`);
  return res.data;
}

export async function obtenerCotizacion(id) {
  const res = await apiClient.get(`/api/cotizaciones/${id}`);
  return res.data;
}

export async function crearCotizacion(cotizacion) {
  const res = await apiClient.post('/api/cotizaciones', cotizacion);
  return res.data;
}

export async function cambiarEstadoCotizacion(id, nuevoEstado) {
  const res = await apiClient.put(`/api/cotizaciones/${id}/estado`, JSON.stringify(nuevoEstado), {
    headers: { 'Content-Type': 'application/json' }
  });
  return res.data;
}

export async function eliminarCotizacion(id) {
  const res = await apiClient.delete(`/api/cotizaciones/${id}`);
  return res.data;
}

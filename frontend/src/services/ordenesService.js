import { apiClient } from './api';

export async function consultarEstado({ placa, numeroOrden }) {
  const params = {};
  if (placa) params.placa = placa;
  if (numeroOrden) params.numeroOrden = numeroOrden;
  const res = await apiClient.get('/api/ordenes/estado', { params });
  return res.data;
}

export async function listarOrdenes() {
  const res = await apiClient.get('/api/ordenes');
  return res.data;
}

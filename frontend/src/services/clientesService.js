import { apiClient } from './api';

export async function listarClientes() {
  const res = await apiClient.get('/api/clientes');
  return res.data;
}

export async function crearCliente(cliente) {
  const res = await apiClient.post('/api/clientes', cliente);
  return res.data;
}

export async function actualizarCliente(id, cliente) {
  const res = await apiClient.put(`/api/clientes/${id}`, cliente);
  return res.data;
}

export async function buscarClientes(texto) {
  const res = await apiClient.get('/api/clientes/buscar', { params: { q: texto } });
  return res.data;
}

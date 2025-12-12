import { apiClient } from './api';

export async function login({ username, password }) {
  const res = await apiClient.post('/api/auth/login', { username, password });
  return res.data;
}

export async function register({ username, password, email, nombre, apellido }) {
  const res = await apiClient.post('/api/auth/register', { 
    username, 
    password, 
    email,
    nombre,
    apellido
  });
  return res.data;
}

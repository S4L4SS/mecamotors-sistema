import { apiClient } from './api';

export async function login({ username, password }) {
  const res = await apiClient.post('/api/auth/login', { username, password });
  return res.data;
}

import React, { createContext, useContext, useState } from 'react';
import { login as loginService } from '../services/authService';
import { setAuthToken } from '../services/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('mm_token'));
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('mm_user');
    return stored ? JSON.parse(stored) : null;
  });

  const isAuthenticated = !!token;

  const login = async (username, password) => {
    const data = await loginService({ username, password });
    // Se asume JwtResponse: { token, type, id, username, email, roles }
    setToken(data.token);
    setUser(data);
    localStorage.setItem('mm_token', data.token);
    localStorage.setItem('mm_user', JSON.stringify(data));
    setAuthToken(data.token);
    return data;
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('mm_token');
    localStorage.removeItem('mm_user');
    setAuthToken(null);
  };

  const hasAnyRole = (roles) => {
    if (!user || !user.roles) return false;
    return user.roles.some((r) => roles.includes(r));
  };

  const value = { token, user, isAuthenticated, login, logout, hasAnyRole };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}

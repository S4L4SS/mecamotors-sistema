import React from 'react';
import { useAuth } from '../context/AuthContext';

export default function Topbar() {
  const { user, logout } = useAuth();

  return (
    <header className="topbar">
      <div className="topbar-left">
        <h1 className="topbar-title">Panel Administrativo</h1>
      </div>
      <div className="topbar-right">
        {user && (
          <>
            <span className="user-info">
              {user.username} ({user.roles?.join(', ')})
            </span>
            <button className="btn btn-outline" onClick={logout}>
              Cerrar sesión
            </button>
          </>
        )}
      </div>
    </header>
  );
}

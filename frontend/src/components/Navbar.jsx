import React from 'react';
import { Link, NavLink } from 'react-router-dom';

export default function Navbar() {
  return (
    <header className="navbar">
      <div className="navbar-left">
        <Link to="/" className="logo">
          Mecamotors
        </Link>
      </div>
      <nav className="navbar-right">
        <NavLink to="/home" end>
          Inicio
        </NavLink>
        <a href="#servicios">Servicios</a>
        <NavLink to="/solicitar-cita">Solicitar Cita</NavLink>
        <NavLink to="/consultar-estado">Consultar Estado</NavLink>
        <NavLink to="/admin">Panel</NavLink>
      </nav>
    </header>
  );
}

import React from 'react';
import { NavLink } from 'react-router-dom';

export default function AdminSidebar() {
  return (
    <aside className="admin-sidebar">
      <h2 className="sidebar-title">Mecamotors</h2>
      <nav className="sidebar-nav">
        <NavLink to="/admin" end>
          Dashboard
        </NavLink>
        <NavLink to="/admin/clientes">Clientes</NavLink>
        <NavLink to="/admin/vehiculos">Vehículos</NavLink>
        <NavLink to="/admin/ordenes">Órdenes de Servicio</NavLink>
        <NavLink to="/admin/diagnostico">Diagnóstico y Reparación</NavLink>
        <NavLink to="/admin/inventario">Inventario</NavLink>
        <NavLink to="/admin/pedidos">Pedidos a Proveedores</NavLink>
        <NavLink to="/admin/cotizaciones">Cotizaciones</NavLink>
        <NavLink to="/admin/agenda">Agenda Técnica</NavLink>
        <NavLink to="/admin/reportes">Reportes</NavLink>
      </nav>
    </aside>
  );
}

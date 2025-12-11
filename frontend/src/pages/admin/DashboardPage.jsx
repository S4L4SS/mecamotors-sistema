import React from 'react';
import { Link } from 'react-router-dom';

export default function DashboardPage() {
  return (
    <div className="page">
      <h1>Dashboard</h1>
      <p>Accesos rápidos a los módulos principales del sistema.</p>
      <div className="dashboard-grid">
        <Link to="/admin/clientes" className="card link-card">
          <h3>Gestión de Clientes</h3>
          <p>Registrar, listar y editar clientes.</p>
        </Link>
        <Link to="/admin/vehiculos" className="card link-card">
          <h3>Vehículos</h3>
          <p>Registro y consulta de vehículos.</p>
        </Link>
        <Link to="/admin/ordenes" className="card link-card">
          <h3>Órdenes de Servicio</h3>
          <p>
            Seguimiento de estados: diagnóstico, reparación, terminado, entregado.
          </p>
        </Link>
        <Link to="/admin/inventario" className="card link-card">
          <h3>Inventario de Repuestos</h3>
          <p>Stock, entradas y salidas.</p>
        </Link>
        <Link to="/admin/pedidos" className="card link-card">
          <h3>Pedidos a Proveedores</h3>
          <p>Arequipa, Lima, importación.</p>
        </Link>
        <Link to="/admin/reportes" className="card link-card">
          <h3>Reportes</h3>
          <p>Costos, repuestos faltantes, servicios realizados.</p>
        </Link>
      </div>
    </div>
  );
}

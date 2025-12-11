import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

import PublicLayout from './layouts/PublicLayout';
import AdminLayout from './layouts/AdminLayout';

import HomePage from './pages/public/HomePage';
import AppointmentPage from './pages/public/AppointmentPage';
import TrackStatusPage from './pages/public/TrackStatusPage';

import LoginPage from './pages/admin/LoginPage';
import DashboardPage from './pages/admin/DashboardPage';
import ClientesPage from './pages/admin/ClientesPage';
import VehiculosPage from './pages/admin/VehiculosPage';
import OrdenesPage from './pages/admin/OrdenesPage';
import DiagnosticoPage from './pages/admin/DiagnosticoPage';
import InventarioPage from './pages/admin/InventarioPage';
import PedidosPage from './pages/admin/PedidosPage';
import CotizacionesPage from './pages/admin/CotizacionesPage';
import AgendaPage from './pages/admin/AgendaPage';
import ReportesPage from './pages/admin/ReportesPage';

import { useAuth } from './context/AuthContext';

function PrivateRoute({ children, roles }) {
  const { isAuthenticated, hasAnyRole } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/admin/login" replace />;
  }

  if (roles && !hasAnyRole(roles)) {
    return <Navigate to="/admin" replace />;
  }

  return children;
}

export default function App() {
  return (
    <Routes>
      {/* Rutas públicas */}
      <Route element={<PublicLayout />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/solicitar-cita" element={<AppointmentPage />} />
        <Route path="/consultar-estado" element={<TrackStatusPage />} />
      </Route>

      {/* Login admin */}
      <Route path="/admin/login" element={<LoginPage />} />

      {/* Panel administrativo */}
      <Route
        path="/admin"
        element={
          <PrivateRoute roles={['ADMIN', 'RECEPCIONISTA', 'MECANICO', 'GERENTE']}>
            <AdminLayout />
          </PrivateRoute>
        }
      >
        <Route index element={<DashboardPage />} />
        <Route path="clientes" element={<ClientesPage />} />
        <Route path="vehiculos" element={<VehiculosPage />} />
        <Route path="ordenes" element={<OrdenesPage />} />
        <Route path="diagnostico" element={<DiagnosticoPage />} />
        <Route path="inventario" element={<InventarioPage />} />
        <Route path="pedidos" element={<PedidosPage />} />
        <Route path="cotizaciones" element={<CotizacionesPage />} />
        <Route path="agenda" element={<AgendaPage />} />
        <Route path="reportes" element={<ReportesPage />} />
      </Route>

      {/* Fallback */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

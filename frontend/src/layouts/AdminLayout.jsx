import React, { useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import AdminSidebar from '../components/AdminSidebar';
import Topbar from '../components/Topbar';
import { useAuth } from '../context/AuthContext';
import { setAuthToken } from '../services/api';

export default function AdminLayout() {
  const { token } = useAuth();

  useEffect(() => {
    setAuthToken(token);
  }, [token]);

  return (
    <div className="admin-layout">
      <AdminSidebar />
      <div className="admin-main">
        <Topbar />
        <div className="admin-content">
          <Outlet />
        </div>
      </div>
    </div>
  );
}

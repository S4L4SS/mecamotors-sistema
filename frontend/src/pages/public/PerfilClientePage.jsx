import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { obtenerCotizacionesPorCliente } from '../../services/cotizacionesService';
import { listarClientes } from '../../services/clientesService';

export default function PerfilClientePage() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [cotizaciones, setCotizaciones] = useState([]);
  const [cliente, setCliente] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    cargarDatos();
  }, [user]);

  const cargarDatos = async () => {
    try {
      // Obtener el cliente asociado al usuario
      const clientes = await listarClientes();
      const miCliente = clientes.find(c => c.usuarioId === user?.id);
      
      if (miCliente) {
        setCliente(miCliente);
        const cotizs = await obtenerCotizacionesPorCliente(miCliente.id);
        setCotizaciones(cotizs);
      }
    } catch (error) {
      console.error('Error al cargar datos:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/', { replace: true });
  };

  if (loading) {
    return <div className="page"><p>Cargando...</p></div>;
  }

  return (
    <div className="page">
      <div style={{ maxWidth: '1200px', margin: '0 auto' }}>
        <h1>Mi Perfil - Mecamotors</h1>
        
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', marginTop: '30px' }}>
          {/* Información de la cuenta */}
          <div className="login-card">
            <h2>Información de la cuenta</h2>
            
            <div style={{ marginTop: '20px' }}>
              <p><strong>Usuario:</strong> {user?.username}</p>
              <p><strong>Email:</strong> {user?.email}</p>
              <p><strong>Tipo de cuenta:</strong> Cliente</p>
            </div>

            <div style={{ marginTop: '30px', display: 'flex', flexDirection: 'column', gap: '10px' }}>
              <button 
                className="btn btn-primary" 
                onClick={() => navigate('/solicitar-cotizacion')}
              >
                + Nueva Cotización
              </button>
              <button 
                className="btn btn-primary" 
                onClick={() => navigate('/solicitar-cita')}
              >
                Solicitar Cita
              </button>
              <button 
                className="btn btn-primary" 
                onClick={() => navigate('/consultar-estado')}
              >
                Consultar Estado de Orden
              </button>
              <button 
                className="btn" 
                onClick={handleLogout}
                style={{ backgroundColor: '#dc3545', color: 'white', marginTop: '10px' }}
              >
                Cerrar Sesión
              </button>
            </div>
          </div>

          {/* Mis Cotizaciones */}
          <div className="login-card">
            <h2>Mis Cotizaciones</h2>
            
            {cotizaciones.length === 0 ? (
              <p style={{ marginTop: '20px', color: '#666' }}>
                No tienes cotizaciones aún. ¡Solicita tu primera cotización!
              </p>
            ) : (
              <div style={{ marginTop: '20px' }}>
                {cotizaciones.map(cot => (
                  <div key={cot.id} style={{ 
                    padding: '15px', 
                    marginBottom: '10px', 
                    border: '1px solid #ddd', 
                    borderRadius: '8px',
                    backgroundColor: '#f9f9f9'
                  }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
                      <div>
                        <p><strong>Vehículo:</strong> {cot.vehiculoPlaca} - {cot.vehiculoMarca} {cot.vehiculoModelo}</p>
                        <p><strong>Fecha:</strong> {new Date(cot.fecha).toLocaleDateString()}</p>
                        <p><strong>Total:</strong> S/. {cot.total.toFixed(2)}</p>
                      </div>
                      <span style={{ 
                        padding: '5px 10px', 
                        borderRadius: '4px',
                        backgroundColor: cot.estado === 'APROBADA' ? '#d4edda' : 
                                       cot.estado === 'RECHAZADA' ? '#f8d7da' : '#fff3cd',
                        color: cot.estado === 'APROBADA' ? '#155724' : 
                               cot.estado === 'RECHAZADA' ? '#721c24' : '#856404',
                        fontSize: '12px',
                        fontWeight: 'bold'
                      }}>
                        {cot.estado}
                      </span>
                    </div>
                    {cot.descripcion && (
                      <p style={{ marginTop: '10px', fontSize: '14px', color: '#666' }}>
                        {cot.descripcion}
                      </p>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

import React, { useState, useEffect } from 'react';
import { obtenerCotizaciones, cambiarEstadoCotizacion } from '../../services/cotizacionesService';
import DataTable from '../../components/DataTable';

export default function CotizacionesPage() {
  const [cotizaciones, setCotizaciones] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedCotizacion, setSelectedCotizacion] = useState(null);

  useEffect(() => {
    cargarCotizaciones();
  }, []);

  const cargarCotizaciones = async () => {
    try {
      const data = await obtenerCotizaciones();
      setCotizaciones(data);
    } catch (error) {
      console.error('Error al cargar cotizaciones:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCambiarEstado = async (id, nuevoEstado) => {
    try {
      await cambiarEstadoCotizacion(id, nuevoEstado);
      cargarCotizaciones();
    } catch (error) {
      console.error('Error al cambiar estado:', error);
      alert('Error al cambiar el estado de la cotización');
    }
  };

  const columnas = [
    { field: 'id', header: 'ID' },
    { field: 'clienteNombre', header: 'Cliente' },
    { 
      field: 'vehiculo', 
      header: 'Vehículo',
      body: (row) => `${row.vehiculoPlaca} - ${row.vehiculoMarca} ${row.vehiculoModelo}`
    },
    { 
      field: 'fecha', 
      header: 'Fecha',
      body: (row) => new Date(row.fecha).toLocaleDateString()
    },
    { 
      field: 'total', 
      header: 'Total',
      body: (row) => `S/. ${row.total.toFixed(2)}`
    },
    { 
      field: 'estado', 
      header: 'Estado',
      body: (row) => (
        <span style={{ 
          padding: '5px 10px', 
          borderRadius: '4px',
          backgroundColor: row.estado === 'APROBADA' ? '#d4edda' : 
                         row.estado === 'RECHAZADA' ? '#f8d7da' : '#fff3cd',
          color: row.estado === 'APROBADA' ? '#155724' : 
                 row.estado === 'RECHAZADA' ? '#721c24' : '#856404',
          fontSize: '12px',
          fontWeight: 'bold'
        }}>
          {row.estado}
        </span>
      )
    }
  ];

  if (loading) return <div className="page"><p>Cargando...</p></div>;

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1>Cotizaciones</h1>
      </div>

      <DataTable
        data={cotizaciones}
        columns={columnas}
        onRowClick={(row) => setSelectedCotizacion(row)}
      />

      {selectedCotizacion && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: 'rgba(0,0,0,0.5)',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          zIndex: 1000
        }}>
          <div style={{
            backgroundColor: 'white',
            padding: '30px',
            borderRadius: '8px',
            maxWidth: '800px',
            width: '90%',
            maxHeight: '90vh',
            overflow: 'auto'
          }}>
            <h2>Detalle de Cotización #{selectedCotizacion.id}</h2>
            
            <div style={{ marginTop: '20px' }}>
              <p><strong>Cliente:</strong> {selectedCotizacion.clienteNombre}</p>
              <p><strong>Vehículo:</strong> {selectedCotizacion.vehiculoPlaca} - {selectedCotizacion.vehiculoMarca} {selectedCotizacion.vehiculoModelo}</p>
              <p><strong>Fecha:</strong> {new Date(selectedCotizacion.fecha).toLocaleString()}</p>
              <p><strong>Estado:</strong> {selectedCotizacion.estado}</p>
              {selectedCotizacion.descripcion && (
                <p><strong>Descripción:</strong> {selectedCotizacion.descripcion}</p>
              )}
            </div>

            <h3 style={{ marginTop: '20px' }}>Items</h3>
            <table style={{ width: '100%', marginTop: '10px', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ backgroundColor: '#f5f5f5' }}>
                  <th style={{ padding: '10px', textAlign: 'left', border: '1px solid #ddd' }}>Repuesto</th>
                  <th style={{ padding: '10px', textAlign: 'center', border: '1px solid #ddd' }}>Cant.</th>
                  <th style={{ padding: '10px', textAlign: 'right', border: '1px solid #ddd' }}>P. Unit.</th>
                  <th style={{ padding: '10px', textAlign: 'right', border: '1px solid #ddd' }}>Subtotal</th>
                </tr>
              </thead>
              <tbody>
                {selectedCotizacion.detalles.map(det => (
                  <tr key={det.id}>
                    <td style={{ padding: '10px', border: '1px solid #ddd' }}>
                      {det.repuestoNombre}
                      {det.descripcion && (
                        <div style={{ fontSize: '12px', color: '#666', marginTop: '5px' }}>
                          {det.descripcion}
                        </div>
                      )}
                    </td>
                    <td style={{ padding: '10px', textAlign: 'center', border: '1px solid #ddd' }}>{det.cantidad}</td>
                    <td style={{ padding: '10px', textAlign: 'right', border: '1px solid #ddd' }}>S/. {det.precioUnitario.toFixed(2)}</td>
                    <td style={{ padding: '10px', textAlign: 'right', border: '1px solid #ddd' }}>S/. {det.subtotal.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
              <tfoot>
                <tr style={{ backgroundColor: '#f5f5f5', fontWeight: 'bold' }}>
                  <td colSpan="3" style={{ padding: '10px', textAlign: 'right', border: '1px solid #ddd' }}>TOTAL:</td>
                  <td style={{ padding: '10px', textAlign: 'right', border: '1px solid #ddd' }}>S/. {selectedCotizacion.total.toFixed(2)}</td>
                </tr>
              </tfoot>
            </table>

            <div style={{ marginTop: '30px', display: 'flex', gap: '10px', justifyContent: 'flex-end' }}>
              {selectedCotizacion.estado === 'PENDIENTE' && (
                <>
                  <button
                    className="btn btn-primary"
                    onClick={() => {
                      handleCambiarEstado(selectedCotizacion.id, 'APROBADA');
                      setSelectedCotizacion(null);
                    }}
                    style={{ backgroundColor: '#28a745' }}
                  >
                    Aprobar
                  </button>
                  <button
                    className="btn"
                    onClick={() => {
                      handleCambiarEstado(selectedCotizacion.id, 'RECHAZADA');
                      setSelectedCotizacion(null);
                    }}
                    style={{ backgroundColor: '#dc3545', color: 'white' }}
                  >
                    Rechazar
                  </button>
                </>
              )}
              <button className="btn" onClick={() => setSelectedCotizacion(null)}>
                Cerrar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

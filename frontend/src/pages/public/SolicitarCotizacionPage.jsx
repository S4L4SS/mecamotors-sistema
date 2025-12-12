import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { listarClientes } from '../../services/clientesService';
import { obtenerVehiculosPorCliente } from '../../services/vehiculosService';
import { obtenerRepuestos } from '../../services/repuestosService';
import { crearCotizacion } from '../../services/cotizacionesService';

export default function SolicitarCotizacionPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [cliente, setCliente] = useState(null);
  const [vehiculos, setVehiculos] = useState([]);
  const [repuestos, setRepuestos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [enviando, setEnviando] = useState(false);
  const [error, setError] = useState(null);
  const [exito, setExito] = useState(false);

  const [formData, setFormData] = useState({
    vehiculoId: '',
    descripcion: '',
    detalles: [{ repuestoId: '', cantidad: 1, descripcion: '' }]
  });

  useEffect(() => {
    cargarDatos();
  }, [user]);

  const cargarDatos = async () => {
    try {
      const clientes = await listarClientes();
      const miCliente = clientes.find(c => c.usuarioId === user?.id);
      
      if (miCliente) {
        setCliente(miCliente);
        const vehs = await obtenerVehiculosPorCliente(miCliente.id);
        setVehiculos(vehs);
      }

      const reps = await obtenerRepuestos();
      setRepuestos(reps);
    } catch (err) {
      console.error('Error al cargar datos:', err);
      setError('Error al cargar los datos necesarios');
    } finally {
      setLoading(false);
    }
  };

  const agregarDetalle = () => {
    setFormData({
      ...formData,
      detalles: [...formData.detalles, { repuestoId: '', cantidad: 1, descripcion: '' }]
    });
  };

  const removerDetalle = (index) => {
    const nuevosDetalles = formData.detalles.filter((_, i) => i !== index);
    setFormData({ ...formData, detalles: nuevosDetalles });
  };

  const actualizarDetalle = (index, campo, valor) => {
    const nuevosDetalles = [...formData.detalles];
    nuevosDetalles[index][campo] = valor;
    setFormData({ ...formData, detalles: nuevosDetalles });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setEnviando(true);

    try {
      const detallesValidos = formData.detalles.filter(d => d.repuestoId && d.cantidad > 0);
      
      if (detallesValidos.length === 0) {
        throw new Error('Debes agregar al menos un repuesto');
      }

      const cotizacion = {
        clienteId: cliente.id,
        vehiculoId: parseInt(formData.vehiculoId),
        descripcion: formData.descripcion,
        detalles: detallesValidos.map(d => ({
          repuestoId: parseInt(d.repuestoId),
          cantidad: parseInt(d.cantidad),
          descripcion: d.descripcion
        }))
      };

      await crearCotizacion(cotizacion);
      setExito(true);
      
      setTimeout(() => {
        navigate('/mi-perfil');
      }, 2000);
    } catch (err) {
      console.error('Error al crear cotización:', err);
      setError(err.message || 'Error al crear la cotización');
    } finally {
      setEnviando(false);
    }
  };

  if (loading) {
    return <div className="page"><p>Cargando...</p></div>;
  }

  return (
    <div className="page">
      <div style={{ maxWidth: '900px', margin: '0 auto' }}>
        <h1>Solicitar Cotización</h1>
        
        <form className="form" onSubmit={handleSubmit} style={{ marginTop: '30px' }}>
          <div className="form-group">
            <label>Vehículo *</label>
            <select
              className="input"
              value={formData.vehiculoId}
              onChange={(e) => setFormData({ ...formData, vehiculoId: e.target.value })}
              required
            >
              <option value="">Selecciona un vehículo</option>
              {vehiculos.map(v => (
                <option key={v.id} value={v.id}>
                  {v.placa} - {v.marca} {v.modelo} ({v.anio})
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Descripción general</label>
            <textarea
              className="input"
              value={formData.descripcion}
              onChange={(e) => setFormData({ ...formData, descripcion: e.target.value })}
              rows="3"
              placeholder="Describe brevemente lo que necesitas..."
            />
          </div>

          <div style={{ marginTop: '30px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '15px' }}>
              <h3>Repuestos / Servicios</h3>
              <button type="button" className="btn btn-primary" onClick={agregarDetalle}>
                + Agregar ítem
              </button>
            </div>

            {formData.detalles.map((detalle, index) => (
              <div key={index} style={{ 
                padding: '15px', 
                marginBottom: '15px', 
                border: '1px solid #ddd', 
                borderRadius: '8px',
                backgroundColor: '#f9f9f9'
              }}>
                <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr auto', gap: '10px', alignItems: 'end' }}>
                  <div className="form-group" style={{ marginBottom: 0 }}>
                    <label>Repuesto / Servicio</label>
                    <select
                      className="input"
                      value={detalle.repuestoId}
                      onChange={(e) => actualizarDetalle(index, 'repuestoId', e.target.value)}
                      required
                    >
                      <option value="">Selecciona...</option>
                      {repuestos.map(r => (
                        <option key={r.id} value={r.id}>
                          {r.nombre} - S/. {r.precio}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group" style={{ marginBottom: 0 }}>
                    <label>Cantidad</label>
                    <input
                      type="number"
                      className="input"
                      value={detalle.cantidad}
                      onChange={(e) => actualizarDetalle(index, 'cantidad', e.target.value)}
                      min="1"
                      required
                    />
                  </div>

                  {formData.detalles.length > 1 && (
                    <button
                      type="button"
                      onClick={() => removerDetalle(index)}
                      style={{ 
                        padding: '10px 15px', 
                        backgroundColor: '#dc3545', 
                        color: 'white', 
                        border: 'none', 
                        borderRadius: '4px', 
                        cursor: 'pointer' 
                      }}
                    >
                      ✕
                    </button>
                  )}
                </div>

                <div className="form-group" style={{ marginTop: '10px', marginBottom: 0 }}>
                  <label>Observaciones (opcional)</label>
                  <input
                    type="text"
                    className="input"
                    value={detalle.descripcion}
                    onChange={(e) => actualizarDetalle(index, 'descripcion', e.target.value)}
                    placeholder="Ej: Color específico, marca preferida..."
                  />
                </div>
              </div>
            ))}
          </div>

          <div style={{ marginTop: '30px', display: 'flex', gap: '10px' }}>
            <button type="submit" className="btn btn-primary" disabled={enviando}>
              {enviando ? 'Enviando...' : 'Solicitar Cotización'}
            </button>
            <button 
              type="button" 
              className="btn" 
              onClick={() => navigate('/mi-perfil')}
              disabled={enviando}
            >
              Cancelar
            </button>
          </div>

          {error && <p className="alert error" style={{ marginTop: '20px' }}>{error}</p>}
          {exito && (
            <p className="alert success" style={{ marginTop: '20px' }}>
              ¡Cotización solicitada exitosamente! Redirigiendo...
            </p>
          )}
        </form>
      </div>
    </div>
  );
}

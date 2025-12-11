import React, { useState } from 'react';
import { consultarEstado } from '../../services/ordenesService';

export default function TrackStatusPage() {
  const [placa, setPlaca] = useState('');
  const [numeroOrden, setNumeroOrden] = useState('');
  const [resultado, setResultado] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setResultado(null);
    setError(null);
    setLoading(true);

    try {
      const data = await consultarEstado({ placa, numeroOrden });
      setResultado(data);
    } catch (err) {
      console.error(err);
      setError('No se encontró información. Verifica que el backend tenga /api/ordenes/estado.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="page">
      <h1>Consultar Estado del Vehículo</h1>
      <p>Ingresa la placa o el número de orden de servicio para ver el estado actual.</p>

      <form className="form" onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="form-group">
            <label>Placa</label>
            <input
              value={placa}
              onChange={(e) => setPlaca(e.target.value)}
              className="input"
              placeholder="ABC-123"
            />
          </div>
          <div className="form-group">
            <label>Número de orden</label>
            <input
              value={numeroOrden}
              onChange={(e) => setNumeroOrden(e.target.value)}
              className="input"
              placeholder="Ej: 2024-00123"
            />
          </div>
        </div>
        <button className="btn btn-primary" disabled={loading}>
          {loading ? 'Buscando...' : 'Consultar'}
        </button>
      </form>

      {error && <p className="alert error">{error}</p>}

      {resultado && (
        <div className="card mt">
          <h2>Resultado</h2>
          <p>
            <strong>Estado actual:</strong> {resultado.estado}
          </p>
          {resultado.descripcion && (
            <p>
              <strong>Detalle:</strong> {resultado.descripcion}
            </p>
          )}
          {resultado.ultimaActualizacion && (
            <p>
              <strong>Última actualización:</strong> {resultado.ultimaActualizacion}
            </p>
          )}
        </div>
      )}
    </section>
  );
}

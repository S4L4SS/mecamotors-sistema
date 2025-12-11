import React, { useState } from 'react';
import { crearCita } from '../../services/citasService';

const initialForm = {
  nombre: '',
  telefono: '',
  placa: '',
  servicio: 'DIAGNOSTICO',
  fecha: '',
};

export default function AppointmentPage() {
  const [form, setForm] = useState(initialForm);
  const [loading, setLoading] = useState(false);
  const [mensaje, setMensaje] = useState(null);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMensaje(null);
    setError(null);

    try {
      await crearCita(form);
      setMensaje('Cita registrada correctamente. Nos contactaremos contigo.');
      setForm(initialForm);
    } catch (err) {
      console.error(err);
      setError('Ocurrió un error al registrar la cita. Verifica que el backend tenga /api/citas.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="page">
      <h1>Solicitar Cita</h1>
      <p>Completa el siguiente formulario para agendar una cita en Mecamotors.</p>

      <form className="form" onSubmit={handleSubmit}>
        <div className="form-grid">
          <div className="form-group">
            <label>Nombre completo</label>
            <input
              name="nombre"
              value={form.nombre}
              onChange={handleChange}
              required
              className="input"
            />
          </div>
          <div className="form-group">
            <label>Teléfono</label>
            <input
              name="telefono"
              value={form.telefono}
              onChange={handleChange}
              required
              className="input"
            />
          </div>
          <div className="form-group">
            <label>Placa</label>
            <input
              name="placa"
              value={form.placa}
              onChange={handleChange}
              required
              className="input"
            />
          </div>
          <div className="form-group">
            <label>Servicio</label>
            <select
              name="servicio"
              value={form.servicio}
              onChange={handleChange}
              className="input"
            >
              <option value="DIAGNOSTICO">Diagnóstico</option>
              <option value="MANTENIMIENTO">Mantenimiento</option>
              <option value="REPUESTOS">Repuestos</option>
              <option value="REPARACION">Reparación</option>
            </select>
          </div>
          <div className="form-group">
            <label>Fecha preferida</label>
            <input
              type="date"
              name="fecha"
              value={form.fecha}
              onChange={handleChange}
              required
              className="input"
            />
          </div>
        </div>

        <button className="btn btn-primary" type="submit" disabled={loading}>
          {loading ? 'Enviando...' : 'Enviar solicitud'}
        </button>

        {mensaje && <p className="alert success">{mensaje}</p>}
        {error && <p className="alert error">{error}</p>}
      </form>
    </section>
  );
}

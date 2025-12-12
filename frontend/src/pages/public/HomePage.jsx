import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export default function HomePage() {
  const { isAuthenticated, login } = useAuth();
  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await login(username, password); // llama a /api/auth/login en el backend
      navigate('/admin', { replace: true });
    } catch (err) {
      console.error(err);
      setError('Credenciales inválidas o error en el servidor.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="home-page">
      <section className="hero">
        <div className="hero-text">
          <h1>Mecamotors</h1>
          <h2>Tu taller automotriz de confianza en Arequipa</h2>
          <p>
            Especialistas en diagnóstico, mantenimiento, reparación e instalación de repuestos para
            todo tipo de vehículos.
          </p>
          <div className="hero-actions">
            <Link className="btn btn-primary" to="/solicitar-cita">
              Solicitar Cita
            </Link>
            <Link className="btn btn-outline" to="/consultar-estado">
              Consultar Estado de mi Vehículo
            </Link>
          </div>
        </div>
        <div className="hero-image">
          <div className="hero-card">
            <h3>Misión</h3>
            <p>
              Brindar un servicio automotriz confiable, rápido y transparente, priorizando la
              seguridad y satisfacción de nuestros clientes.
            </p>
          </div>
          <div className="hero-card">
            <h3>Visión</h3>
            <p>
              Ser el taller automotriz líder en Arequipa, reconocido por la calidad técnica y la
              excelencia en la atención al cliente.
            </p>
          </div>
        </div>
      </section>

      <section id="servicios" className="services-section">
        <h2>Servicios</h2>
        <div className="services-grid">
          <article className="service-card">
            <h3>Diagnóstico</h3>
            <p>Escaneo computarizado y diagnóstico integral de tu vehículo.</p>
          </article>
          <article className="service-card">
            <h3>Mantenimiento</h3>
            <p>Cambio de aceite, filtros, frenos, alineamiento y mantenimiento preventivo.</p>
          </article>
          <article className="service-card">
            <h3>Repuestos</h3>
            <p>Venta e instalación de repuestos originales y alternativos.</p>
          </article>
          <article className="service-card">
            <h3>Reparación</h3>
            <p>Reparación de motor, sistema eléctrico, suspensión y más, realizada por especialistas.</p>
          </article>
        </div>
      </section>

      <section className="page" style={{ marginTop: '2rem' }}>
        <h2>Acceso para personal de Mecamotors</h2>
        {isAuthenticated ? (
          <div className="card mt">
            <p>Ya has iniciado sesión.</p>
            <button className="btn btn-primary" onClick={() => navigate('/admin')}>
              Ir al panel administrativo
            </button>
          </div>
        ) : (
          <form className="form" onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-group">
                <label>Usuario</label>
                <input
                  className="input"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label>Contraseña</label>
                <input
                  type="password"
                  className="input"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
            </div>
            <button className="btn btn-primary" disabled={loading}>
              {loading ? 'Ingresando...' : 'Iniciar sesión'}
            </button>
            {error && <p className="alert error">{error}</p>}
          </form>
        )}
      </section>
    </div>
  );
}

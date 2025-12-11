import React from 'react';
import { Link } from 'react-router-dom';

export default function HomePage() {
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
    </div>
  );
}

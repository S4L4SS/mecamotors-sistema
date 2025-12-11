import React from 'react';

export default function OrdenesPage() {
  return (
    <div className="page">
      <h1>Órdenes de Servicio</h1>
      <p>
        Módulo para gestionar órdenes con estados: En diagnóstico, Cotización pendiente, Aprobada, En
        reparación, Control de calidad, Terminado, Entregado.
      </p>
      <p>Aquí puedes conectar un CRUD de órdenes usando /api/ordenes.</p>
    </div>
  );
}

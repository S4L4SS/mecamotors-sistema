import React, { useEffect, useState } from 'react';
import DataTable from '../../components/DataTable';
import {
  listarClientes,
  crearCliente,
  actualizarCliente,
  buscarClientes,
} from '../../services/clientesService';

const emptyCliente = {
  id: null,
  nombre: '',
  telefono: '',
  email: '',
  documento: '',
};

export default function ClientesPage() {
  const [clientes, setClientes] = useState([]);
  const [busqueda, setBusqueda] = useState('');
  const [form, setForm] = useState(emptyCliente);
  const [editing, setEditing] = useState(false);

  const cargarClientes = async () => {
    try {
      const data = busqueda ? await buscarClientes(busqueda) : await listarClientes();
      setClientes(data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    cargarClientes();
  }, []);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editing) {
        await actualizarCliente(form.id, form);
      } else {
        await crearCliente(form);
      }
      setForm(emptyCliente);
      setEditing(false);
      await cargarClientes();
    } catch (err) {
      console.error(err);
    }
  };

  const handleRowClick = (cliente) => {
    setForm(cliente);
    setEditing(true);
  };

  const columns = [
    { key: 'nombre', label: 'Nombre' },
    { key: 'telefono', label: 'Teléfono' },
    { key: 'email', label: 'Email' },
    { key: 'documento', label: 'Documento' },
  ];

  return (
    <div className="page">
      <div className="page-header">
        <h1>Gestión de Clientes</h1>
      </div>

      <div className="page-actions">
        <input
          className="input"
          placeholder="Buscar por nombre / documento..."
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
        <button className="btn btn-outline" onClick={cargarClientes}>
          Buscar
        </button>
      </div>

      <div className="layout-two-cols">
        <div>
          <h2>Listado</h2>
          <DataTable columns={columns} data={clientes} onRowClick={handleRowClick} />
        </div>

        <div>
          <h2>{editing ? 'Editar Cliente' : 'Nuevo Cliente'}</h2>
          <form className="form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Nombre</label>
              <input
                name="nombre"
                className="input"
                value={form.nombre}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label>Teléfono</label>
              <input
                name="telefono"
                className="input"
                value={form.telefono}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label>Email</label>
              <input
                name="email"
                className="input"
                value={form.email}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label>Documento</label>
              <input
                name="documento"
                className="input"
                value={form.documento}
                onChange={handleChange}
              />
            </div>
            <button className="btn btn-primary" type="submit">
              {editing ? 'Actualizar' : 'Crear'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export default function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const userData = await login(username, password);
      
      // Verificar si el usuario tiene roles administrativos
      const rolesAdministrativos = ['ADMIN', 'RECEPCIONISTA', 'MECANICO', 'GERENTE'];
      const tieneAccesoAdmin = userData.roles && userData.roles.some(rol => rolesAdministrativos.includes(rol));
      
      if (tieneAccesoAdmin) {
        navigate('/admin', { replace: true });
      } else {
        // Usuario normal (CLIENTE) - redirigir a su perfil
        navigate('/mi-perfil', { replace: true });
      }
    } catch (err) {
      console.error(err);
      setError('Credenciales inválidas o error en el servidor.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <h1>Mecamotors - Panel</h1>
        <form className="form" onSubmit={handleSubmit}>
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
          <button className="btn btn-primary" disabled={loading}>
            {loading ? 'Ingresando...' : 'Ingresar'}
          </button>
          {error && <p className="alert error">{error}</p>}
        </form>
        
        <div style={{ textAlign: 'center', marginTop: '20px' }}>
          <Link to="/admin/register" style={{ color: '#007bff', textDecoration: 'none' }}>
            ¿No tienes cuenta? Regístrate aquí
          </Link>
        </div>
      </div>
    </div>
  );
}

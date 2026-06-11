import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)   // ← NUEVO: controla visibilidad
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')

    try {
      const data = await login({ username, password })
      navigate(data.rol === 'ADMIN' ? '/admin' : '/dashboard')
    } catch (err) {
      setError(err.message || 'Credenciales inválidas')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-box">
          <div className="login-logo">
            <span className="login-logo-icon">🏢</span>
            <h1>Edificio</h1>
            <p className="subtitle">Gestión de Facturación</p>
            <span className="building-info">6 Pisos • 8 Departamentos</span>
          </div>

          {error && <div className="login-error">{error}</div>}

          <form className="login-form" onSubmit={handleSubmit}>

            {/* USUARIO */}
            <div className="form-group">
              <label>Usuario</label>
              <div className="form-input-wrapper">
                <span className="form-icon">👤</span>
                <input
                  className="form-input"
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="admin o dpto101"
                  required
                  autoComplete="username"
                />
              </div>
            </div>

            {/* CONTRASEÑA con toggle de visibilidad */}
            <div className="form-group">
              <label>Contraseña</label>
              <div className="form-input-wrapper">
                <span className="form-icon">🔒</span>
                <input
                  className="form-input"
                  type={showPassword ? 'text' : 'password'}   // ← NUEVO: cambia tipo
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••"
                  required
                  autoComplete="current-password"
                />
                {/* BOTÓN OJO */}
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowPassword(!showPassword)}
                  tabIndex={-1}
                  aria-label={showPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'}
                >
                  {showPassword ? '🙈' : '👁️'}
                </button>
              </div>
            </div>

            <button type="submit" className="login-btn" disabled={loading}>
              {loading ? 'Ingresando...' : 'Ingresar'}
            </button>
          </form>

          <div className="login-footer">
            <p>Edificio Residencial</p>
            <div className="floors">
              {[1,2,3,4,5,6].map(f => (
                <span key={f} className="floor-dot active" />
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
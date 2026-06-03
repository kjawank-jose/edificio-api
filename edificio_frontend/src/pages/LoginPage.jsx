import React, { useState } from 'react'
import { useAuth } from '../hooks/useAuth'
import { useToast } from '../components/ToastContainer'
import { login as loginApi } from '../services/api'

export default function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const { addToast } = useToast()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const { data } = await loginApi({ username, password })
      login(data)
      addToast(`Bienvenido, ${data.username}`, 'success')
    } catch (err) {
      addToast(err.response?.data?.message || 'Credenciales inválidas', 'error')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-container">
      <div className="login-box">
        <h1>🏢 Edificio</h1>
        <p className="subtitle">Gestión de Facturación</p>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Usuario</label>
            <input
              id="username"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="admin o inquilino"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Contraseña</label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••"
              required
            />
          </div>
          <button type="submit" className="btn btn-primary w-full" disabled={loading}>
            {loading ? <span className="loading" /> : 'Ingresar'}
          </button>
        </form>
      </div>
    </div>
  )
}
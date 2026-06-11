import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
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
    <div style={{
      minHeight: '100vh',
      width: '100%',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      background: 'linear-gradient(135deg, #0d1b2a 0%, #1b263b 100%)',
      padding: '20px',
      position: 'relative',
      boxSizing: 'border-box'
    }}>
      <div style={{
        width: '100%',
        maxWidth: '400px',
        position: 'relative',
        zIndex: 1
      }}>
        <div style={{
          background: 'white',
          borderRadius: '20px',
          padding: '40px 30px',
          boxShadow: '0 20px 60px rgba(0,0,0,0.3)',
          animation: 'fadeIn 0.6s ease'
        }}>

          {/* LOGO */}
          <div style={{ textAlign: 'center', marginBottom: '30px' }}>
            <span style={{ fontSize: '50px', display: 'block', marginBottom: '10px' }}>🏢</span>
            <h1 style={{ fontSize: '28px', color: '#1a237e', margin: 0 }}>Edificio</h1>
            <p style={{ fontSize: '15px', color: '#64748b', marginTop: '6px', fontWeight: 500 }}>Gestión de Facturación</p>
            <span style={{
              display: 'inline-block',
              marginTop: '10px',
              padding: '6px 16px',
              background: 'linear-gradient(135deg, #e3f2fd, #fff3e0)',
              borderRadius: '20px',
              fontSize: '12px',
              fontWeight: 700,
              color: '#1565c0',
              textTransform: 'uppercase',
              letterSpacing: '1px'
            }}>6 Pisos • 8 Departamentos</span>
          </div>

          {/* ERROR */}
          {error && (
            <div style={{
              background: '#ffebee',
              color: '#c62828',
              padding: '12px',
              borderRadius: '8px',
              textAlign: 'center',
              fontSize: '14px',
              marginBottom: '16px'
            }}>
              {error}
            </div>
          )}

          {/* FORMULARIO */}
          <form onSubmit={handleSubmit} style={{
            display: 'flex',
            flexDirection: 'column',
            gap: '16px'
          }}>

            {/* USUARIO */}
            <div>
              <label style={{
                fontSize: '12px',
                fontWeight: 'bold',
                color: '#555',
                textTransform: 'uppercase',
                marginBottom: '6px',
                display: 'block'
              }}>Usuario</label>
              <div style={{ position: 'relative' }}>
                <span style={{
                  position: 'absolute',
                  left: '18px',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  fontSize: '20px',
                  opacity: 0.6,
                  pointerEvents: 'none',
                  zIndex: 2
                }}>👤</span>
                <input
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="admin o dpto101"
                  required
                  style={{
                    width: '100%',
                    padding: '16px 20px 16px 52px',
                    border: '2px solid #e2e8f0',
                    borderRadius: '16px',
                    fontSize: '16px',
                    background: '#f8fafc',
                    color: '#1e293b',
                    outline: 'none',
                    fontFamily: 'inherit',
                    boxSizing: 'border-box'
                  }}
                />
              </div>
            </div>

            {/* CONTRASEÑA */}
            <div>
              <label style={{
                fontSize: '12px',
                fontWeight: 'bold',
                color: '#555',
                textTransform: 'uppercase',
                marginBottom: '6px',
                display: 'block'
              }}>Contraseña</label>
              <div style={{ position: 'relative' }}>
                <span style={{
                  position: 'absolute',
                  left: '18px',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  fontSize: '20px',
                  opacity: 0.6,
                  pointerEvents: 'none',
                  zIndex: 2
                }}>🔒</span>
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••"
                  required
                  style={{
                    width: '100%',
                    padding: '16px 20px 16px 52px',
                    border: '2px solid #e2e8f0',
                    borderRadius: '16px',
                    fontSize: '16px',
                    background: '#f8fafc',
                    color: '#1e293b',
                    outline: 'none',
                    fontFamily: 'inherit',
                    boxSizing: 'border-box'
                  }}
                />
              </div>
            </div>

            {/* BOTÓN */}
            <button
              type="submit"
              disabled={loading}
              style={{
                width: '100%',
                padding: '16px',
                border: 'none',
                borderRadius: '12px',
                background: 'linear-gradient(135deg, #1565c0, #1976d2)',
                color: 'white',
                fontSize: '16px',
                fontWeight: 'bold',
                cursor: loading ? 'not-allowed' : 'pointer',
                marginTop: '8px',
                opacity: loading ? 0.6 : 1,
                transition: 'transform 0.2s, box-shadow 0.2s'
              }}
            >
              {loading ? 'Ingresando...' : 'Ingresar'}
            </button>
          </form>

          {/* FOOTER */}
          <div style={{
            textAlign: 'center',
            marginTop: '32px',
            paddingTop: '24px',
            borderTop: '1px solid #e2e8f0'
          }}>
            <p style={{ fontSize: '13px', color: '#94a3b8', margin: 0 }}>Edificio Residencial</p>
            <div style={{ display: 'inline-flex', gap: '4px', marginTop: '8px' }}>
              {[1,2,3,4,5,6].map(f => (
                <span key={f} style={{
                  width: '8px',
                  height: '8px',
                  borderRadius: '50%',
                  background: 'linear-gradient(135deg, #1976d2, #42a5f5)'
                }} />
              ))}
            </div>
          </div>

        </div>
      </div>
    </div>
  )
}
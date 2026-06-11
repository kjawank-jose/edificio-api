import React, { useState } from 'react'
import { useLocation, useNavigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Layout() {
  const { user, logout } = useAuth()
  const location = useLocation()
  const navigate = useNavigate()
  const [menuOpen, setMenuOpen] = useState(false)
  const isAdmin = user?.rol === 'ADMIN'

  const navItems = [
    { path: '/dashboard', label: '📊 Dashboard', show: true },
    { path: '/historial', label: '📜 Historial', show: true },
    { path: '/admin', label: '⚙️ Admin', show: isAdmin },
  ]

  const visibleItems = navItems.filter(i => i.show)

  const toggleTheme = () => {
    const html = document.documentElement
    const current = html.getAttribute('data-theme')
    const next = current === 'light' ? 'dark' : 'light'
    html.setAttribute('data-theme', next)
    localStorage.setItem('theme', next)
  }

  React.useEffect(() => {
    const saved = localStorage.getItem('theme')
    if (saved) document.documentElement.setAttribute('data-theme', saved)
  }, [])

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="app-container">
      {/* Sidebar - visible en desktop, oculto en móvil */}
      <aside className={`sidebar ${menuOpen ? 'sidebar-open' : ''}`}>
        <div className="sidebar-brand">🏢 Edificio</div>
        <nav className="sidebar-nav">
          {visibleItems.map(item => (
            <button
              key={item.path}
              className={`sidebar-link ${location.pathname === item.path ? 'active' : ''}`}
              onClick={() => {
                navigate(item.path)
                setMenuOpen(false)
              }}
            >
              {item.label}
            </button>
          ))}
        </nav>
      </aside>

      {/* Overlay para cerrar menú en móvil */}
      {menuOpen && <div className="sidebar-overlay" onClick={() => setMenuOpen(false)} />}

      <main className="main-content">
        <div className="container">
          {/* Header responsive */}
          <header className="header">
            <div className="header-left">
              {/* Botón hamburguesa - solo en móvil */}
              <button
                className="hamburger-btn"
                onClick={() => setMenuOpen(!menuOpen)}
                aria-label="Menú"
              >
                ☰
              </button>
              <h1>
                {visibleItems.find(i => i.path === location.pathname)?.label || '🏢 Edificio'}
              </h1>
            </div>

            <div className="header-actions">
              <button className="btn btn-icon btn-secondary" onClick={toggleTheme} title="Cambiar tema">
                🌓
              </button>
              <div className="user-info desktop-only">
                <span>{user?.username}</span>
                <span className={`badge badge-${isAdmin ? 'admin' : 'inquilino'}`}>
                  {user?.rol}
                </span>
              </div>
              <button className="btn btn-sm btn-danger" onClick={handleLogout}>
                🚪 <span className="desktop-only">Salir</span>
              </button>
            </div>
          </header>

          {/* Contenido de la ruta */}
          <Outlet />
        </div>
      </main>
    </div>
  )
}

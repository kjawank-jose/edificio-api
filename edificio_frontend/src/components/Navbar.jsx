import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { getUser, logout, isAdmin } from '../services/auth';
import './Navbar.css';

export default function Navbar() {
  const [menuOpen, setMenuOpen] = useState(false);
  const user = getUser();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const toggleMenu = () => setMenuOpen(!menuOpen);

  const navItems = [
    { label: 'Edificio', path: '/' },
    { label: 'Dashboard', path: user?.rol === 'ADMIN' ? '/admin' : '/inquilino' },
    { label: 'Historial', path: '/historial' },
  ];

  if (isAdmin()) {
    navItems.push({ label: 'Admin', path: '/admin' });
  }

  return (
    <nav className="navbar">
      <div className="nav-brand">
        <span>🏢 Edificio</span>
        <button className="hamburger" onClick={toggleMenu} aria-label="Menú">
          ☰
        </button>
      </div>

      <ul className={`nav-links ${menuOpen ? 'open' : ''}`}>
        {navItems.map((item) => (
          <li key={item.label}>
            <button
              className={location.pathname === item.path ? 'active' : ''}
              onClick={() => {
                navigate(item.path);
                setMenuOpen(false);
              }}
            >
              {item.label}
            </button>
          </li>
        ))}
        <li>
          <button className="logout-btn" onClick={handleLogout}>
            🚪 Salir
          </button>
        </li>
      </ul>

      {menuOpen && <div className="overlay" onClick={() => setMenuOpen(false)} />}
    </nav>
  );
}

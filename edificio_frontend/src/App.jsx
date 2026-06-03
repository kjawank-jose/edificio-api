import { Routes, Route, Navigate } from 'react-router-dom'
import LoginPage from './components/LoginPage'
import Layout from './components/Layout'
import CobrosTable from './components/CobrosTable'
import StatsCard from './components/StatsCard'
import { useAuth } from './hooks/useAuth'

// Placeholders para rutas que aún no tengas componentes dedicados
const Dashboard = () => (
  <div>
    <StatsCard />
    <CobrosTable />
  </div>
)

const Historial = () => (
  <div className="card">
    <h2>Historial de Pagos</h2>
    <p>Próximamente...</p>
  </div>
)

const AdminPanel = () => (
  <div className="card">
    <h2>Panel de Administración</h2>
    <p>Configuración del edificio...</p>
  </div>
)

// Protección de rutas
function ProtectedLayout() {
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" replace />
  return <Layout />
}

export default function App() {
  return (
    <Routes>
      {/* Login público */}
      <Route path="/login" element={<LoginPage />} />

      {/* Rutas protegidas con Layout (sidebar + header responsive) */}
      <Route element={<ProtectedLayout />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/historial" element={<Historial />} />
        <Route path="/admin" element={<AdminPanel />} />
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
      </Route>

      {/* Cualquier otra ruta → login */}
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  )
}
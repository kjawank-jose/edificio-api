import React, { useState, useEffect } from 'react'
import { useAuth } from '../hooks/useAuth'
import { useToast } from '../components/ToastContainer'
import { getDepartamentos, abrirMes, ingresarDatos, cerrarMes } from '../services/api'
import AbrirMesModal from '../components/AbrirMesModal'
import IngresarDatosModal from '../components/IngresarDatosModal'
import CerrarMesModal from '../components/CerrarMesModal'

export default function AdminPage() {
  const [departamentos, setDepartamentos] = useState([])
  const [loading, setLoading] = useState(true)
  const [modal, setModal] = useState(null)
  const { addToast } = useToast()

  useEffect(() => {
    loadDepartamentos()
  }, [])

  const loadDepartamentos = async () => {
    try {
      const { data } = await getDepartamentos()
      setDepartamentos(data)
    } catch (err) {
      addToast('Error al cargar departamentos', 'error')
    } finally {
      setLoading(false)
    }
  }

  const handleAbrirMes = async (data) => {
    try {
      await abrirMes(data)
      addToast('✅ Mes abierto correctamente', 'success')
      setModal(null)
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al abrir mes', 'error')
    }
  }

  const handleIngresar = async (data) => {
    try {
      await ingresarDatos(data)
      addToast('✅ Datos ingresados correctamente', 'success')
      setModal(null)
      loadDepartamentos()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al ingresar datos', 'error')
    }
  }

  const handleCerrar = async (data) => {
    try {
      await cerrarMes(data)
      addToast('✅ Mes cerrado y nuevo mes abierto', 'success')
      setModal(null)
      loadDepartamentos()
    } catch (err) {
      addToast(err.response?.data?.message || 'Error al cerrar mes', 'error')
    }
  }

  return (
    <div className="page">
      <div className="grid">
        <div className="card">
          <div className="card-header">
            <span className="card-title">Abrir Nuevo Mes</span>
          </div>
          <p className="card-subtitle">Inicia un nuevo periodo de facturación</p>
          <button className="btn btn-success mt-2" onClick={() => setModal('abrir')}>
            ➕ Abrir Mes
          </button>
        </div>
        <div className="card">
          <div className="card-header">
            <span className="card-title">Ingresar Datos</span>
          </div>
          <p className="card-subtitle">Registra recibos y lecturas del mes actual</p>
          <button className="btn btn-primary mt-2" onClick={() => setModal('ingresar')}>
            📝 Ingresar Datos
          </button>
        </div>
        <div className="card">
          <div className="card-header">
            <span className="card-title">Cerrar Mes</span>
          </div>
          <p className="card-subtitle">Cierra el mes actual y abre el siguiente</p>
          <button className="btn btn-warning mt-2" onClick={() => setModal('cerrar')}>
            🔒 Cerrar Mes
          </button>
        </div>
      </div>

      <div className="table-container">
        <div className="table-header">
          <h2>🏢 Departamentos</h2>
        </div>
        <table>
          <thead>
            <tr>
              <th>Código</th>
              <th>Nombre</th>
              <th>Propietario</th>
              <th>Piso</th>
              <th>Tiene Luz</th>
              <th>Últ. Lectura Agua</th>
              <th>Últ. Lectura Luz</th>
            </tr>
          </thead>
          <tbody>
            {departamentos.map(d => (
              <tr key={d.codigo}>
                <td><strong>{d.codigo}</strong></td>
                <td>{d.nombre}</td>
                <td>{d.propietario}</td>
                <td>{d.piso}</td>
                <td>{d.tieneLuz ? '✅' : '❌'}</td>
                <td>{d.ultimaLecturaAgua}</td>
                <td>{d.ultimaLecturaLuz || '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {modal === 'abrir' && <AbrirMesModal onSubmit={handleAbrirMes} onClose={() => setModal(null)} />}
      {modal === 'ingresar' && <IngresarDatosModal departamentos={departamentos} onSubmit={handleIngresar} onClose={() => setModal(null)} />}
      {modal === 'cerrar' && <CerrarMesModal onSubmit={handleCerrar} onClose={() => setModal(null)} />}
    </div>
  )
}
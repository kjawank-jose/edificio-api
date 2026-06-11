import React, { useState, useEffect } from 'react'
import { useToast } from '../components/ToastContainer'
import { getHistorial, getMesByPeriodo } from '../services/api'
import DetalleMesModal from '../components/DetalleMesModal'

export default function HistorialPage() {
  const [historial, setHistorial] = useState([])
  const [loading, setLoading] = useState(true)
  const [selectedPeriodo, setSelectedPeriodo] = useState(null)
  const [detalle, setDetalle] = useState(null)
  const { addToast } = useToast()

  useEffect(() => {
    loadHistorial()
  }, [])

  const loadHistorial = async () => {
    try {
      setLoading(true)
      const { data } = await getHistorial()
      setHistorial(data)
    } catch (err) {
      addToast('Error al cargar historial', 'error')
    } finally {
      setLoading(false)
    }
  }

  const verDetalle = async (periodo) => {
    try {
      const { data } = await getMesByPeriodo(periodo)
      setDetalle(data)
      setSelectedPeriodo(periodo)
    } catch (err) {
      addToast('Error al cargar detalle', 'error')
    }
  }

  if (loading) {
    return (
      <div className="page">
        <div className="table-container">
          <div className="table-header"><h2>📜 Historial de Meses</h2></div>
          <div className="empty-state">Cargando...</div>
        </div>
      </div>
    )
  }

  return (
    <div className="page">
      <div className="table-container">
        <div className="table-header">
          <h2>📜 Historial de Meses</h2>
        </div>
        <table>
          <thead>
            <tr>
              <th>Periodo</th>
              <th>Mes</th>
              <th>Estado</th>
              <th>Total General</th>
              <th>Fecha Cierre</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {historial.length === 0 ? (
              <tr>
                <td colSpan={6} className="empty-state">No hay historial disponible</td>
              </tr>
            ) : (
              historial.map(h => (
                <tr key={h.periodo}>
                  <td><strong>{h.periodo}</strong></td>
                  <td>{h.nombreMes}</td>
                  <td>
                    <span className={`status status-${h.estado.toLowerCase()}`}>
                      {h.estado}
                    </span>
                  </td>
                  <td>S/ {(h.totalGeneral || 0).toFixed(2)}</td>
                  <td>{h.fechaCierre || '-'}</td>
                  <td>
                    <button className="btn btn-sm btn-primary" onClick={() => verDetalle(h.periodo)}>
                      👁️ Ver
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {detalle && (
        <DetalleMesModal 
          data={detalle} 
          onClose={() => { setDetalle(null); setSelectedPeriodo(null); }} 
        />
      )}
    </div>
  )
}

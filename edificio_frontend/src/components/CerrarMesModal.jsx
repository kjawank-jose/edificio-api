import React, { useState } from 'react'

export default function CerrarMesModal({ onSubmit, onClose }) {
  const [periodoActual, setPeriodoActual] = useState('')
  const [proximoPeriodo, setProximoPeriodo] = useState('')
  const [proximoNombre, setProximoNombre] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    await onSubmit({ periodoActual, proximoPeriodo, proximoNombre })
    setLoading(false)
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>🔒 Cerrar Mes y Abrir Siguiente</h2>
          <button className="close-btn" onClick={onClose}>&times;</button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>Periodo Actual</label>
              <input type="text" value={periodoActual} onChange={(e) => setPeriodoActual(e.target.value)} placeholder="2026-05" required />
            </div>
            <div className="form-group">
              <label>Próximo Periodo</label>
              <input type="text" value={proximoPeriodo} onChange={(e) => setProximoPeriodo(e.target.value)} placeholder="2026-06" required />
            </div>
          </div>
          <div className="form-group">
            <label>Nombre del Próximo Mes</label>
            <input type="text" value={proximoNombre} onChange={(e) => setProximoNombre(e.target.value)} placeholder="Junio 2026" required />
          </div>
          <div style={{ background: 'var(--bg-card)', padding: 16, borderRadius: 8, margin: '16px 0' }}>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
              ⚠️ Esta acción cerrará el mes actual, actualizará las últimas lecturas de cada departamento y abrirá automáticamente el nuevo periodo.
            </p>
          </div>
          <button type="submit" className="btn btn-warning" disabled={loading}>
            {loading ? <span className="loading" /> : '🔒 Confirmar Cierre de Mes'}
          </button>
        </form>
      </div>
    </div>
  )
}

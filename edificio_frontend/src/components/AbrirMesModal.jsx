import React, { useState } from 'react'

export default function AbrirMesModal({ onSubmit, onClose }) {
  const [periodo, setPeriodo] = useState('')
  const [nombreMes, setNombreMes] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    await onSubmit({ periodo, nombreMes })
    setLoading(false)
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>➕ Abrir Nuevo Mes</h2>
          <button className="close-btn" onClick={onClose}>&times;</button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>Periodo (año-mes)</label>
              <input
                type="text"
                value={periodo}
                onChange={(e) => setPeriodo(e.target.value)}
                placeholder="2026-05"
                required
              />
            </div>
            <div className="form-group">
              <label>Nombre del Mes</label>
              <input
                type="text"
                value={nombreMes}
                onChange={(e) => setNombreMes(e.target.value)}
                placeholder="Mayo 2026"
                required
              />
            </div>
          </div>
          <button type="submit" className="btn btn-success" disabled={loading}>
            {loading ? <span className="loading" /> : '➕ Abrir Mes'}
          </button>
        </form>
      </div>
    </div>
  )
}
import React, { useState } from 'react'

export default function IngresarDatosModal({ departamentos, onSubmit, onClose }) {
  const [periodo, setPeriodo] = useState('')
  const [sedapalM3, setSedapalM3] = useState('')
  const [sedapalImporte, setSedapalImporte] = useState('')
  const [luzKwh, setLuzKwh] = useState('')
  const [luzImporte, setLuzImporte] = useState('')
  const [lecturasAgua, setLecturasAgua] = useState({})
  const [lecturasLuz, setLecturasLuz] = useState({})
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    await onSubmit({
      periodo,
      sedapalM3: parseFloat(sedapalM3),
      sedapalImporte: parseFloat(sedapalImporte),
      luzKwh: parseFloat(luzKwh),
      luzImporte: parseFloat(luzImporte),
      lecturasAgua,
      lecturasLuz,
    })
    setLoading(false)
  }

  const dptosConLuz = departamentos.filter(d => d.tieneLuz)

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>📝 Ingresar Datos del Mes</h2>
          <button className="close-btn" onClick={onClose}>&times;</button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Periodo</label>
            <input type="text" value={periodo} onChange={(e) => setPeriodo(e.target.value)} placeholder="2026-05" required />
          </div>

          <h3 style={{ margin: '20px 0 12px', fontSize: '1rem' }}>💧 Recibo SEDAPAL</h3>
          <div className="form-row">
            <div className="form-group">
              <label>m³ Consumidos</label>
              <input type="number" step="0.001" value={sedapalM3} onChange={(e) => setSedapalM3(e.target.value)} required />
            </div>
            <div className="form-group">
              <label>Importe (S/)</label>
              <input type="number" step="0.01" value={sedapalImporte} onChange={(e) => setSedapalImporte(e.target.value)} required />
            </div>
          </div>

          <h3 style={{ margin: '20px 0 12px', fontSize: '1rem' }}>⚡ Recibo Luz</h3>
          <div className="form-row">
            <div className="form-group">
              <label>kWh Consumidos</label>
              <input type="number" step="0.01" value={luzKwh} onChange={(e) => setLuzKwh(e.target.value)} required />
            </div>
            <div className="form-group">
              <label>Importe (S/)</label>
              <input type="number" step="0.01" value={luzImporte} onChange={(e) => setLuzImporte(e.target.value)} required />
            </div>
          </div>

          <h3 style={{ margin: '20px 0 12px', fontSize: '1rem' }}>📊 Lecturas de Agua por Departamento</h3>
          <div className="lecturas-grid">
            {departamentos.map(d => (
              <div className="lectura-item" key={d.codigo}>
                <label>💧 {d.codigo} - {d.nombre}</label>
                <small style={{ color: 'var(--text-secondary)', display: 'block', marginBottom: 4 }}>
                  Última lectura: {d.ultimaLecturaAgua}
                </small>
                <input
                  type="number"
                  step="0.001"
                  placeholder="Ingrese lectura actual"
                  onChange={(e) => setLecturasAgua(prev => ({ ...prev, [d.codigo]: parseFloat(e.target.value) }))}
                  required
                />
              </div>
            ))}
          </div>

          <h3 style={{ margin: '20px 0 12px', fontSize: '1rem' }}>📊 Lecturas de Luz por Departamento</h3>
          <div className="lecturas-grid">
            {dptosConLuz.map(d => (
              <div className="lectura-item" key={d.codigo}>
                <label>⚡ {d.codigo} - {d.nombre}</label>
                <small style={{ color: 'var(--text-secondary)', display: 'block', marginBottom: 4 }}>
                  Última lectura: {d.ultimaLecturaLuz || 0}
                </small>
                <input
                  type="number"
                  step="0.01"
                  placeholder="Ingrese lectura actual"
                  onChange={(e) => setLecturasLuz(prev => ({ ...prev, [d.codigo]: parseFloat(e.target.value) }))}
                  required
                />
              </div>
            ))}
          </div>

          <button type="submit" className="btn btn-primary mt-3" disabled={loading}>
            {loading ? <span className="loading" /> : '💾 Guardar Datos'}
          </button>
        </form>
      </div>
    </div>
  )
}
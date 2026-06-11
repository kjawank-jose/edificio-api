import React from 'react'
import StatsCard from './StatsCard'

export default function DetalleMesModal({ data, onClose }) {
  const stats = [
    { title: 'Agua', value: `S/ ${(data.totalRecaudadoAgua || 0).toFixed(2)}`, subtitle: `${data.sedapalM3} m³ | S/ ${data.sedapalImporte}` },
    { title: 'Luz', value: `S/ ${(data.totalRecaudadoLuz || 0).toFixed(2)}`, subtitle: `${data.luzKwh} kWh | S/ ${data.luzImporte}` },
    { title: 'Total General', value: `S/ ${(data.totalGeneral || 0).toFixed(2)}`, subtitle: `Área común: S/ ${(data.areaComunPorDpto || 0).toFixed(2)}` },
  ]

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" style={{ maxWidth: 900 }} onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>📋 {data.nombreMes} ({data.periodo})</h2>
          <button className="close-btn" onClick={onClose}>&times;</button>
        </div>

        <div className="grid" style={{ marginBottom: 20 }}>
          {stats.map((s, i) => <StatsCard key={i} {...s} />)}
        </div>

        <table>
          <thead>
            <tr>
              <th>Depto</th>
              <th>Propietario</th>
              <th>Agua</th>
              <th>Luz</th>
              <th>Área Común</th>
              <th>Total</th>
            </tr>
          </thead>
          <tbody>
            {data.cobros.map(c => (
              <tr key={c.codigoDpto}>
                <td>
                  <strong>{c.codigoDpto}</strong>
                  <br />
                  <small>{c.nombreDpto}</small>
                </td>
                <td>{c.propietario}</td>
                <td>S/ {(c.pagoAgua || 0).toFixed(2)}</td>
                <td>{c.tieneLuz ? `S/ ${(c.pagoLuz || 0).toFixed(2)}` : '-'}</td>
                <td>S/ {(c.pagoAreaComun || 0).toFixed(2)}</td>
                <td><strong>S/ {(c.pagoTotal || 0).toFixed(2)}</strong></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
